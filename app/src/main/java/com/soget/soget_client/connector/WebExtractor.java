package com.soget.soget_client.connector;

import android.os.AsyncTask;
import android.util.Log;

import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.model.Bookmark;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-22.
 */
public class WebExtractor extends AsyncTask<Void, Void, ArrayList<Bookmark>> {
    private OnTaskCompleted listener;
    private ArrayList<Bookmark> bookmarks;
    public WebExtractor(OnTaskCompleted listener, ArrayList<Bookmark> bookmarks){
        this.listener = listener;
        this.bookmarks = bookmarks;
    }

    @Override
    protected ArrayList<Bookmark> doInBackground(Void... params) {
        try{
            for(int i = 0 ; i < bookmarks.size() ; ++i){
                Document document = Jsoup.connect(bookmarks.get(i).getUrl()).get();
                bookmarks.get(i).setTitle(document.title());
                String img_url = getMetaTag(document, "og:image");
                String description = getMetaTag(document, "og:description");
                bookmarks.get(i).setImg_url(img_url);
                bookmarks.get(i).setDesc(description);
            }
            return bookmarks;

        } catch (Exception e){
            Log.e("RegisterRequestTask", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Bookmark> bookmarks){
        listener.onTaskCompleted(bookmarks);
    }

    private String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) return s;
        }
        return null;
    }

}
