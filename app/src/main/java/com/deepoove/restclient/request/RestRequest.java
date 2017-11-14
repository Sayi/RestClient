package com.deepoove.restclient.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.deepoove.restclient.model.HttpStatusNoteMap;
import com.deepoove.restclient.model.RemoteResponse;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sayi
 */

public class RestRequest extends Request<RemoteResponse> {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

    private Response.Listener<RemoteResponse> mListener;

    public RestRequest(int method, String url, final Response.Listener<RemoteResponse> listener) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                RemoteResponse remoteResponse = new RemoteResponse();
                if (error instanceof NoConnectionError) {
                    remoteResponse.setStatusCode(-1);
                    remoteResponse.setResponseMessage("No Connect");
                } else if (error instanceof TimeoutError) {
                    remoteResponse.setStatusCode(-1);
                    remoteResponse.setResponseMessage("Timeout");
                } else {
                    NetworkResponse response = error.networkResponse;
                    if (null != response) {
                        try {
                            remoteResponse.setStatusCode(response.statusCode);
                            remoteResponse.setResponseMessage(HttpStatusNoteMap.getNote(response.statusCode));
                            remoteResponse.setInterval(response.networkTimeMs);
                            remoteResponse.setHeaders(response.headers);
                            String str = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            remoteResponse.setResponse(str);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            remoteResponse.setResponse(e.getMessage());
                        }
                    } else {
                        remoteResponse.setStatusCode(-1);
                        Throwable cause = error.getCause();
                        if (null != cause){
                            remoteResponse.setResponseMessage(cause.getMessage());
                        }else {
                            remoteResponse.setResponseMessage(error.getMessage());
                        }
                    }
                }

                listener.onResponse(remoteResponse);
            }
        });
        mListener = listener;
    }

    @Override
    protected Response<RemoteResponse> parseNetworkResponse(NetworkResponse response) {
        RemoteResponse remoteResponse = new RemoteResponse();
        if (null != response) {
            try {
                remoteResponse.setStatusCode(response.statusCode);
                remoteResponse.setResponseMessage(HttpStatusNoteMap.getNote(response.statusCode));
                remoteResponse.setInterval(response.networkTimeMs);
                remoteResponse.setHeaders(response.headers);
                String str = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                remoteResponse.setResponse(str);

            } catch (UnsupportedEncodingException e) {
                remoteResponse.setResponse(e.getMessage());
            }
        } else {
            remoteResponse.setStatusCode(-1);
            remoteResponse.setResponseMessage("Error");
        }
        return Response.success(remoteResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(RemoteResponse response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        return headers;
    }


}
