package com.deepoove.restclient.presenter;

import com.deepoove.restclient.model.RestTest;

/**
 * @author Sayi
 */

public interface AddRestPresenter {

    void saveRest(RestTest restTest);

    void sendRequest(int method, RestTest restTest);

    void abortRequest();
}
