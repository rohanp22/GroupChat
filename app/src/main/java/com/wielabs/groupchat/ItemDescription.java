package com.wielabs.groupchat;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDescription extends AppCompatActivity {

    SliderLayout sliderLayout;
    Button buy;
    TextView title,sp,mrp,availability,color,size;
    ShoppingItem pro;
    Spinner s;

    private final String JSON_URL = "http://wielabs.esy.es/MamaGang/shoppingItem.php?id=";
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        buy = (Button) findViewById(R.id.buyBtn);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://wielabs.esy.es/MamaGang/addtocart.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //displaying the error in toast if occurrs
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("userid", "12345678");
                        params.put("title",pro.getTitle());
                        params.put("image_url1",pro.getImage1());
                        params.put("mrp",pro.getMrp());
                        params.put("sp",pro.getSp());
                        params.put("size",s.getSelectedItem().toString());
                        return params;
                    }
                };

                //creating a request queue
                RequestQueue requestQueue = Volley.newRequestQueue(ItemDescription.this);

                //adding the string request to request queue
                requestQueue.add(stringRequest);
                
                //Intent i = new Intent(ItemDescription.this,)
            }
        });
        title = (TextView) findViewById(R.id.titleItem);
        sp = (TextView) findViewById(R.id.spItem);
        mrp = (TextView) findViewById(R.id.mrpItem);
        availability = (TextView) findViewById(R.id.availability);
        color = (TextView) findViewById(R.id.color);
        size = (TextView) findViewById(R.id.size);
        s = (Spinner) findViewById(R.id.sizeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sizes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);


        position = getIntent().getStringExtra("sid");
        Log.d("PositionItem",position);

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(5); //set scroll delay in seconds :
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
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject heroObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                ShoppingItem product = new ShoppingItem(heroObject.getString("id"), heroObject.getString("title"), heroObject.getString("img_url1"), heroObject.getString("img_url2"), heroObject.getString("img_url3"),heroObject.getString("mrp"),heroObject.getString("sp"),heroObject.getString("stock"),heroObject.getString("size"),heroObject.getString("color"),heroObject.getString("barcode"));
                                Log.d("imageurl",product.getImage1());
                                pro = product;

                                title.setText(product.getTitle());
                                mrp.setText("₹"+product.getMrp());
                                sp.setText("₹"+product.getSp());
                                availability.setText(availability.getText()+product.getStock());
                                color.setText(color.getText()+product.getColor());
                                size.setText(size.getText()+product.getSize());

                                for (int t = 0; t <= 2; t++) {

                                    SliderView sliderView = new SliderView(ItemDescription.this);

                                    switch (t){
                                        case 0:
                                            sliderView.setImageUrl(product.getImage1());
                                            break;
                                        case 1:
                                            sliderView.setImageUrl(product.getImage2());
                                            break;
                                        case 2:
                                            sliderView.setImageUrl(product.getImage3());
                                            break;
                                    }
                                    final int finalI = t;
                                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(SliderView sliderView) {
                                            Toast.makeText(ItemDescription.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

                                    //at last add this view in your layout :
                                    sliderLayout.addSliderView(sliderView);
                                }
                                //adding the hero to herolist
                            }

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

        //setSliderViews();
    }
}
