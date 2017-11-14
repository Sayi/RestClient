package com.deepoove.restclient.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sayi
 */

public class HttpStatusNoteMap {

    private static Map<Integer, String> map = new HashMap<Integer, String>();


    static {
        map.put(HttpStatus.SC_CONTINUE, "Continue");
        map.put(HttpStatus.SC_SWITCHING_PROTOCOLS, "Switching Protocols");
        map.put(HttpStatus.SC_PROCESSING, "Processing");


        map.put(HttpStatus.SC_OK, "OK");
        map.put(HttpStatus.SC_CREATED, "Created");
        map.put(HttpStatus.SC_ACCEPTED, "Accepted");
        map.put(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, "Non Authoritative Information");
        map.put(HttpStatus.SC_NO_CONTENT, "No Content");
        map.put(HttpStatus.SC_RESET_CONTENT, "Reset Content");
        map.put(HttpStatus.SC_PARTIAL_CONTENT, "Partial Content");
        map.put(HttpStatus.SC_MULTI_STATUS, "Multi-Status");


        map.put(HttpStatus.SC_MULTIPLE_CHOICES, "Mutliple Choices");
        map.put(HttpStatus.SC_MOVED_PERMANENTLY, "Moved Permanently");
        map.put(HttpStatus.SC_MOVED_TEMPORARILY, "Moved Temporarily");
        map.put(HttpStatus.SC_SEE_OTHER, "See Other");
        map.put(HttpStatus.SC_NOT_MODIFIED, "Not Modified");
        map.put(HttpStatus.SC_USE_PROXY, "Use Proxy");
        map.put(HttpStatus.SC_TEMPORARY_REDIRECT, "Temporary Redirect");


        map.put(HttpStatus.SC_BAD_REQUEST, "Bad Request");
        map.put(HttpStatus.SC_UNAUTHORIZED, "Unauthorized");
        map.put(HttpStatus.SC_PAYMENT_REQUIRED, "Payment Required");
        map.put(HttpStatus.SC_FORBIDDEN, "Forbidden");
        map.put(HttpStatus.SC_NOT_FOUND, "Not Found");
        map.put(HttpStatus.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        map.put(HttpStatus.SC_NOT_ACCEPTABLE, "Not Acceptable");
        map.put(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
        map.put(HttpStatus.SC_REQUEST_TIMEOUT, "Request Timeout");
        map.put(HttpStatus.SC_GONE, "Gone");
        map.put(HttpStatus.SC_LENGTH_REQUIRED, "Length Required");
        map.put(HttpStatus.SC_PRECONDITION_FAILED, "Precondition Failed");
        map.put(HttpStatus.SC_REQUEST_TOO_LONG, "Request Too Long");
        map.put(HttpStatus.SC_REQUEST_URI_TOO_LONG, "Request Uri Too Long");
        map.put(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
        map.put(HttpStatus.SC_EXPECTATION_FAILED, "Expectation Failed");
        map.put(HttpStatus.SC_METHOD_FAILURE, "Method Failure");
        map.put(HttpStatus.SC_UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        map.put(HttpStatus.SC_FAILED_DEPENDENCY, "Failed Dependency");


        map.put(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        map.put(HttpStatus.SC_NOT_IMPLEMENTED, "Not Implemented");
        map.put(HttpStatus.SC_BAD_GATEWAY, "Bad Gateway");
        map.put(HttpStatus.SC_SERVICE_UNAVAILABLE, "Service Unavailable");
        map.put(HttpStatus.SC_GATEWAY_TIMEOUT, "Gateway Timeout");
        map.put(HttpStatus.SC_INSUFFICIENT_STORAGE, "Insufficient Storage");
    }

    public static String getNote(int code){
        try {
            return map.get(code);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }


}

