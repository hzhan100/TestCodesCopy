package com.example.listtestfrominternet;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class InsertProgressbar {
	private Activity activity;
	private ProgressBar bar;
	private RelativeLayout relativeLayout;

	public InsertProgressbar(RelativeLayout view, Context context) {
		// frameLayout = (FrameLayout)
		// view.findViewById(R.id.list_span_thumbnail);
		relativeLayout = view;
		bar = new ProgressBar(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.getRules()[RelativeLayout.CENTER_IN_PARENT] = RelativeLayout.TRUE;
		bar.setLayoutParams(params);
		// bar.setVisibility(View.INVISIBLE);

	}

	public void insertBar() {
		bar.setProgress(0);

		if (!bar.isShown()) {
			bar.setVisibility(View.VISIBLE);
			relativeLayout.addView(bar);

		}

	}

	public void stopBar() {
		bar.setVisibility(View.INVISIBLE);
		relativeLayout.removeView(bar);
	}
}
