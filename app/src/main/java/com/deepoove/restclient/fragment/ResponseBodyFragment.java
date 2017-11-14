package com.deepoove.restclient.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.deepoove.restclient.R;
import com.deepoove.restclient.conf.Template;
import com.deepoove.restclient.model.RemoteResponse;
import com.deepoove.restclient.model.RestTest;
import com.deepoove.restclient.ui.RadioBoxView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class ResponseBodyFragment extends Fragment {
    private static final String ARG_RESPONSE = "response";
    private static final String ARG_REST = "restTest";

    private RemoteResponse response;

    private RadioBoxView previewTypeBox;
    private WebView mWebView;
    private TextView rawView;

    private View codeView;
    private View jsonTextView;


    public ResponseBodyFragment() {
    }

    public static ResponseBodyFragment newInstance(RemoteResponse response, RestTest restTest) {
        ResponseBodyFragment fragment = new ResponseBodyFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_response_body, container, false);


        rawView = (TextView) inflate.findViewById(R.id.response);
        jsonTextView = inflate.findViewById(R.id.jsonTextView);
        codeView = inflate.findViewById(R.id.codeView);
        mWebView = (WebView) inflate.findViewById(R.id.webview);
        buildWebView();

        previewTypeBox = (RadioBoxView) inflate.findViewById(R.id.previewType);
        previewTypeBox.setOnSelectListener(new RadioBoxView.OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                switch (text) {
                    case "Pretty":
                        jsonTextView.setVisibility(View.GONE);
                        codeView.setVisibility(View.VISIBLE);
                        break;
                    case "Raw":
                        jsonTextView.setVisibility(View.VISIBLE);
                        codeView.setVisibility(View.GONE);
                        break;
                }
            }
        });
        previewTypeBox.setSelectPosition(0, "Pretty");

        Map<String, String> headers = response.getHeaders();

        if (!"-1".equals(response.getStatusCode()) && null != response.getResponse()) {
            String text = response.getResponse();
            rawView.setText(response.getResponse());
            if (null != headers) {
                String contentType = headers.get("Content-type");
                if (null != text && null != contentType) {
                    if (-1 != contentType.indexOf("application/json")) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                        JsonParser jp = new JsonParser();
                        JsonElement je = jp.parse(text);
                        text = gson.toJson(je);
                    } else if (-1 != contentType.indexOf("application/xml")) {
                        SAXBuilder jdomBuilder = new SAXBuilder();
                        try {
                            Document jdomDoc = jdomBuilder.build(new StringReader(text));
                            XMLOutputter xml = new XMLOutputter();
                            xml.setFormat(Format.getPrettyFormat());
                            text = xml.outputString(jdomDoc);
                            text = StringEscapeUtils.escapeXml(text);
                        } catch (JDOMException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (-1 != contentType.indexOf("html")) {
                        text = StringEscapeUtils.escapeHtml4(text);
                    }
                }
            }
            String data = Template.getInstance().getCodeHtml();
            data = data.replace("{{code}}", text);
            mWebView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
        }


        return inflate;

    }

    private void buildWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);


        mWebView.setLongClickable(false);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100 && (View.INVISIBLE == mWebView.getVisibility()
                        || View.GONE == mWebView.getVisibility())) {
                    mWebView.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
