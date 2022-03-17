package com.example.a2ndpractical;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RepositoryActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> descriptions = new ArrayList<String>();
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MyListAdapter adapter = new MyListAdapter(this, titles, descriptions, images, urls);

        ListView listView = (ListView) findViewById(R.id.item_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,  long id) {
                String url = urls.get(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                Toast.makeText(RepositoryActivity.this, "on item click: " + urls.get(position), Toast.LENGTH_LONG).show();
            }
        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.spaceflightnewsapi.net/v3/articles";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i("Praktiskais", "response:" + response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject article = response.getJSONObject(i);
                        titles.add(article.getString("title"));
                        descriptions.add(article.getString("summary"));
                        urls.add(article.getString("url"));
                        images.add(article.getString("imageUrl"));

                    }
                } catch (Exception e) {
                    Log.i("Praktiskais", "error:" + e.toString());
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Praktiskais", "error:" + error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                finish();
                Intent mapsActivity = new Intent(this, MapsActivity.class);
                startActivity(mapsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ArrayList<String> maintitle;
        private final ArrayList<String> description;
        private final ArrayList<String> imgid;
        private final ArrayList<String> url;

        public MyListAdapter(Activity context, ArrayList<String> maintitle, ArrayList<String> description, ArrayList<String> imgid, ArrayList<String> url) {
            super(context, R.layout.listview_item, maintitle);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.maintitle=maintitle;
            this.description=description;
            this.imgid=imgid;
            this.url = url;

        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.listview_item, null,true);

            TextView titleText = (TextView) rowView.findViewById(R.id.title);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView subtitleText = (TextView) rowView.findViewById(R.id.description);

            titleText.setText(maintitle.get(position));
            Picasso.get().load(images.get(position)).into(imageView);
//            imageView.setImageResource();
            subtitleText.setText(description.get(position));

            return rowView;

        };
    }

}
