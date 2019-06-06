package com.wielabs.groupchat;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Belal on 12/22/2015.
 */

public class GridViewAdapter extends ArrayAdapter<Products> {

    List<Products> ProductsList;
    Context context;
    private NetworkImageView imagee;
    private ImageLoader imageLoader;

    public GridViewAdapter(Context context, List<Products> heroList) {
        super(context, R.layout.menu_list, heroList);
        this.context = context;
        this.ProductsList = heroList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //getting the view
        View view = layoutInflater.inflate(R.layout.grid_single, null, false);

        //getting the view elements of the list from the view
        TextView title = view.findViewById(R.id.grid_text);
        //TextView description = view.findViewById(R.id.description);
        imagee = view.findViewById(R.id.grid_image);

        final Products product = ProductsList.get(position);
        imageLoader = MyVolley.getInstance(getContext())
                .getImageLoader();
        imagee.setImageUrl(product.getImage(), imageLoader);
        title.setText(product.getName());
        //description.setText(product.getDescription());
        return view;
    }
}