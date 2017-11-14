package com.deepoove.restclient.presenter;

/**
 * @author Sayi
 */

public interface RestTestPresenter {

    void onCreate();

    void removeItem(int position);
    void upSortItem(int position);
    void downSortItem(int position);


    void refresh();

    void clickItem(int position);

    void copyItem(int position);
}
