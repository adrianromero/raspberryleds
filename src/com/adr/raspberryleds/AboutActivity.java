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

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }

        TextView tc = (TextView) findViewById(R.id.textContent);
        tc.setText(Html.fromHtml(getResources().getString(R.string.html_about)
                .replace("${app_name}", getResources().getString(R.string.app_name))
                .replace("${app_version}", versionName)));
        tc.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
