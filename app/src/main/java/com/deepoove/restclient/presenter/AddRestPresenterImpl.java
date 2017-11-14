package com.deepoove.restclient.presenter;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.deepoove.restclient.interactor.RestTestInteractor;
import com.deepoove.restclient.interactor.RestTestInteractorImpl;
import com.deepoove.restclient.model.BasicAuth;
import com.deepoove.restclient.model.RemoteResponse;
import com.deepoove.restclient.model.RestTest;
import com.deepoove.restclient.request.RestRequest;
import com.deepoove.restclient.request.VolleySingleton;
import com.deepoove.restclient.view.OnAddRestView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sayi
 */

public class AddRestPresenterImpl implements AddRestPresenter {


    private OnAddRestView view;
    private RestRequest request;
    private RestTestInteractor restTestInteractor;

    public AddRestPresenterImpl(OnAddRestView view) {
        this.view = view;
        restTestInteractor = new RestTestInteractorImpl();
    }

    @Override
    public void saveRest(RestTest restTest) {
        restTestInteractor.saveRestTest(restTest);
        view.showToast("Saved.");
    }

    @Override
    public void sendRequest(int method, final RestTest restTest) {
        view.showTimerDialog();
        request = new RestRequest(method, restTest.getQueryUrl(), new Response.Listener<RemoteResponse>() {
            @Override
            public void onResponse(RemoteResponse response) {
                Log.d("Sayi", request.getOriginUrl() + "");
                view.navigateToResponse(response, restTest);
                view.cancelTimer();
                //progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                List<RestTest.KeyValue> keyValues = restTest.getHeaders();
                BasicAuth basicAuth = restTest.getBasicAuth();
                if (keyValues != null) {
                    for (RestTest.KeyValue keyValue : keyValues) {
                        headers.put(keyValue.getKey(), keyValue.getValue());
                    }
                }
                if (basicAuth.isValid()) {
                    headers.put("Authorization", authorization(basicAuth.getUsername(), basicAuth.getPassword()));
                }

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if ("Form".equals(restTest.getBodyType())) {
                    List<RestTest.KeyValue> keyValues = restTest.getFormBodys();
                    if (keyValues != null) {
                        for (RestTest.KeyValue keyValue : keyValues) {
                            params.put(keyValue.getKey(), keyValue.getValue());
                        }
                    }
                }
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if ("Text".equals(restTest.getBodyType()) && null != restTest.getBody()) {
                    return restTest.getBody().getBytes();
                } else {
                    return super.getBody();
                }
            }


        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 0f));
        request.setShouldCache(false);
        VolleySingleton.getInstance().addToRequestQueue(request);
    }

    @Override
    public void abortRequest() {
        view.cancelTimer();
        if (null != request) request.cancel();
        view.dismissTimerDialog();
    }

    public static String authorization(String username, String password) {
        String creds = String.format("%s:%s", username, password);
        return "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
    }
}
