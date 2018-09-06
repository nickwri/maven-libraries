package uk.co.referencepoint.publishtest.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;

// Handles both POST and GET using okHttp - http://square.github.io/okhttp/
// All response objects MUST extend the BaseClientResponse class
// Http code is set in the base class
// Uses Gzip compression by default - no need to set Accept-Encoding: gzip header.

public class HttpRequest {

    private final String TAG = "httpRequest";

    //BaseHttpResponse.enumHTTPVerb httpMethod;

    // okHttp Notes:
    // To configure timeouts, use a builder. See https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/ConfigureTimeouts.java
    // You can also configure connection failure retry. See retryOnConnectionFailure(...) - default is true. See https://medium.com/inloop/okhttp-is-quietly-retrying-requests-is-your-api-ready-19489ef35ace
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(constants.HTTP_REQUEST_SERVER_CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(constants.HTTP_REQUEST_SERVER_WRITE_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(constants.HTTP_REQUEST_SERVER_READ_TIME_OUT, TimeUnit.SECONDS)
            .build();

    private Context context;

    //region constructor
    private String url;                 // this is the full endpoint url e.g. https://vircarda-beta.rplwebsites.net/api/registration/GetSecurityQuestions

    //private String authHeaderName = "Authorization";      // needs to be soft as MS uses "App"
    private String apiKeyWithPrefix;                      // include any prefix when specifying this e.g. ApiKey PIfUM2YyBE6XqmYEB7mpCi6Do4BfWxXikwjEIVnIEBfg
    private boolean isPost;

    public HttpRequest(Context _context, String _url) {
        url = _url;
        context = _context;
    }

    // INCLUDE any prefix when specifying the apikey e.g. ApiKey PIfUM2YyBE6XqmYEB7mpCi6Do4BfWxXikwjEIVnIEBfg
    public HttpRequest(Context _context, String _url, String _apikeyWithPrefix) {
        url = _url;
        apiKeyWithPrefix = _apikeyWithPrefix;
        context = _context;
    }

//    public HttpRequest(Context _context, String _url, String _apikeyWithPrefix, String _authHeaderName) {
//        url = _url;
//        apiKeyWithPrefix = _apikeyWithPrefix;
//        context = _context;
//        authHeaderName = _authHeaderName;
//    }
    //endregion

    public <S extends BaseHttpResponse> S get(HashMap<String, String> requestParams, Class<S> responseType) {
        return get(requestParams, responseType, false);
    }

    // convertBase64ToBytes - if set to true, all base64 strings passed by the server will be converted to byte arrays. Added for card data responses from the MS.
    public <S extends BaseHttpResponse> S get(HashMap<String, String> requestParams, Class<S> responseType, boolean convertBase64ToBytes) {

        S response;
        try {
            // This extracts us a type safe BaseHttpResponse base class object
            response = getGenericResponse(responseType, null, 0, url, BaseHttpResponse.enumHTTPVerb.GET);
        } catch (Exception e) {
            return null;
        }

        // check for offline
        if (!isNetworkConnectionAvailable(context)) {
            response.setOffline(true);
            return response;
        }

        if (requestParams != null && requestParams.size() > 0) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }

            url = urlBuilder.build().toString();
        }

        Request request = buildRequest(null, response.getApiVersion(), BaseHttpResponse.enumHTTPVerb.GET);

        Timber.tag(TAG).d(String.format("HTTP GET request to %s", url));

        // call the server
        boolean timedOut = false;
        Exception httpException = null;
        try (Response httpResponse = client.newCall(request).execute()) {

            // Response notes:
            //      http:404 will result in an empty string

            String responseBody = "";    // can only be read ONCE.

            if (!isHttpSuccess(httpResponse.code()))
                return getGenericResponse(responseType, httpResponse.body().string(), httpResponse.code(), url, BaseHttpResponse.enumHTTPVerb.GET);

            try {
                // Convert to response object type
                responseBody = httpResponse.body().string();         // can only be accessed once

                if (responseType == BaseHttpResponse.class || httpResponse.code() == 204)                // no data response
                    return getGenericResponse(responseType, responseBody, httpResponse.code(), url, BaseHttpResponse.enumHTTPVerb.GET);

                // Serialise to JSON
                Gson gson = convertBase64ToBytes
                        ? new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter()).create()
                        : new Gson();

                response = gson.fromJson(responseBody, responseType);
                response.setHttpCode(httpResponse.code());
                response.setRawHttpResponse(responseBody);
                return response;

            } catch (Exception e) {
                return getGenericResponse(responseType, responseBody, httpResponse.code(), url, BaseHttpResponse.enumHTTPVerb.GET, e);
            }

        } catch (SocketTimeoutException e) {
            timedOut = true;
        } catch (Exception e) {
            e.printStackTrace();
            httpException = e;
        }

        try {
            S genericResponse = getGenericResponse(responseType, null, 0, url, BaseHttpResponse.enumHTTPVerb.GET, httpException);
            genericResponse.setTimedOut(timedOut);
            return genericResponse;

        } catch (Exception e2) {
            return null;
        }
    }


    public <T, S extends BaseHttpResponse> S post(@NonNull T requestObject, Class<S> responseType) {
        return sendRequest(requestObject, responseType, BaseHttpResponse.enumHTTPVerb.POST);
    }

    public <T, S extends BaseHttpResponse> S put(@NonNull T requestObject, Class<S> responseType) {
        return sendRequest(requestObject, responseType, BaseHttpResponse.enumHTTPVerb.PUT);
    }


    public <T, S extends BaseHttpResponse> S sendRequest(@NonNull T requestObject, Class<S> responseType, BaseHttpResponse.enumHTTPVerb httpVerb) {

        S response;

        try {
            // This extracts us a type safe BaseHttpResponse base class object
            response = getGenericResponse(responseType, null, 0, url, httpVerb);
        } catch (Exception e) {
            return null;
        }

        isPost = true;

        // check for offline
        if (!isNetworkConnectionAvailable(context)) {
            response.setOffline(true);
            return response;
        }

        // prepare JSON request
        Gson gson = new Gson();
        String json = "";
        if (requestObject != null) {
            json = gson.toJson(requestObject, requestObject.getClass());
        }

        RequestBody body = RequestBody.create(JSON, json);
        Request request = buildRequest(body, response.getApiVersion(), httpVerb);

        Timber.tag(TAG).d(String.format("HTTP %s request to %s", httpVerb.toString(), url));

        // call the server
        boolean timedOut = false;
        Exception httpException = null;
        try (Response httpResponse = client.newCall(request).execute()) {

            // Response notes:
            //      http:404 will result in an empty string

            if (!isHttpSuccess(httpResponse.code()))
                return getGenericResponse(responseType, httpResponse.body().string(), httpResponse.code(), url, httpVerb);

            try {
                // Convert to response object type
                String responseBody = httpResponse.body().string();         // can only be accessed once

                if (responseType == BaseHttpResponse.class || httpResponse.code() == 204)            // no schemes response expected
                    return getGenericResponse(responseType, responseBody, httpResponse.code(), url, httpVerb);     // return code and raw response

                response = gson.fromJson(responseBody, responseType);
                response.setHttpCode(httpResponse.code());
                response.setRawHttpResponse(responseBody);
                response.setUrl(url);
                response.setHttpMethod(httpVerb);
                return response;

            } catch (Exception e) {
                // most likely a JSON serialisation error
                return getGenericResponse(responseType, httpResponse.body().string(), httpResponse.code(), url, httpVerb, e);
            }

        } catch (SocketTimeoutException e) {
            timedOut = true;
        } catch (Exception e) {
            httpException = e;
        }

        try {
            S genericResponse = getGenericResponse(responseType, null, 0, url, httpVerb, httpException);
            genericResponse.setTimedOut(timedOut);
            return genericResponse;

        } catch (Exception e2) {
            return null;
        }


    }

    private Request buildRequest(RequestBody body, String apiVersion, BaseHttpResponse.enumHTTPVerb httpVerb) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json");

        switch (httpVerb) {
            case POST:
                requestBuilder.post(body);
                break;
            case PUT:
                requestBuilder.put(body);
                break;
            case DELETE:
                requestBuilder.delete(body);
                break;
        }

        if (!TextUtils.isEmpty(apiKeyWithPrefix)) {
            //String apiKeyHeader = String.format("%s %s", apiKeyName, apiKeyWithPrefix);
            Timber.tag(TAG).d(String.format("Auth header set as: {%s: %s}", constants.HTTP_HEADER_AUTHORISATION, apiKeyWithPrefix));
            requestBuilder.addHeader(constants.HTTP_HEADER_AUTHORISATION, apiKeyWithPrefix);
        } else
            Timber.tag(TAG).d("ANONYMOUS REQUEST - No Auth header has been defined for this server request.");


        if (!TextUtils.isEmpty(apiVersion))
            requestBuilder.addHeader(constants.HTTP_HEADER_API_VERSION, apiVersion);

        return requestBuilder.build();
    }


    @NonNull
    private <S extends BaseHttpResponse> S getGenericResponse(Class<S> responseType, String rawResponse, int httpCode, String url, BaseHttpResponse.enumHTTPVerb httpVerb) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        return getGenericResponse(responseType, rawResponse, httpCode, url, httpVerb, null);
    }

    @NonNull
    private <S extends BaseHttpResponse> S getGenericResponse(Class<S> responseType, String rawResponse, int httpCode, String url, BaseHttpResponse.enumHTTPVerb httpVerb, Exception e) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        S response;
        response = responseType.getDeclaredConstructor(String.class, int.class, String.class).newInstance(rawResponse, httpCode, url);    // this is how we create an instance of a generic type
        //response.setIsPost(isPost);
        response.setHttpMethod(httpVerb);
        response.setHttpException(e);   // most likely null
        return response;
    }

    private boolean isHttpSuccess(int code) {
        return code > 199 && code < 300;
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean isNetworkConnectionAvailable(Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
}
