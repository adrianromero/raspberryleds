//    Raspberry LEDs is an application to remotely control Raspberry Pi LEDs connected to the GPIO.
//    Copyright (C) 2013 Adrián Romero Corchado.
//
//    This file is part of Web Common
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.raspberryleds;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int REQUEST_CODE = 123456;
	
	private boolean started = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		setContentView(R.layout.activity_main);
	    
//	    ConnectivityManager connMgr = (ConnectivityManager) 
//	            getSystemService(Context.CONNECTIVITY_SERVICE);
//	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//	        if (networkInfo != null && networkInfo.isConnected()) {
//	            // fetch data
//	        } else {
//	            // display error
//	        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);			
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		
		super.onPrepareOptionsMenu(menu);
		
        // Disable button if no recognition service is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
        	MenuItem speakmenu = menu.findItem(R.id.action_speak);
        	speakmenu.setEnabled(false);
        }  				
		
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		started = true;
		refreshLeds();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		started = false;
	}
	
	private void refreshLeds() {

		Log.d("com.adr.raspberryleds.LedInformation", "refreshing");

		new LedInformation(this, getSettingRpiUrl()) {
			
			private ProgressDialog progress;
			
			@Override
			protected void onPreExecute () {
				if (started) {
					progress = ProgressDialog.show(MainActivity.this, "", MainActivity.this.getResources().getText(R.string.loading));													
				} else {
					cancel(false);
				}
			}
			
			@Override
			protected void onPostExecute (JSONObject result) {
				
				if (started) {
					progress.dismiss();
					
					boolean success = checkSuccess(result);
					
					((Switch) MainActivity.this.findViewById(R.id.switch0)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch1)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch2)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch3)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch4)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch5)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch6)).setEnabled(success);
					((Switch) MainActivity.this.findViewById(R.id.switch7)).setEnabled(success);
					
					if (!success) {
						((Switch) MainActivity.this.findViewById(R.id.switch0)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch1)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch2)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch3)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch4)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch5)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch6)).setChecked(false);											
						((Switch) MainActivity.this.findViewById(R.id.switch7)).setChecked(false);																	
					}	
				}
				progress = null;	
			}						
		}.execute();				
	}
	
	public void onToggleClicked(View view) {
		
		Switch sw = ((Switch) view);
		actionLed(new LedCommand(new String[]{(String) sw.getTag()}, sw.isChecked() ? LedCommand.CMD_SWITCH_ON : LedCommand.CMD_SWITCH_OFF));
	}
	
	private void actionLed(LedCommand ledc) {

	    new LedActivate(this, getSettingRpiUrl(), ledc){
	    	@Override
	    	protected void onPostExecute (JSONObject result) {
	    		if (started) {
	    			checkSuccess(result);
	    		}
	    	}
	    }.execute();	
	}

	
	public void onRefreshClicked(MenuItem item) {
		
		refreshLeds();
	}

	public void onSpeakClicked(MenuItem item) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.adr.raspberryleds");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, this.getResources().getString(R.string.msg_speak_prompt));
        
        startActivityForResult(intent, REQUEST_CODE);            
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList<String> speechmatches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            
            LedCommandParser vcparser = new LedCommandParser(this.getResources());
        	LedCommand vc = vcparser.parseVoiceCommand(speechmatches);
        	if (vc == null) {
        		Toast toast = Toast.makeText(getApplicationContext(), 
        				this.getResources().getString(R.string.msg_command_not_recognized), 
        				Toast.LENGTH_SHORT);
        		toast.show();
        	} else {
        		// parse command and return ..
        		this.actionLed(vc);
        	} 
        }
        super.onActivityResult(requestCode, resultCode, data);
    } 
    
	public void onSettingsClicked(MenuItem item) {
	
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void onAboutClicked(MenuItem item) {
		
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	private boolean checkSuccess(JSONObject result) {
	
		if (result.has("exception")) {
			
			Log.d("com.adr.raspberryleds.MainActivity", result.toString());
			
			new AlertDialog.Builder(MainActivity.this)
		    .setTitle(MainActivity.this.getResources().getString(R.string.app_name))
		    .setMessage(MainActivity.this.getResources().getString(R.string.msg_cannot_reach_rpi))
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        }
		     })
		     .show();	
			return false;
		} else {
			refreshSwitch(result, R.id.switch0, "LED0");
			refreshSwitch(result, R.id.switch1, "LED1");
			refreshSwitch(result, R.id.switch2, "LED2");
			refreshSwitch(result, R.id.switch3, "LED3");
			refreshSwitch(result, R.id.switch4, "LED4");
			refreshSwitch(result, R.id.switch5, "LED5");
			refreshSwitch(result, R.id.switch6, "LED6");
			refreshSwitch(result, R.id.switch7, "LED7");				
			return true;
		}
	}
	
	private void refreshSwitch(JSONObject result, int id, String led) {
		if (result.has(led)) {
			((Switch) MainActivity.this.findViewById(id)).setChecked(result.optBoolean(led));												
		}	
	}
	
	private String getSettingRpiUrl() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPref.getString("pref_rpi_url", "");
	}
}
