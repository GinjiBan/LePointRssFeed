package com.example.ginji.rssfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Detail extends ActionBarActivity implements Serializable {

    private ImageView picView;
    private TextView titleView;
    private TextView dateView;
    private TextView descView;
    private Bitmap picture = null;
    private String title = null;
    private String pic = null;
    private String desc = null;
    private Date date = null;
    private String time = null;


    @Override
    protected void onUserLeaveHint() {
        picture = null;
        super.onUserLeaveHint();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("item", new Item(title, desc, date, picture, null));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        picView = (ImageView) findViewById(R.id.imgDetail);
        titleView = (TextView) findViewById(R.id.titleDetail);
        dateView = (TextView) findViewById(R.id.dateDetail);
        descView = (TextView) findViewById(R.id.descDetail);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss");
        int pos = -1;
        if (savedInstanceState != null) {
            Item newItem = (Item) savedInstanceState.getSerializable("item");
            title = newItem.getTitle();
            desc = newItem.getDesc();
            date = newItem.getDate();
            picture = newItem.getPic();
            picView.setImageBitmap(picture);
        } else {
            pos = (int) getIntent().getIntExtra("pos", -1);
            if (pos == -1) {
                title = (String) getIntent().getSerializableExtra("title");
                pic = (String) getIntent().getSerializableExtra("pic");
                desc = (String) getIntent().getSerializableExtra("desc");
                date = (Date) getIntent().getSerializableExtra("date");
                new getBigPic().execute(pic);
            } else {
                NewsDAO news = new NewsDAO(this);
                news.open();
                title = news.getItemWithId(pos + 1).getTitle();
                picture = news.getItemWithId(pos + 1).getPic();
                desc = news.getItemWithId(pos + 1).getDesc();
                date = news.getItemWithId(pos + 1).getDate();
                picView.setImageBitmap(picture);
            }
        }
        time = formatter.format(date);
        titleView.setText(title);
        dateView.setText("On " + time);
        descView.setText(desc);
    }

    private class getBigPic extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap image = null;
            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL(urls[0]);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                //return "Unable to retrieve web page. URL may be invalid.";
            }
            return image;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Bitmap result) {
            picture = result;
            picView.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
