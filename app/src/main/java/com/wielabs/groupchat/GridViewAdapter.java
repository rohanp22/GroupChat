package com.wielabs.groupchat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Belal on 12/22/2015.
 */
public class GridViewAdapter extends BaseAdapter {

    //Imageloader to load images
    private ImageLoader imageLoader;

    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private String[] images;
    private String[] names;

    public GridViewAdapter (Context context, String[] images, String[] names){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = names;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creating a linear layout

        View grid;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = inflater.inflate(R.layout.grid_single, parent,false);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            NetworkImageView networkImageView = (NetworkImageView) grid.findViewById(R.id.grid_image);

            //NetworkImageView

            //Initializing ImageLoader
            imageLoader = MyVolley.getInstance(context).getImageLoader();
            imageLoader.get(images[position], ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

            //Setting the image url to load
            networkImageView.setImageUrl(images[position], imageLoader);

            //Creating a textview to show the title
            textView.setText(names[position]);

            networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            networkImageView.setLayoutParams(new GridView.LayoutParams(200, 200));

        return parent;
    }
}