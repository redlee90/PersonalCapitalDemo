package com.ruili.personalcapitaldemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ruili.personalcapitaldemo.adapter.ItemRecyclerViewAdapter;
import com.ruili.personalcapitaldemo.model.Item;
import com.ruili.personalcapitaldemo.ui.RecyclerViewItemDecoration;
import com.ruili.personalcapitaldemo.util.Utilities;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

/*
 * Created by Rui Li on DEFAULT_PADDING/29/17.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int DEFAULT_PADDING = 10;

    // "Refresh" menu item name
    private static final String MENU_ITEM_REFRESH_NAME = "Refresh";

    // this data source for recyclerView, stores the second to last article item
    private List<Item> mItemList = new ArrayList<>();
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;

    private NestedScrollView scrollView;
    private LinearLayout firstItemLayout;
    private ImageView firstImageView;
    private TextView firstTvTitle;
    private TextView firstTvSummary;

    private RetainedFragment retainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int screenWidthPx = getResources().getDisplayMetrics().widthPixels;

        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);
        FrameLayout.LayoutParams progressBarLayoutParams = new FrameLayout.LayoutParams(screenWidthPx / 4, screenWidthPx / 4);
        progressBarLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        progressBar.setLayoutParams(progressBarLayoutParams);

        // root scrollview
        scrollView = new NestedScrollView(this);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        // linearlayout in vertical orientation
        LinearLayout topLinerLayout = new LinearLayout(this);
        topLinerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        topLinerLayout.setOrientation(LinearLayout.VERTICAL);

        // layout for first item
        firstItemLayout = new LinearLayout(this);
        firstItemLayout.setOrientation(LinearLayout.VERTICAL);
        firstItemLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        firstItemLayout.setBackground(getDrawable(R.drawable.bg_gray_rectangle));
        firstImageView = new ImageView(this);
        firstImageView.setAdjustViewBounds(true);
        firstTvTitle = new TextView(this);
        firstTvTitle.setSingleLine(true);
        firstTvTitle.setEllipsize(TextUtils.TruncateAt.END);
        firstTvTitle.setTextColor(Color.BLACK);
        firstTvTitle.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        firstTvSummary = new TextView(this);
        firstTvSummary.setMaxLines(2);
        firstTvSummary.setEllipsize(TextUtils.TruncateAt.END);
        firstTvSummary.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        firstItemLayout.addView(firstImageView);
        firstItemLayout.addView(firstTvTitle);
        firstItemLayout.addView(firstTvSummary);

        // layout for "previous articles" TextView
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        textView.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        textView.setTextColor(Color.BLACK);
        textView.setText(getString(R.string.previous_articles));

        // RecyclerView for other items
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        recyclerView.setPadding(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(DEFAULT_PADDING));
        recyclerView.setNestedScrollingEnabled(false);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(this, mItemList);
        recyclerView.setAdapter(itemRecyclerViewAdapter);

        float sceenWidthDp = Utilities.pxToDp(this, screenWidthPx);
        // a typical 7' tablet has a width of 600dp
        if (sceenWidthDp < 600) {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        }

        // add all view/layout(s) to top layout
        topLinerLayout.addView(firstItemLayout);
        topLinerLayout.addView(textView);
        topLinerLayout.addView(recyclerView);

        scrollView.addView(topLinerLayout);

        setContentView(progressBar);

        // if fragment does not exist, initialize it and attach it to activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        retainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(RetainedFragment.class.getSimpleName());
        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(retainedFragment, RetainedFragment.class.getSimpleName()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem refresh = menu.add(MENU_ITEM_REFRESH_NAME);
        refresh.setIcon(getDrawable(R.drawable.ic_refresh_white_24dp));
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(MENU_ITEM_REFRESH_NAME)) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    // populate view for first article(item)
    public void addFirstItem(final Item item) {
        setContentView(scrollView);

        firstImageView.setImageBitmap(item.getImageBitmap());
        firstTvTitle.setText(item.getTitle());

        StringBuilder descStringBuilder = new StringBuilder();

        String pubDate = item.getPubDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            Date date = originalFormat.parse(pubDate);
            String dateFormatted = dateFormat.format(date);
            descStringBuilder.append("<i>");
            descStringBuilder.append(dateFormatted);
            descStringBuilder.append("</i>");
            descStringBuilder.append("&#8212;");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        descStringBuilder.append(item.getDescription().replaceFirst("<p>", "").replaceFirst("</p>", ""));

        if (Build.VERSION.SDK_INT >= 24) {
            firstTvSummary.setText(Html.fromHtml(descStringBuilder.toString(), FROM_HTML_MODE_COMPACT));
        } else {
            firstTvSummary.setText(Html.fromHtml(descStringBuilder.toString()));
        }

        firstItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", item.getLink());
                intent.putExtra("title", item.getTitle());
                startActivity(intent);
            }
        });
    }

    // populate views for other articles(items)
    public void addOtherItems(List<Item> items) {
        mItemList.addAll(items);
        itemRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Call this function to manually refresh the screen
     */
    public void refresh() {
        // clear this list so we can fill it with newest feed
        mItemList.clear();

        retainedFragment.loadRssFeed();
    }

    /**
     * This fragment retains LoadRssFeedAsyncTask on orientation change. When this fragment was attached for
     * the first time, it will call loadRssFeed to start parsing xml file and pushing data to recyclerView.
     * If screen orientation changes during or after the process, it will call MainActivity's addFirstItem
     * and addOtherItems method to fill the data.
     */
    public static class RetainedFragment extends Fragment {
        // keep a reference of MainActivity
        private MainActivity mainActivity;

        // keep a reference of the first item
        private Item firstItem;

        // keep a reference of other items
        private List<Item> otherItems = new ArrayList<>();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // This method will allows us to retain the items and asynctask on orientation change
            setRetainInstance(true);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            mainActivity = (MainActivity) getActivity();

            /*
             * If this method is called for the first time, call loadRssFeed to start
             * otherwise it's likely that a configuration change just happened and we
             * just need to fill the data that we already have
             */
            if (firstItem != null) {
                mainActivity.addFirstItem(firstItem);
                mainActivity.addOtherItems(otherItems);
            } else {
                loadRssFeed();
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();

            /*
             * We no long have reference to MainActivity if it's detached
             */
            mainActivity = null;
        }

        /**
         * This method will execute an asynctask that parse and load data
         */
        void loadRssFeed() {
            firstItem = null;
            otherItems.clear();

            new LoadRssFeedAsyncTask().execute();
        }

        class LoadRssFeedAsyncTask extends AsyncTask<Void, Item, Void> {
            // this flag helps us determine whether it's loading the first article or others
            boolean isFirstItem = true;

            @Override
            protected Void doInBackground(Void... params) {
                InputStream inputStream = null;
                try {
                    URL url = new URL("https://blog.personalcapital.com/feed/?cat=3,891,890,68,284");
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setReadTimeout(15000);
                    httpsURLConnection.setConnectTimeout(15000);

                    httpsURLConnection.connect();

                    inputStream = httpsURLConnection.getInputStream();

                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);

                    // parse the xml stream
                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        if (!parser.getName().equals("item")) {
                            continue;
                        }

                        String title = null;
                        String imageUrl = null;
                        String description = null;
                        String pubDate = null;
                        String link = null;

                        while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals("item")) {
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }

                            // get tag name inside item tag
                            String name = parser.getName();

                            if (name.equals("title")) {
                                title = parser.nextText();
                            } else if (name.equals("link")) {
                                link = parser.nextText();
                            } else if (name.equals("pubDate")) {
                                pubDate = parser.nextText();
                            } else if (name.equals("description")) {
                                description = parser.nextText();
                            } else if (name.equals("media:content")) {
                                for (int i = 0; i < parser.getAttributeCount(); ++i) {
                                    if (parser.getAttributeName(i).equals("url")) {
                                        imageUrl = parser.getAttributeValue(i);
                                        break;
                                    }
                                }
                            }
                        }

                        Bitmap bitmap = Utilities.getBitmapFromURL(imageUrl);
                        if (bitmap == null) {
                            bitmap = ((BitmapDrawable) (getActivity().getDrawable(R.drawable.ic_refresh_white_24dp))).getBitmap();
                        }

                        // construct article item on the go
                        Item item = new Item(title, bitmap, description, pubDate, link);
                        publishProgress(item);

                    }
                } catch (IOException | XmlPullParserException e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Item... values) {
                super.onProgressUpdate(values);

                if (values != null && values.length > 0) {

                    // if first item, call addFirstItem of MainActivity
                    if (isFirstItem) {
                        firstItem = values[0];

                        if (mainActivity != null) {
                            mainActivity.addFirstItem(firstItem);
                        }

                        isFirstItem = false;
                    } else {
                        otherItems.add(values[0]);

                        if (mainActivity != null) {
                            List<Item> list = new ArrayList<>();
                            list.add(values[0]);

                            mainActivity.addOtherItems(list);
                        }
                    }
                }
            }
        }
    }
}
