package com.deepoove.restclient.interactor;

import com.deepoove.restclient.model.RestTest;

import java.util.List;

/**
 * @author Sayi
 */

public interface RestTestInteractor {

    void saveRestTest(RestTest test);

    List<RestTest> getTests();

}
