package com.example.a2ndpractical;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RepositoryActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> publishedDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MyListAdapter adapter = new MyListAdapter(this, titles, descriptions, authors, publishedDates);

        ListView listView = findViewById(R.id.item_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = urls.get(position);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        NewsApiClient newsApiClient = new NewsApiClient("e26ad9a5ae3543aab782ac7e7b1d41cc");
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Ukraine")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        for (int i = 0; i < 5; i++) {
                            titles.add(response.getArticles().get(i).getTitle());
                            descriptions.add(response.getArticles().get(i).getDescription());
                            urls.add(response.getArticles().get(i).getUrl());
                            images.add(response.getArticles().get(i).getUrlToImage());
                            authors.add(response.getArticles().get(i).getAuthor());
                            publishedDates.add(response.getArticles().get(i).getPublishedAt());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Praktiskais", "error:" + throwable.getMessage());
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.map) {
            finish();
            Intent mapsActivity = new Intent(this, MapsActivity.class);
            startActivity(mapsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> maintitle;
        private final ArrayList<String> description;
        private final ArrayList<String> author;
        private final ArrayList<String> datePublished;

        public MyListAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> description,
                             ArrayList<String> author, ArrayList<String> datePublished) {
            super(context, R.layout.listview_item, maintitle);
            this.context = context;
            this.maintitle = maintitle;
            this.description = description;
            this.author = author;
            this.datePublished = datePublished;
        }

        @SuppressLint("SetTextI18n")
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.listview_item, null,true);

            TextView titleText = rowView.findViewById(R.id.title);
            ImageView imageView = rowView.findViewById(R.id.icon);
            TextView subtitleText = rowView.findViewById(R.id.description);
            TextView authorText = rowView.findViewById(R.id.author);
            TextView datePublishedText = rowView.findViewById(R.id.datePublished);

            titleText.setText(maintitle.get(position));
            Picasso.get().load(images.get(position)).into(imageView);
            subtitleText.setText(description.get(position));
            authorText.setText("by " + author.get(position));
            datePublishedText.setText("Published at: " + datePublished.get(position).substring(0, 10) + " " + datePublished.get(position).substring(11, 16));

            return rowView;

        }
    }
}
