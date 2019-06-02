package com.wielabs.groupchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    DatabaseReference reference;

    ArrayList<String> arrayList,arrayList1;

    EditText e1;
    GridView l1;
    ArrayAdapter<String> adapter;
    GridViewAdapter adapter2;
    String name;
    EditText ee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = (EditText)findViewById(R.id.editText);
        l1 = (GridView) findViewById(R.id.gridView);
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        String images[] = new String[10];


//        Toast.makeText(this,SharedPrefManager.getInstance(this).getDeviceToken()+"",Toast.LENGTH_LONG);
//           Log.d("TOken",SharedPrefManager.getInstance(this).getDeviceToken());

        for(int i = 0; i < 7;i++)
            images[i] = "https://image.shutterstock.com/image-photo/various-sport-tools-on-grass-260nw-522863668.jpg";

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        arrayList1.add("abc");
        arrayList1.add("cdf");
        arrayList1.add("cadf");
        adapter2 = new GridViewAdapter(this,images,arrayList);

        if(l1.getCount() > 0) {
            Toast.makeText(MainActivity.this, "count > 0", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this,"count = 0",Toast.LENGTH_LONG).show();
        }



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
                adapter2.notifyDataSetChanged();
                l1.setAdapter(adapter2);
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Errorr",databaseError.toString());
                Toast.makeText(MainActivity.this, "No network connectivity", Toast.LENGTH_SHORT).show();
            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Chatroom.class);
                intent.putExtra("room_name", arrayList.get(i));
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });
    }

    public void request_username()
    {
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

    public void insert_data(View v)
    {
        Map<String,Object> map = new HashMap<>();
        map.put(e1.getText().toString(), "");
        reference.updateChildren(map);
        e1.setText("");
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
                params.put("email","rohan@gmail.com");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendd(View view) {
        sendTokenToServer();
    }
}
