package uk.co.referencepoint.publishtest.http;

/**
 * Created by nick.wright on 03/10/2017.
 */

public enum enumHttpResponseStatus {
    OFFLINE,
    SUCCESS,
    FAILED,         // http 0
    UNAUTHORISED,
    SERVER_ERROR,
    BAD_REQUEST,    // client errors - 400 (bad request - params wrong), 404 (not found) , 429 (throttling) etc
    FORBIDDEN,
    TIMEOUT
}
