package com.deepoove.restclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.deepoove.restclient.R;
import com.deepoove.restclient.activity.AddRestActivity;
import com.deepoove.restclient.adapter.RestTestAdapter;
import com.deepoove.restclient.model.RestTest;
import com.deepoove.restclient.presenter.RestTestPresenter;
import com.deepoove.restclient.presenter.RestTestPresenterImpl;
import com.deepoove.restclient.util.ItemClickSupport;
import com.deepoove.restclient.view.OnRestFragmentView;

import java.util.List;


public class RestTestFragment extends Fragment implements OnRestFragmentView {

    private int position;

    RecyclerView mRecyclerView;
    RestTestAdapter mAdapter;

    private RestTestPresenter presenter;

    public RestTestFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RestTestPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rest_test, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.restlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        presenter.clickItem(position);
                    }
                }
        );
        ItemClickSupport.addTo(mRecyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                RestTestFragment.this.position = position;
                return false;
            }
        });

        presenter.onCreate();
        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = this.position;
        int id = item.getItemId();
        switch (id) {
            case R.id.rest_delete:
                presenter.removeItem(position);
                return true;
            case R.id.rest_copy_request:
                presenter.copyItem(position);
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.rest_sort_up:
                presenter.upSortItem(position);
                return true;
            case R.id.rest_sort_down:
                presenter.downSortItem(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    @Override
    public void navigateToTest(RestTest restTest) {
        Intent intent = new Intent(getActivity(), AddRestActivity.class);
        intent.putExtra("restTest", restTest);
        startActivityForResult(intent, 300, null);
    }

    @Override
    public void setItems(List<RestTest> list) {
        mRecyclerView.setAdapter(mAdapter = new RestTestAdapter(list));
    }

    @Override
    public void notifyItemRangeChanged(int start, int length) {
        mAdapter.notifyItemRangeChanged(start, length);
    }

    @Override
    public void notifyItemRemoved(int position) {
        mAdapter.notifyItemRemoved(position);
    }

    @Override
    public void refresh() {
        presenter.refresh();
        mAdapter.notifyDataSetChanged();
    }
}
