package com.example.midBR;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Evertonj on 07/01/2017.
 */

public class HttpActivity extends AppCompatActivity {

    private static WebView web;
    private static String username = "";
    private static String keyid = "";
    private static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_activity);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        Bundle bundle = getIntent().getExtras();

        username = bundle.getString("username");
        keyid = bundle.getString("keyid");

        web = (WebView) findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.setVisibility(View.INVISIBLE);
        String urlParameters = "";
        try {
            urlParameters = "otp="+ URLEncoder.encode((username+keyid), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String finalUrlParameters = urlParameters;
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("StateId")) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    web.setVisibility(View.VISIBLE);
                }
                if(url.contains("AuthState")){
                    web.postUrl(url, finalUrlParameters.getBytes());
                }
            }
        });

        HttpActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = ProgressDialog.show(HttpActivity.this,"", "Acessando SP ...");
                web.loadUrl("https://sp-saml.gidlab.rnp.br/index.php/Especial:Autenticar-se");
            }
        });
    }

}
