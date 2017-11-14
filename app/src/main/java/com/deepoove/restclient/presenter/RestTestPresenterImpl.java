package com.deepoove.restclient.presenter;

import com.deepoove.restclient.RestClientApplication;
import com.deepoove.restclient.conf.Settings;
import com.deepoove.restclient.interactor.RestTestInteractor;
import com.deepoove.restclient.interactor.RestTestInteractorImpl;
import com.deepoove.restclient.model.RestTest;
import com.deepoove.restclient.util.StringUtils;
import com.deepoove.restclient.view.OnRestFragmentView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sayi
 */

public class RestTestPresenterImpl implements RestTestPresenter {

    private OnRestFragmentView onDocFragmentView;
    private RestTestInteractor restTestInteractor;

    List<RestTest> datas = new ArrayList<>();


    public RestTestPresenterImpl(OnRestFragmentView view) {
        this.onDocFragmentView = view;
        this.restTestInteractor = new RestTestInteractorImpl();

    }

    @Override
    public void onCreate() {
        onDocFragmentView.setItems(datas = restTestInteractor.getTests());
    }


    @Override
    public void removeItem(int position) {
        datas.remove(position);
        Settings settings = RestClientApplication.getSettings();
        settings.put("conf_rest_list", datas);
        onDocFragmentView.notifyItemRemoved(position);
    }


    @Override
    public void upSortItem(int position) {
        int newPosition = position - 1 >= 0 ? position - 1 : -1;
        if (newPosition != -1) {
            RestTest up = datas.get(position);
            RestTest old = datas.get(newPosition);
            datas.set(newPosition, up);
            datas.set(position, old);
            Settings settings = RestClientApplication.getSettings();
            settings.put("conf_rest_list", datas);
            onDocFragmentView.notifyItemRangeChanged(newPosition, 2);
        }

    }

    @Override
    public void downSortItem(int position) {

        int newPosition = position + 1 <= datas.size() - 1 ? position + 1 : -1;
        if (newPosition != -1) {
            RestTest down = datas.get(position);
            RestTest old = datas.get(newPosition);
            datas.set(newPosition, down);
            datas.set(position, old);
            Settings settings = RestClientApplication.getSettings();
            settings.put("conf_rest_list", datas);
            onDocFragmentView.notifyItemRangeChanged(position, 2);
        }

    }

    @Override
    public void refresh() {
        datas.clear();
        datas.addAll(restTestInteractor.getTests());
    }

    @Override
    public void clickItem(int position) {
        onDocFragmentView.navigateToTest(datas.get(position));
    }

    @Override
    public void copyItem(int position) {
        RestTest restTest = datas.get(position);

        Gson gson = new Gson();
        RestTest copyRest = gson.fromJson(gson.toJson(restTest), RestTest.class);
        copyRest.setId(StringUtils.getRandomString(10));
        restTestInteractor.saveRestTest(copyRest);
        datas.add(0, copyRest);

    }
}
