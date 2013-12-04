package com.example.listtestfrominternet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> data;
	private Activity activity;
	private LayoutInflater inflater;
	private MyImageLoader imageloader;
	private OneThreadImageLoader newImageLoader;

	public NewListAdapter(ArrayList<HashMap<String, String>> data,
			Activity activity) {
		this.activity = activity;
		this.data = data;
		inflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageloader = new MyImageLoader(activity.getApplicationContext());
		newImageLoader = new OneThreadImageLoader(activity);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder viewholder = new ViewHolder();
		// if (convertView == null) {
		// convertView = inflater.inflate(R.layout.list_raw, null);
		// viewholder.title = (TextView) convertView
		// .findViewById(R.id.list_span_title);
		// viewholder.message = (TextView) convertView
		// .findViewById(R.id.list_span_summary);
		// viewholder.image = (ImageView) convertView
		// .findViewById(R.id.list_span_image1);
		// convertView.setTag(viewholder);
		// } else {
		// viewholder = (ViewHolder) convertView.getTag();
		// }
		//
		// HashMap<String, String> hash = new HashMap<String, String>();
		// hash = data.get(position);
		// viewholder.title.setText(hash.get("Title").toString());
		// String url = hash.get("Image").toString();
		//
		// // imageloader.DisplayImage(url, viewholder.image);
		// newImageLoader.DisplayImage(url, viewholder.image);
		// return convertView;

		View tempView = convertView;
		if (tempView == null)
			tempView = inflater.inflate(R.layout.list_raw, null);
		TextView title = (TextView) tempView.findViewById(R.id.list_span_title);
		TextView summary = (TextView) tempView
				.findViewById(R.id.list_span_summary);
		RelativeLayout relativeLayout = (RelativeLayout) tempView
				.findViewById(R.id.list_span_progressbar);

		ImageView image = (ImageView) tempView
				.findViewById(R.id.list_span_image1);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash = data.get(position);

		// retrieve data from hashmap
		title.setText(hash.get("Title").toString());
		String url = hash.get("Image").toString();

		// imageloader.DisplayImage(url, image);
		newImageLoader.DisplayImage(url, image, relativeLayout);
		return tempView;
	}

	static class ViewHolder {
		ImageView image = null;
		TextView title = null;
		TextView message = null;

	}

}
