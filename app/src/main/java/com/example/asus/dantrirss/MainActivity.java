package com.example.asus.dantrirss;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.asus.dantrirss.utils.ArticleAdapter;
import com.example.asus.dantrirss.utils.ConnectionDetector;
import com.example.asus.dantrirss.utils.DBHelper;
import com.example.asus.dantrirss.utils.RSSItem;
import com.example.asus.dantrirss.utils.RssParser;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

	List<RSSItem> items;
	RssParser rssParser = new RssParser();
	ListView listView;
	DBHelper myDatabase;
	ConnectionDetector connectionDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initImageLoader(this);
		myDatabase=new DBHelper(this);
		connectionDetector=new ConnectionDetector(getApplicationContext());

		items = new ArrayList<>();
		listView = (ListView) findViewById(R.id.listview);


		if(connectionDetector.isConnectingToInternet())
			new GetListItem(this).execute();
		else {
			ArrayList<String> titleArticle=myDatabase.getAllArticle();
			items=new ArrayList<RSSItem>();
			for(String title:titleArticle){
				String titleNew=convertTitle(title);
				RSSItem rssItem=new RSSItem(titleNew,"","","","");
				items.add(rssItem);
			}
			ArticleAdapter adapter=new ArticleAdapter(this,items);
			listView.setAdapter(adapter);
		}


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(MainActivity.this, DetailArticleActivity.class);
				intent.putExtra("link", items.get(i).get_link());
				intent.putExtra("title",items.get(i).get_title());
				startActivity(intent);
			}
		});



	}

	public class GetListItem extends AsyncTask<Void, Void, Void> {

		Context context;
		ProgressDialog pd;

		GetListItem(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			items = rssParser.getRSSFeedItems("http://dantri.com.vn/trangchu.rss");

			Log.d("rss", items.size() + "");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			ArticleAdapter adapter = new ArticleAdapter(context, items);
			listView.setAdapter(adapter);

		}

	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
    private String convertTitle(String titleOld){
		int index1=titleOld.indexOf("<h1>");
		int index2=titleOld.indexOf("</h1");
		String titleNew=titleOld.substring(index1+4,index2);
		return titleNew;
	}

}