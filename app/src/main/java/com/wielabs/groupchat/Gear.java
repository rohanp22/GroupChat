package com.wielabs.groupchat;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
import android.widget.GridView;
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

public class Gear extends AppCompatActivity {

    List<ShoppingItem> productsList;
    GridView l;

    private String JSON_URL = "http://wielabs.esy.es/MamaGang/gear.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear);

        l = (GridView) findViewById(R.id.shoppingList);
        productsList = new ArrayList<>();
        loadHeroList();

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sid = productsList.get(position).getId();
                Intent t = new Intent(Gear.this, ItemDescription.class);
                t.putExtra("sid",sid);
                startActivity(t);
            }
        });
    }

    private void loadHeroList() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
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
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                ShoppingItem product = new ShoppingItem(heroObject.getString("id"), heroObject.getString("title"), heroObject.getString("img_url1"), heroObject.getString("img_url2"), heroObject.getString("img_url3"),heroObject.getString("mrp"),heroObject.getString("sp"),heroObject.getString("stock"),heroObject.getString("size"),heroObject.getString("color"),heroObject.getString("barcode"));

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

    class MenuAdapter extends ArrayAdapter<ShoppingItem> {

        List<ShoppingItem> ProductsList;
        Context context;
        private NetworkImageView imagee;
        private ImageLoader imageLoader;

        public MenuAdapter(Context context, List<ShoppingItem> heroList) {
            super(context, R.layout.menu_list, heroList);
            this.context = context;
            this.ProductsList = heroList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            //getting the view
            View view = layoutInflater.inflate(R.layout.shoppingitem, null, false);

            //getting the view elements of the list from the view
            TextView title = view.findViewById(R.id.titleShopping);
            TextView description = view.findViewById(R.id.mrp);
            TextView sp = view.findViewById(R.id.sp);
            imagee = view.findViewById(R.id.shopping_image);
            final ShoppingItem product = ProductsList.get(position);
            imageLoader = MyVolley.getInstance(getContext())
                    .getImageLoader();
            imagee.setImageUrl(product.getImage1(), imageLoader);
            title.setText(product.getTitle());
            sp.setText("₹"+product.getSp());
            description.setText(product.getMrp());
            return view;
        }
    }
}
