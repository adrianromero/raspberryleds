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
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LedDataFragment extends Fragment {

    public static final String TAG = "LEDDataFragment-Tag";

    private LedDataCallbacks callbacks = null;

    private Map<String, Boolean> ledstatus = null;
    private String ledexception = null;

    public void loadInit(String url) {

        if (ledstatus == null) {
            loadForce(url);
        } else {
            publishRefreshLedData();
        }
    }

    public void loadForce(String url) {

        ledstatus = null;
        ledexception = null;
        publishRefreshLedData();
        publishStartLoadLedData();

        new LedInformation(url) {
            @Override
            protected void onPostExecute(JSONObject result) {
                updateResults(result);
                publishFinishLoadLedData();
            }

            @Override
            protected void onCancelled() {
                publishCancelLoadLedData();
            }
        }.execute();
    }

    public void execute(String url, LedCommand lc) {
        new LedActivate(url, lc) {
            @Override
            protected void onPostExecute(JSONObject result) {
                updateResults(result);
            }

            @Override
            protected void onCancelled() {
            }
        }.execute();
    }

    private void updateResults(JSONObject result) {

        if (ledstatus == null) {
            ledstatus = new HashMap<String, Boolean>();
        }
        if (result.has("exception")) {
            ledexception = result.optString("exception");
        } else {
            ledexception = null;
            setLedInfo("LED0", result);
            setLedInfo("LED1", result);
            setLedInfo("LED2", result);
            setLedInfo("LED3", result);
            setLedInfo("LED4", result);
            setLedInfo("LED5", result);
            setLedInfo("LED6", result);
            setLedInfo("LED7", result);
        }
        publishRefreshLedData();
    }

    private void setLedInfo(String led, JSONObject result) {

        if (result.has(led)) {
            ledstatus.put(led, result.optBoolean(led, false));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    public void setLedDataListener(LedDataCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public boolean hasData() {
        return ledstatus != null;
    }

    public boolean hasDataException() {
        return ledexception != null;
    }

    public boolean getLedStatus(String led) {
        if (ledstatus == null) {
            return false;
        }
        Boolean b = ledstatus.get(led);
        return b == null ? false : b;
    }

    public boolean getLedEnabled(String led) {
        if (ledstatus == null) {
            return false;
        }
        return ledstatus.get(led) != null;
    }

    @Override
    public String toString() {
        if (hasData()) {
            if (hasDataException()) {
                return ledexception;
            } else {
                return ledstatus.toString();
            }
        } else {
            return "<No data>";
        }
    }

    private void publishRefreshLedData() {
        Log.d("com.adr.raspberryleds.LedDataFragment", "publishRefreshLedData");
        if (callbacks != null) {
            callbacks.onRefreshLedData();
        }
    }

    private void publishStartLoadLedData() {
        Log.d("com.adr.raspberryleds.LedDataFragment", "publishStartLoadedLedData");
        if (callbacks != null) {
            callbacks.onStartLoadLedData();
        }
    }

    private void publishFinishLoadLedData() {
        Log.d("com.adr.raspberryleds.LedDataFragment", "publishFinishLoadLedData");
        if (callbacks != null) {
            callbacks.onFinishLoadLedData();
        }
    }

    private void publishCancelLoadLedData() {
        Log.d("com.adr.raspberryleds.LedDataFragment", "publishCancelLoadLedData");
        if (callbacks != null) {
            callbacks.onCancelLoadLedData();
        }
    }

    public static interface LedDataCallbacks {

        public void onRefreshLedData();

        public void onStartLoadLedData();

        public void onFinishLoadLedData();

        public void onCancelLoadLedData();
    }
}
