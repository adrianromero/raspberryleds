package com.adr.raspberryleds;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LedInformation extends AsyncTask<Void, Void, JSONObject> {

	private String url;
	
	public LedInformation(Context cnt, String url) {
		super();
		
		this.url = url;
	}

	@Override
	protected JSONObject doInBackground(Void... params) {

		JSONObject query = new JSONObject();
		try {
			query.put("LED0", "get");
			query.put("LED1", "get");			
			query.put("LED2", "get");			
			query.put("LED3", "get");			
			query.put("LED4", "get");			
			query.put("LED5", "get");			
			query.put("LED6", "get");			
			query.put("LED7", "get");			
			return HTTPUtils.execPOST(url, query);
		} catch (JSONException e) {
			return null; // never thrown.
		} catch (IOException e) {
			Log.d("com.adr.raspberryleds.LedInformation", null, e);
			try {
				query.put("exception", e.getMessage());
			} catch (JSONException e1) {			
				return null; // never reached.
			}
			return query;
		}	
	}	
}
