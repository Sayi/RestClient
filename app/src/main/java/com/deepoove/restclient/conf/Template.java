package com.deepoove.restclient.conf;

import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sayi
 */

public class Template {

    private String codeHtml;

    private static Template instance;

    //加载到内存，不允许大文件
    private Template(Resources resources) {
        try {
            codeHtml = init(resources.getAssets().open("code.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static synchronized Template getInstance(Resources resources) {
        if (null == instance) {
            instance = new Template(resources);
        }
        return instance;
    }

    public static synchronized Template getInstance() {
        return instance;
    }


    public String init(InputStream in) {
        return FileUtils.read(in);
    }
    public String getCodeHtml() {
        return codeHtml;
    }

}
