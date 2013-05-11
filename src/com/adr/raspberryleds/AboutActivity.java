package com.adr.raspberryleds;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about);
		
		TextView tc = (TextView) this.findViewById(R.id.textContent);
		tc.setText(Html.fromHtml("<b>Title</b>" +  "<br />" + 
        "<small>description</small>" + "<br />" + 
        "<small>description</small><br/>" +
        "Sí es html"));		

	}	


}
