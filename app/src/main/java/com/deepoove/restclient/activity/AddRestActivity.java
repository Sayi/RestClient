package com.deepoove.restclient.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.deepoove.restclient.R;
import com.deepoove.restclient.adapter.KeyValueAdapter;
import com.deepoove.restclient.listener.TextWatcherListener;
import com.deepoove.restclient.model.BasicAuth;
import com.deepoove.restclient.model.RemoteResponse;
import com.deepoove.restclient.model.RestTest;
import com.deepoove.restclient.presenter.AddRestPresenter;
import com.deepoove.restclient.presenter.AddRestPresenterImpl;
import com.deepoove.restclient.ui.RadioBoxView;
import com.deepoove.restclient.util.ItemClickSupport;
import com.deepoove.restclient.util.StringUtils;
import com.deepoove.restclient.view.OnAddRestView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AddRestActivity extends BaseActivity implements OnAddRestView {

    private final List<String> METHOD = Arrays.asList("GET", "POST", "PUT", "DELETE");
    private final List<Integer> METHOD_INT = Arrays.asList(Request.Method.GET, Request.Method.POST, Request.Method.PUT, Request.Method.DELETE);
    private final List<String> BODY_TYPE = Arrays.asList("Form", "Text");

    private AlertDialog dialog;
    private TextView titleView;
    private View okView;
    protected Dialog progressDialog;
    private TextView abortView;

    private RecyclerView mBodyFormRecyclerView;
    private KeyValueAdapter mQueryAdapter;
    private KeyValueAdapter mHeaderAdapter;
    private KeyValueAdapter mBodyAdapter;

    private List<RestTest.KeyValue> queryParameters = new ArrayList<>();
    private List<RestTest.KeyValue> headers = new ArrayList<>();
    private List<RestTest.KeyValue> bodyForms = new ArrayList<>();


    private EditText urlView;
    private RadioBoxView requestMethodBox;
    private RadioBoxView bodyType;
    private EditText body;
    private EditText keyView;
    private EditText valueView;
    private ImageView authImageView;

    private RestTest restTest;
    private Handler handler;
    private Timer timer;
    private AddRestPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rest);

        renderToolBar("");

        progressDialog = createProgressDialog("");

        restTest = (RestTest) getIntent().getSerializableExtra("restTest");
        if (restTest == null) {
            restTest = new RestTest();
            restTest.setId(StringUtils.getRandomString(10));
        }
        queryParameters.addAll(restTest.getQueryParameters());
        headers.addAll(restTest.getHeaders());
        bodyForms.addAll(restTest.getFormBodys());

        authImageView = (ImageView) findViewById(R.id.auth);
        mBodyFormRecyclerView = (RecyclerView) findViewById(R.id.formlist);
        urlView = (EditText) findViewById(R.id.url);
        requestMethodBox = (RadioBoxView) findViewById(R.id.requestMethod);
        bodyType = (RadioBoxView) findViewById(R.id.bodyType);
        body = (EditText) findViewById(R.id.content);
        final View requestBodyView = findViewById(R.id.requestBody);
        final View bodyTypeJsonView = findViewById(R.id.bodyTypeJson);
        final View bodyTypeFormView = findViewById(R.id.bodyTypeForm);
        requestMethodBox.setOnSelectListener(new RadioBoxView.OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                switch (text) {
                    case "POST":
                    case "PUT":
                        requestBodyView.setVisibility(View.VISIBLE);
                        break;
                    case "GET":
                    case "DELETE":
                        requestBodyView.setVisibility(View.GONE);
                        break;
                }
            }
        });
        bodyType.setOnSelectListener(new RadioBoxView.OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                switch (text) {
                    case "Form":
                        mBodyFormRecyclerView.setVisibility(View.VISIBLE);
                        bodyTypeJsonView.setVisibility(View.GONE);
                        bodyTypeFormView.setVisibility(View.VISIBLE);
                        break;
                    case "Text":
                        mBodyFormRecyclerView.setVisibility(View.GONE);
                        bodyTypeJsonView.setVisibility(View.VISIBLE);
                        bodyTypeFormView.setVisibility(View.GONE);
                        break;
                }
            }
        });

        urlView.setText(restTest.getUrl());
        urlView.setSelection(restTest.getUrl().length());
        if (null != restTest.getBody()) {
            body.setText(restTest.getBody());
        }
        requestMethodBox.setSelectPosition(METHOD.indexOf(restTest.getMethod()), restTest.getMethod());
        bodyType.setSelectPosition(BODY_TYPE.indexOf(restTest.getBodyType()), restTest.getBodyType());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_edit, null);
        builder.setView(layout);
        dialog = builder.create();

        titleView = (TextView) layout.findViewById(R.id.dialog_title);
        keyView = (EditText) layout.findViewById(R.id.key);
        valueView = (EditText) layout.findViewById(R.id.value);
        okView = layout.findViewById(R.id.dialog_ok);

        TextWatcher watcher = new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable s) {
                okView.setAlpha(StringUtils.isBlank(keyView.getText().toString()) ? 0.4f : 1.0f);
            }
        };
        keyView.addTextChangedListener(watcher);

        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = (String) titleView.getTag();
                String key = keyView.getText().toString();
                if (StringUtils.isBlank(key)) {
                    return;
                }
                RestTest.KeyValue keyValue = new RestTest.KeyValue();
                keyValue.setKey(key);
                keyValue.setValue(valueView.getText().toString());
                switch (titleView.getText().toString()) {
                    case "Query Parameter":
                        if ("".equals(tag)) {
                            queryParameters.add(keyValue);
                        } else {
                            queryParameters.set(Integer.parseInt(tag), keyValue);
                        }
                        mQueryAdapter.notifyDataSetChanged();
                        break;
                    case "Header":
                        if ("".equals(tag)) {
                            headers.add(keyValue);
                        } else {
                            headers.set(Integer.parseInt(tag), keyValue);
                        }
                        mHeaderAdapter.notifyDataSetChanged();
                        break;
                    case "From Parameter":
                        if ("".equals(tag)) {
                            bodyForms.add(keyValue);
                        } else {
                            bodyForms.set(Integer.parseInt(tag), keyValue);
                        }
                        mBodyAdapter.notifyDataSetChanged();
                        break;
                }

                dialog.dismiss();
            }
        });


        RecyclerView queryRecyclerView = (RecyclerView) findViewById(R.id.queryParameterlist);
        queryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        queryRecyclerView.setAdapter(mQueryAdapter = new KeyValueAdapter(queryParameters));

        RecyclerView headerRecyclerView = (RecyclerView) findViewById(R.id.headerlist);
        headerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        headerRecyclerView.setAdapter(mHeaderAdapter = new KeyValueAdapter(headers));


        mBodyFormRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBodyFormRecyclerView.setAdapter(mBodyAdapter = new KeyValueAdapter(bodyForms));

        ItemClickSupport.addTo(queryRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        updateMeta(queryParameters.get(position), "query", position);
                    }
                }
        );
        ItemClickSupport.addTo(headerRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        updateMeta(headers.get(position), "header", position);
                    }
                }
        );
        ItemClickSupport.addTo(mBodyFormRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        updateMeta(bodyForms.get(position), "form", position);
                    }
                }
        );


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                String text = data.getString("text");
                abortView.setText(text);
            }
        };

        if (restTest.getBasicAuth().isValid()) {
            authImageView.setImageResource(R.drawable.ic_menu_auth_black);
        }


        presenter = new AddRestPresenterImpl(this);


    }

    private Dialog createProgressDialog(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sending);
        abortView = (TextView) dialog.findViewById(R.id.abort);
        abortView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != presenter) presenter.abortRequest();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_rest, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(300);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(300);
            finish();
            return true;
        }

        String url = urlView.getText().toString();
        if (StringUtils.isBlank(url)) {
            showToast("Invalid URI.");
            return true;
        } else {
            restTest.setUrl(url);
            restTest.setMethod(METHOD.get(requestMethodBox.getmSelectPosition()));
            restTest.setQueryParameters(queryParameters);
            restTest.setHeaders(headers);
            if ("PUT".equals(restTest.getMethod()) || "POST".equals(restTest.getMethod())) {
                restTest.setBodyType(BODY_TYPE.get(bodyType.getmSelectPosition()));
                if ("Text".equals(restTest.getBodyType())) {
                    restTest.setBody(body.getText().toString());
                    restTest.setFormBodys(new ArrayList<RestTest.KeyValue>());
                } else {
                    restTest.setBody("");
                    restTest.setFormBodys(bodyForms);
                }
            }

            if (id == R.id.action_save_rest) {
                presenter.saveRest(restTest);
            } else if (id == R.id.action_send) {
                presenter.sendRequest(METHOD_INT.get(requestMethodBox.getmSelectPosition()), restTest);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dismissTimerDialog();
        if (requestCode == 300 && resultCode == 300) {
            BasicAuth auth = (BasicAuth) data.getSerializableExtra("auth");
            restTest.getBasicAuth().setUsername(auth.getUsername());
            restTest.getBasicAuth().setPassword(auth.getPassword());
            if (restTest.getBasicAuth().isValid()) {
                authImageView.setImageResource(R.drawable.ic_menu_auth_black);
            } else {
                authImageView.setImageResource(R.drawable.ic_menu_auth_black_invalid);
            }
        }
    }


    public void addMeta(View view) {
        String tag = (String) view.getTag();
        keyView.requestFocus();
        keyView.setText("");
        valueView.setText("");
        switch (tag) {
            case "query":
                titleView.setText("Query Parameter");
                break;
            case "header":
                titleView.setText("Header");
                break;
            case "form":
                titleView.setText("From Parameter");
                break;

            default:
                break;
        }
        titleView.setTag("");
        dialog.show();
    }

    public void updateMeta(RestTest.KeyValue keyValue, String tag, int position) {
        titleView.requestFocus();
        keyView.setText(keyValue.getKey());
        valueView.setText(StringUtils.isBlank(keyValue.getValue()) ? "" : keyValue.getValue());
        switch (tag) {
            case "query":
                titleView.setText("Query Parameter");
                break;
            case "header":
                titleView.setText("Header");
                break;
            case "form":
                titleView.setText("From Parameter");
                break;

            default:
                break;
        }
        titleView.setTag(position + "");
        dialog.show();
    }

    public void navigateToAuth(View view) {
        Intent intent = new Intent(this, BasicAuthActivity.class);
        intent.putExtra("auth", restTest.getBasicAuth());
        startActivityForResult(intent, 300);
    }

    @Override
    public void showToast(String msg) {
        Toast makeText = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        makeText.setDuration(Toast.LENGTH_SHORT);
        makeText.show();
    }

    @Override
    public void showTimerDialog() {

        //sending request
        abortView.setText("Abort");
        progressDialog.show();


        //start timer to update send time
        TimerTask task = new TimerTask() {
            int i = 1;

            @Override
            public void run() {
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("text", "Abort(" + (i++) + "s)");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };

        timer = new Timer();
        timer.schedule(task, 1000, 1000);

    }

    @Override
    public void cancelTimer() {
        if (null != timer) timer.cancel();
    }

    @Override
    public void navigateToResponse(RemoteResponse response, RestTest restTest) {
        Intent intent = new Intent(AddRestActivity.this, ResponseActivity.class);
        intent.putExtra("response", response);
        intent.putExtra("restTest", restTest);
        startActivityForResult(intent, 200);
    }

    @Override
    public void dismissTimerDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
