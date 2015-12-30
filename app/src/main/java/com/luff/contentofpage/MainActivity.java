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

import com.luff.contentofpage.models.SlidingItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

    Button btnRead;

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
            //InputStream is;
            String urlStr = params[0];
            //String line = "";
            SlidingItem item;
            ArrayList<SlidingItem> listPTag = new ArrayList<>();
            try {
//                Elements elements =doc.select("div.fck_detail.width_common").select("img");
//                for(int i=0;i<elements.size();i++)
//                {
//                    System.out.println("Sources of "+ i +":"+elements.get(i).attr("src"));
//                    listPTag.add(elements.get(i).attr("src").toString());
//                }


                // doan nay lay ra duoc ảnh dang bitmap.
                //Document doc = Jsoup.parse(Parser.unescapeEntities(Jsoup.connect(urlStr).get().toString(), false), "", Parser.xmlParser());
//                Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
//                for (Element image : images) {
//                    System.out.println("src : " + image.attr("src"));
//                    URL url = new URL(image.attr("src"));
//                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    slidingItem = new SlidingItem();
//                    slidingItem.setTitle(image.attr("src"));
//                    slidingItem.setIcon(bmp);
//                    listPTag.add(slidingItem);

                Document doc = Jsoup.connect(urlStr).get();
                Elements links = doc.select("item");
                for (int i = 0; i < links.size(); i++) {
                    item = new SlidingItem();
                    item.setTitle(links.get(i).getElementsByAttribute("src~=(?i)\\\\.(png|jpe?g|gif)").text());
                    //item.setIcon(getImage(linkInner));
                    listPTag.add(item);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listPTag;
        }

        protected void onPostExecute(ArrayList<SlidingItem> result) {
            tv = (TextView) findViewById(R.id.tv_title);
            iv = (ImageView) findViewById(R.id.iv_icon);
            tv.setText(result.get(0).getTitle());
            //Log.i("result", result.get(0).getTitle());
            iv.setImageBitmap(result.get(0).getIcon());
        }

    }

    private Bitmap getImage(String linkInners) {
        URL url = null;
        Bitmap bmp = null;
        try {
            url = new URL(linkInners);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmp;
    }
}

