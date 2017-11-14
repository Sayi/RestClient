package com.deepoove.restclient.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deepoove.restclient.R;
import com.deepoove.restclient.model.RemoteResponse;
import com.deepoove.restclient.model.RestTest;

import java.util.Map;

public class ResponseGeneralFragment extends Fragment {
    private static final String ARG_RESPONSE = "response";
    private static final String ARG_REST = "restTest";

    private RemoteResponse response;

    private RestTest restTest;

    private View detailView;
    private View headerDetailView;
    private TextView curlView;
    private TextView headerView;
    private ImageView indicator;
    private ImageView indicatorHeader;


    public ResponseGeneralFragment() {
    }

    public static ResponseGeneralFragment newInstance(RemoteResponse response, RestTest restTest) {
        ResponseGeneralFragment fragment = new ResponseGeneralFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, response);
        args.putSerializable(ARG_REST, restTest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (RemoteResponse) getArguments().getSerializable(ARG_RESPONSE);
            restTest = (RestTest) getArguments().getSerializable(ARG_REST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_response_general, container, false);

        TextView timeView = (TextView) inflate.findViewById(R.id.time);

        curlView = (TextView) inflate.findViewById(R.id.curl);
        detailView = inflate.findViewById(R.id.detail);
        indicator = (ImageView) inflate.findViewById(R.id.indicator);

        headerView = (TextView) inflate.findViewById(R.id.header);
        headerDetailView = inflate.findViewById(R.id.header_detail);
        indicatorHeader = (ImageView) inflate.findViewById(R.id.indicator_header);

        curlView.setText(convertToCURL(restTest));
        Map<String, String> headers = response.getHeaders();
        headerView.setText(prettyHeader(headers));

        timeView.setText(response.getInterval() + "ms");

        inflate.findViewById(R.id.generalLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(v);
            }
        });
        inflate.findViewById(R.id.headerLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeaderDetail(v);
            }
        });

        return inflate;

    }

    private String prettyHeader(Map<String, String> headers) {
        StringBuffer sb = new StringBuffer("");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n\n");
            }

        }
        if (sb.length() <= 0){
            sb.append("No Headers.");
        }
        return sb.toString();
    }

    private String convertToCURL(RestTest restTest) {
        StringBuffer sb = new StringBuffer();
        sb.append(restTest.getMethod().toUpperCase()).append(" ").append(restTest.getQueryUrl()).append("\n");
        return sb.toString();
    }


    public void showDetail(View view) {
        int visibility = detailView.getVisibility();
        if (View.GONE == visibility) {
            detailView.setVisibility(View.VISIBLE);
            indicator.setImageResource(R.drawable.ic_go_down);
        } else {
            detailView.setVisibility(View.GONE);
            indicator.setImageResource(R.drawable.ic_go_right);
        }
    }

    public void showHeaderDetail(View view) {
        int visibility = headerDetailView.getVisibility();
        if (View.GONE == visibility) {
            headerDetailView.setVisibility(View.VISIBLE);
            indicatorHeader.setImageResource(R.drawable.ic_go_down);
        } else {
            headerDetailView.setVisibility(View.GONE);
            indicatorHeader.setImageResource(R.drawable.ic_go_right);
        }
    }


}
