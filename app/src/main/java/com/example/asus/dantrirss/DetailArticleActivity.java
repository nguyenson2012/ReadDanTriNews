package com.example.asus.dantrirss;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.asus.dantrirss.utils.ConnectionDetector;
import com.example.asus.dantrirss.utils.DBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class DetailArticleActivity extends Activity {

    WebView webview;
    String link, title, description, date;
    String detail = "";
    private String content;
    DBHelper myDatabase;
    ConnectionDetector connectionDetector;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_layout);
        myDatabase=new DBHelper(this);
        connectionDetector=new ConnectionDetector(getApplicationContext());
        webview = (WebView) findViewById(R.id.desc);
        tvContent=(TextView)findViewById(R.id.tvContent);
        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("link");
        title=bundle.getString("title");

        WebSettings webSettings = webview.getSettings();
        webSettings.setSupportZoom(true);


        if(connectionDetector.isConnectingToInternet()) {
            new GetData().execute();
            webview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }
            });
            webview.loadUrl(link);
        }
        else {
            ArrayList<String> titleArticles=myDatabase.getAllArticle();
            ArrayList<String> contentArticles=myDatabase.getAllContentArticle();
            for(int i=0;i<titleArticles.size();i++){
                String titleArticle=titleArticles.get(i);
                if(titleArticle.equals("<h1>"+title+"</h1>"))
                    tvContent.setText(contentArticles.get(i));
            }
        }






    }

    public class GetData extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect(link)
                        .get();

                Elements title = doc.select("div.ovh.detail_w h1");
                Elements date = doc.select("div.publishdate");
                Elements description = doc.select("div.ovh.detail_w h2");
                doc.select("table").remove();
                Elements main = doc.select("div.ovh.content ");
                detail += "<h2 style = \" color: red \">" + title.text()
                        + "</h2>";
                detail += "<font size=\" 1.2em \" style = \" color: #005500 \"><em>"
                        + date.text() + "</em></font>";
                detail += "<p style = \" color: #999999 \"><b>" + "<font size=\" 4em \" >"
                        + description.text() + "</font></b></p>";
                detail += "<font size=\" 4em \" >"+  main.toString() + "</font>";
                boolean addTitle=true;
                ArrayList<String> listTitle=myDatabase.getAllArticle();
                for(String titleArticle:listTitle){
                    if(titleArticle.equals(title.toString()))
                        addTitle=false;
                }
                if(addTitle)
                    myDatabase.insertArticle(title.toString(),main.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            webview.loadDataWithBaseURL(
//                    "",
//                    "<style>img{display: inline;height: auto;max-width: 100%;}"
//                            + " p {font-family:\"Tangerine\", \"Sans-serif\",  \"Serif\" font-size: 48px} </style>"
//                            + detail, "text/html", "UTF-8", "");


        }

    }
    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }
}
