package com.deepoove.restclient.view;

import com.deepoove.restclient.model.RestTest;

import java.util.List;

/**
 * @author Sayi
 */

public interface OnRestFragmentView {

    void navigateToTest(RestTest restTest);

    void setItems(List<RestTest> list);

    void notifyItemRangeChanged(int start, int length);

    void notifyItemRemoved(int position);

    void refresh();
}
