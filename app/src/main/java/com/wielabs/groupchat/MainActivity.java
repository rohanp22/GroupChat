package com.wielabs.groupchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    DatabaseReference reference, reference1;
    private List<Products> productsList;
    private String JSON_URL = "http://wielabs.esy.es/MamaGang/forums.php";

    ArrayList<String> arrayList, arrayList1;

    //EditText e1;
    ListView l1;
    GridView gridView;
    ArrayAdapter<String> adapter;
    GridViewAdapter adapter2;
    ListView l;
    String name;
    EditText ee;
    private static String JSON_URL_NOTIF = "http://wielabs.esy.es/MamaGang/notif.php";
    public List<Products> productsList2;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //e1 = (EditText)findViewById(R.id.editText);
        l1 = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        productsList = new ArrayList<>();
        l = (ListView) findViewById(R.id.forums);
        gridView = (GridView) findViewById(R.id.gridview);
        String images[] = new String[10];

//        Toast.makeText(this,SharedPrefManager.getInstance(this).getDeviceToken()+"",Toast.LENGTH_LONG);
//           Log.d("TOken",SharedPrefManager.getInstance(this).getDeviceToken());

        for (int i = 0; i < 7; i++)
            images[i] = "https://image.shutterstock.com/image-photo/various-sport-tools-on-grass-260nw-522863668.jpg";

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        reference = FirebaseDatabase.getInstance().getReference().getRoot();
        request_username();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();

                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }

                arrayList.clear();
                arrayList.addAll(set);
                l1.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Errorr", databaseError.toString());
                Toast.makeText(MainActivity.this, "No network connectivity", Toast.LENGTH_SHORT).show();
            }
        });

        loadHeroList();
        loadNotification();

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Chatroom.class);
                intent.putExtra("room_name", arrayList.get(i));
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = productsList2.get(position).getId();
                Log.d("position",productsList2.get(position).getId()+"");
                Intent i = new Intent(MainActivity.this,WallActivity.class);
                i.putExtra("position",pos);
                startActivity(i);
            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = productsList.get(position).getId();
                Log.d("position",productsList.get(position).getId()+"");
                Intent i = new Intent(MainActivity.this,ClubPosts.class);
                i.putExtra("position",pos);
                startActivity(i);
            }
        });


    }

    public void request_username() {
        name = "You";
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Enter your name?");
//        ee = new EditText(this);
//        builder.setView(ee);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                name = ee.getText().toString();
//
//
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//                request_username();
//
//
//            }
//        });
//        builder.show();

    }

    public void insert_data(View v) {
//        Map<String,Object> map = new HashMap<>();
//        map.put(e1.getText().toString(), "");
//        reference.updateChildren(map);
//        e1.setText("");
    }

    ProgressDialog progressDialog;

    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();

        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("email", "rohan@gmail.com");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendd(View view) {
        sendTokenToServer();
    }

    class MenuAdapter extends ArrayAdapter<Products> {

        List<Products> ProductsList;
        Context context;
        private NetworkImageView imagee;
        private ImageLoader imageLoader;

        public MenuAdapter(Context context, List<Products> heroList) {
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
            final Products product = ProductsList.get(position);
            imageLoader = MyVolley.getInstance(getContext())
                    .getImageLoader();
            imagee.setImageUrl(product.getImage(), imageLoader);
            title.setText(product.getName());
            description.setText(product.getDescription());
            return view;
        }
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
                                Products product = new Products(heroObject.getString("id"), heroObject.getString("image_url"), heroObject.getString("title"), heroObject.getString("description"));

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



    private void loadNotification() {

        productsList2 = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,JSON_URL_NOTIF,
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
                                Products product = new Products(heroObject.getString("id"), heroObject.getString("image_url"), heroObject.getString("title"), heroObject.getString("description"));

                                //adding the hero to herolist
                                productsList2.add(product);
                            }

                            //creating custom adapter object
                            GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(), productsList2);


                            //adding the adapter to listview
                            gridView.setAdapter(adapter);

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

}