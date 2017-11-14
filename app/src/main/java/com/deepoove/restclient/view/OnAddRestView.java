package com.deepoove.restclient.view;

import com.deepoove.restclient.model.RemoteResponse;
import com.deepoove.restclient.model.RestTest;

/**
 * @author Sayi
 */

public interface OnAddRestView {

    void showToast(String msg);

    void showTimerDialog();

    void cancelTimer();

    void navigateToResponse(RemoteResponse response, RestTest restTest);

    void dismissTimerDialog();
}
