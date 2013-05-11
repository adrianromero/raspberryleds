package com.adr.raspberryleds;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LedActivate extends AsyncTask<Void, Void, JSONObject> {

	
	private String url;
	private LedCommand ledc;
	
//	private ProgressDialog progress = null;
	
	public LedActivate(Context cnt, String url, LedCommand ledc) {
		super();
		
		this.url = url;
		this.ledc = ledc;
	}	

	@Override
	protected JSONObject doInBackground(Void... params) {

		JSONObject query = new JSONObject();
		try {
			for (String led: ledc.getLeds()) {
				query.put(led, ledc.getCommand());
			}
			return HTTPUtils.execPOST(url, query);
		} catch (JSONException e) {
			return null; // never reached.
		} catch (IOException e) {
			Log.d("com.adr.raspberryleds.LedActivate", null, e);	

			try {
				query.put("exception", e.getMessage());
			} catch (JSONException e1) {			
				return null; // never reached.
			}
			return query;
		}
	}	
}
