package com.example.ginji.rssfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ginji on 19/02/2015.
 */
public class NetworkFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String DEBUG_TAG = "HttpExample";
    private static final String STRING_URL = "http://www.lepoint.fr/high-tech-internet/rss.xml";
    private View mView;
    private Activity mActivity;
    private XmlParser parser = null;
    private List<Item> listItem = null;
    private ListView listView;
    private int i = 0;
    boolean isPressed = false;
    SwipeRefreshLayout swipeLayout;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (listItem != null && isPressed == false) {
            for (int i = 0; i < listItem.size(); i++)
                savedInstanceState.putSerializable(String.valueOf(i), listItem.get(i));
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mActivity = getActivity();
        super.onCreate(savedInstanceState);
        listView = (ListView) mView.findViewById(R.id.list_news);
        swipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        if (savedInstanceState != null) {
            Item test = null;
            List tmpList = new ArrayList();
            i = 0;
            while ((test = (Item) savedInstanceState.getSerializable(String.valueOf(i))) != null) {
                tmpList.add(i, test);
                i++;
            }
            listItem = tmpList;
        }
        if (i != 0) {
            ItemAdapter adapter = (new ItemAdapter((Context) (this.getActivity()), (ArrayList) (listItem)));
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    isPressed = true;
                    int itemPosition = position;
                    Item itemValue = (Item) listView.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), Detail.class);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });
        } else {
            ConnectivityManager connMgr = (ConnectivityManager)
                    mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask().execute(STRING_URL);
            } else {
                getDataFromDB();
            }
        }
        return (mView);
    }

    public void getDataFromDB() {
        NewsDAO news = new NewsDAO(mActivity);
        news.open();
        int i = 1;
        Item tmp = null;
        List tmpList = new ArrayList();
        while ((tmp = news.getItemWithId(i)) != null) {
            tmpList.add(tmp);
            i++;
        }
        listItem = tmpList;
        ItemAdapter adapter = (new ItemAdapter((Context) (this.getActivity()), (ArrayList) (listItem)));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*
                ** TODO : Check if co
                */
                isPressed = true;
                int itemPosition = position;
                Item itemValue = (Item) listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });
    }

    public void addListItem() {
        if (listItem == null)
            return;
        ItemAdapter adapter = (new ItemAdapter((Context) (this.getActivity()), (ArrayList) (listItem)));
        listView.setAdapter(adapter);
        NewsDAO news = new NewsDAO(mActivity);
        news.open();
        news.delete();
        for (int i = 0; i < listItem.size(); i++)
            news.insert(listItem.get(i));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                isPressed = true;
                int itemPosition = position;
                Item itemValue = (Item) listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), Detail.class);
                intent.putExtra("title", listItem.get(position).getTitle());
                intent.putExtra("date", listItem.get(position).getDate());
                intent.putExtra("desc", listItem.get(position).getDesc());
                intent.putExtra("pic", listItem.get(position).getImgLink());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connMgr = (ConnectivityManager)
                        mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    swipeLayout.setRefreshing(false);
                    new DownloadWebpageTask().execute(STRING_URL);
                } else {
                    swipeLayout.setRefreshing(false);
                }
            }
        }, 5000);
    }

    @Override
    public void onClick(View v) {

    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            addListItem();
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000 /* milliseconds */);
            conn.setConnectTimeout(150000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            try {
                parser = new XmlParser();
                listItem = parser.parse(is);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}