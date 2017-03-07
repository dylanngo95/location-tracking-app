package com.jundat95.locationtracking.View.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jundat95.locationtracking.Common.MarkerManager;
import com.jundat95.locationtracking.Model.Node;
import com.jundat95.locationtracking.R;

import java.util.List;

/**
 * Created by tinhngo on 3/5/17.
 */

public class SelectNodeAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<Node> nodes;
    private LayoutInflater inflater;


    public SelectNodeAdapter(Context context, int resource, List<Node> nodes) {
        super(context, resource);
        this.context = context;
        this.nodes = nodes;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.spinner_select_node_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(MarkerManager.getImages(nodes.get(position).getNodeId()));
        textView.setText(nodes.get(position).getTitle());
        return view;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.spinner_select_node_item, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(MarkerManager.getImages(nodes.get(position).getNodeId()));
        textView.setText(nodes.get(position).getTitle());
        return view;
    }
}
