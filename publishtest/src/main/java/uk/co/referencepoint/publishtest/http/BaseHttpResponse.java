package uk.co.referencepoint.publishtest.http;


import java.util.Locale;


public class BaseHttpResponse {

    private int httpCode;
    private String url;
    private String rawHttpResponse;
    private boolean isOffline;
    private enumHTTPVerb httpMethod;
    private boolean isTimedOut;
    private String apiVersion = constants.HTTP_HEADER_API_VERSION;  // change as required for each api call

    protected enum enumHTTPVerb { GET, POST, PUT, DELETE}

    public String getApiVersion() {
        return apiVersion;
    }

    public void setHttpMethod(enumHTTPVerb httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Exception getHttpException() {
        return httpException;
    }

    public void setHttpException(Exception httpException) {
        this.httpException = httpException;
    }

    private Exception httpException; // populated if the http request throws a try...catch exception

    public BaseHttpResponse(String rawHttpResponse, int httpCode, String url) {
        this.httpCode = httpCode;
        this.url = url;
        this.rawHttpResponse = rawHttpResponse;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRawHttpResponse() {
        return rawHttpResponse;
    }

    public void setRawHttpResponse(String rawHttpResponse) {
        this.rawHttpResponse = rawHttpResponse;
    }

    // offline and timed out are not read directly - mapped below in getHttpResponseStatus
    public void setOffline(boolean offline) {
        isOffline = offline;
    }
    public void setTimedOut(boolean timedOut) {
        isTimedOut = timedOut;
    }

//    public void setIsPost(boolean post) {
//        isPost = post;
//    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public enumHttpResponseStatus getHttpResponseStatus() {

        if(isTimedOut) return enumHttpResponseStatus.TIMEOUT;
        if(isOffline) return enumHttpResponseStatus.OFFLINE;
        if(httpCode == 0) return enumHttpResponseStatus.FAILED;

        if(httpCode >= 200 && httpCode <= 299)
            return enumHttpResponseStatus.SUCCESS;
        else if(httpCode == 401)
            return enumHttpResponseStatus.UNAUTHORISED;
        else if(httpCode == 403)
            return enumHttpResponseStatus.FORBIDDEN;
        else if(httpCode >= 400 && httpCode <= 499)
            return enumHttpResponseStatus.BAD_REQUEST;      // catch all
        if(httpCode >= 500 && httpCode <= 599)
            return enumHttpResponseStatus.SERVER_ERROR;
        else
            return enumHttpResponseStatus.FAILED;
    }

    public boolean isHTTPSuccess() { return getHttpResponseStatus() == enumHttpResponseStatus.SUCCESS;}


    public String getHTTPResponseMessage(boolean detailed){
        return detailed ? getHTTPResponseDetailedMessage() : getHTTPResponseShortMessage();
    }

    // Provides a short friendly message which clients can display to their users
    public String getHTTPResponseShortMessage(){

        enumHttpResponseStatus status = getHttpResponseStatus();
        if(isTimedOut || status == enumHttpResponseStatus.TIMEOUT)
            return "The call timed out";
        if(isOffline  || status == enumHttpResponseStatus.OFFLINE)
            return "Device is offline.";
        switch (status){
            case SUCCESS:
                return "Call to the server was successful.";
            case FORBIDDEN:
                return "Client does not have authorisation to perform this task.";
            case UNAUTHORISED:
                return "Unable to authenticate client (unknown).";
            case SERVER_ERROR:
                return "A server error occurred processing this request.";
            case BAD_REQUEST:
                return "A server request was not valid and was rejected by the server.";
            case FAILED:
                return "An http call failed.";
            default:
                return "An unhandled response status was returned when trying to talk to the server.";

        }
    }

    // Use this to supply the client with detailed info about the request - reserved for clients who request extra debug info
    public String getHTTPResponseDetailedMessage(){

        String protocol = httpMethod.toString(); //isPost ? "POST" : "GET";
        if(isTimedOut)
            return String.format(Locale.getDefault(), "Http %s request to [%s] timed out with response code [http:%d]. Raw response was [%s].", protocol, url, httpCode, rawHttpResponse);
        if(isOffline)
            return String.format(Locale.getDefault(), "Unable to make an http %s request to [%s] because device is offline.", protocol, url);

        if(httpException != null)
            return String.format(Locale.getDefault(), "Http %s response code from [%s] was [http:%d]. Http exception: [%s]. Raw response: [%s].", protocol, url, httpCode, httpException.getMessage(), rawHttpResponse);
        else
            return String.format(Locale.getDefault(), "Http %s response code from [%s] was [http:%d]. Raw response: [%s].", protocol, url, httpCode, rawHttpResponse);

    }
}
