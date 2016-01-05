package com.luff.contentofpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.luff.contentofpage.adapters.ReviewAdapter;
import com.luff.contentofpage.models.SlidingItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {

    Button btnRead;
    ListView lv;
    TextView tv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead = (Button) findViewById(R.id.bt_read);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WebpageDownloader().execute(
                        "http://vnexpress.net/rss/tin-moi-nhat.rss");

            }
        });
    }


    // hảm trả về số  content trong tag
    public class WebpageDownloader extends AsyncTask<String, Void, ArrayList<SlidingItem>> {

        @Override
        protected ArrayList<SlidingItem> doInBackground(String... params) {
            String urlStr = params[0];
            SlidingItem item;
            ArrayList<SlidingItem> listPTag = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(urlStr).timeout(100000).get();
                Elements nodes = doc.getElementsByTag("item");
                for (Element nodeItem : nodes) {
                    item = new SlidingItem();
                    String title = nodeItem.select("title").first().text();
                    item.setTitle(title);
                    Log.i("title", title);
                    String unescapedHtml = nodeItem.select("description").first().text();
                    String src = Jsoup.parse(unescapedHtml).select("img").first().attr("src");
                    Log.i("src", src);
                    URL url = new URL(src);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    item.setIcon(bmp);
                    listPTag.add(item);

                }
                Log.i("size", listPTag.size() + "");
                return listPTag;


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(ArrayList<SlidingItem> result) {
            lv = (ListView) findViewById(R.id.lv_review);
            ReviewAdapter adapter = new ReviewAdapter(MainActivity.this, result);
            lv.setAdapter(adapter);
        }
    }
}

