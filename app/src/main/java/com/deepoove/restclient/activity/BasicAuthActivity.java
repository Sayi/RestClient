package com.deepoove.restclient.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.deepoove.restclient.R;
import com.deepoove.restclient.model.BasicAuth;

public class BasicAuthActivity extends BaseActivity {
    EditText userNameView;
    EditText passWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_auth);

        renderToolBar(R.string.title_auth);

        Intent intent = getIntent();
        BasicAuth auth = (BasicAuth) intent.getSerializableExtra("auth");

        userNameView = (EditText) findViewById(R.id.username);
        passWordView = (EditText) findViewById(R.id.password);

        if (auth.getUsername() != null && auth.getPassword() != null) {
            userNameView.setText(auth.getUsername());
            passWordView.setText(auth.getPassword());
            passWordView.requestFocus();
            passWordView.setSelection(auth.getPassword().length());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_auth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_save_auth) {
            BasicAuth auth = new BasicAuth();
            auth.setUsername(userNameView.getText().toString());
            auth.setPassword(passWordView.getText().toString());
            Intent intent = new Intent();
            intent.putExtra("auth", auth);
            setResult(300, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
