package com.deepoove.restclient.interactor;

import android.util.Log;

import com.deepoove.restclient.RestClientApplication;
import com.deepoove.restclient.conf.Settings;
import com.deepoove.restclient.model.RestTest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sayi
 */

public class RestTestInteractorImpl implements RestTestInteractor {

    String key = "conf_rest_list";

    public RestTestInteractorImpl() {
    }


    @Override
    public void saveRestTest(RestTest test) {
        Settings settings = RestClientApplication.getSettings();
        String value = settings.get(key);
        List<RestTest> result = new ArrayList<>();
        int pos = -1;
        if (null != value) {
            Gson gson = new Gson();
            result = gson.fromJson(value, new TypeToken<List<RestTest>>() {
            }.getType());
            int size = result.size();
            for (int i = 0; i < size; i++) {
                RestTest temp = result.get(i);
                if (temp.getId().equals(test.getId())) {
                    pos = i;
                    break;
                }
            }

        }
        if (pos != -1) {
            result.set(pos, test);
        } else {
            result.add(0, test);
        }
        settings.put(key, result);
    }

    @Override
    public List<RestTest> getTests() {
        List<RestTest> result = null;
        Settings settings = RestClientApplication.getSettings();
        String value = settings.get(key);
        if (null != value) {
            Gson gson = new Gson();
            result = gson.fromJson(value, new TypeToken<List<RestTest>>() {
            }.getType());
        } else {
            result = new ArrayList<RestTest>();
            RestTest doc = new RestTest();
            doc.setId("init_test_0");
            doc.setUrl("http://petstore.swagger.io/v2/swagger.json");
            result.add(doc);
            settings.put(key, result);
        }
        return result;
    }


}
