package com.wielabs.groupchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClubPosts extends AppCompatActivity {

    ListView l;
    List<Post> productsList;
    private String JSON_URL = "http://wielabs.esy.es/MamaGang/clubs.php?id=";
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_posts);
        l = (ListView) findViewById(R.id.clubPosts);
        position = getIntent().getIntExtra("position",0);
        Log.d("Position",position+"");
        productsList = new ArrayList<>();

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        loadHeroList();
    }

    private void loadHeroList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL+position,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray heroArray = obj.getJSONArray("menu");
                            Log.d("Menu",heroArray.toString());

                            //now looping through all the elements of the json array
                            Log.d("Length:",heroArray.length()+"");
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Post product = new Post(heroObject.getString("id"), heroObject.getString("cid"), heroObject.getString("title"), heroObject.getString("description"), heroObject.getString("infoline"), heroObject.getString("image_url"));

                                //adding the hero to herolist
                                productsList.add(product);
                            }

                            //creating custom adapter object
                            MenuAdapter adapter = new MenuAdapter(getApplicationContext(), productsList);


                            //adding the adapter to listview
                            l.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    class MenuAdapter extends ArrayAdapter<Post> {

        List<Post> ProductsList;
        Context context;
        private NetworkImageView imagee;
        private ImageLoader imageLoader;

        public MenuAdapter(Context context, List<Post> heroList) {
            super(context, R.layout.menu_list, heroList);
            this.context = context;
            this.ProductsList = heroList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            //getting the view
            View view = layoutInflater.inflate(R.layout.menu_list, null, false);

            //getting the view elements of the list from the view
            TextView title = view.findViewById(R.id.title);
            TextView description = view.findViewById(R.id.description);
            imagee = view.findViewById(R.id.imagee);
            final Post product = ProductsList.get(position);
            imageLoader = MyVolley.getInstance(getContext())
                    .getImageLoader();
            description.setText(product.getDescription());
            imagee.setImageUrl(product.getImage_url(), imageLoader);
            title.setText(product.getTitle());
            description.setText(product.getDescription());
            return view;
        }
    }
}
