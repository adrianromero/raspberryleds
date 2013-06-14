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

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LedActivate extends AsyncTask<Void, Void, JSONObject> {

    private String url;
    private LedCommand ledc;

    public LedActivate(String url, LedCommand ledc) {

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
