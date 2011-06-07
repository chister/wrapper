package com.wrapper;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.wrapper.xml.XmlFeedPuller;

public class InitWrapper extends Activity {
	
	WebView webview;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, XmlFeedPuller.pullMobileSites());
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spinner.setAdapter(adapter);
        spinner.setPromptId(R.string.selectUrlPrompt);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				webview.loadUrl(XmlFeedPuller.getUrl(arg0.getItemAtPosition(arg2).toString()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
        	
        });
        
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        //webview.loadUrl("http://m.cnn.com/");
        
        //String data = "<!DOCTYPE HTML><html><body>Hello<br><video id=\"video\" title=\"Michael Chi\" src=\"http://ll.video.nfl.com/films/s2009/fantasy/w01/vikings_browns_0025394_3436_700k.mp4\" controls=\"controls\">Your browser sucks</video></body></html>";
        //webview.loadData(data, "text/html", "utf-8");
        
        webview.setWebViewClient(new WrapperWebViewClient());
    }
    
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
        	webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    private class WrapperWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}