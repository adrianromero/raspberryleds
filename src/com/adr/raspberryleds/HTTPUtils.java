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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPUtils {

    public static JSONObject execPOST(String address, JSONObject params) throws IOException {

        BufferedReader readerin = null;
        Writer writerout = null;

        try {
            URL url = new URL(address);
            String query = params.toString();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("POST");
            connection.setAllowUserInteraction(false);

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.addRequestProperty("Content-Type", "application/json,encoding=UTF-8");
            connection.addRequestProperty("Content-length", String.valueOf(query.length()));

            writerout = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writerout.write(query);
            writerout.flush();

            writerout.close();
            writerout = null;

            int responsecode = connection.getResponseCode();
            if (responsecode == HttpURLConnection.HTTP_OK) {
                StringBuilder text = new StringBuilder();

                readerin = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = readerin.readLine()) != null) {
                    text.append(line);
                    text.append(System.getProperty("line.separator"));
                }

                JSONObject result = new JSONObject(text.toString());

                if (result.has("exception")) {
                    throw new IOException(MessageFormat.format("Remote exception: {0}.", result.getString("exception")));
                } else {
                    return result;
                }
            } else {
                throw new IOException(MessageFormat.format("HTTP response error: {0}. {1}", Integer.toString(responsecode), connection.getResponseMessage()));
            }
        } catch (JSONException ex) {    
            throw new IOException(MessageFormat.format("Parse exception: {0}.", ex.getMessage()));
        } finally {
            if (writerout != null) {
                writerout.close();
                writerout = null;
            }
            if (readerin != null) {
                readerin.close();
                readerin = null;
            }
        }
    }
}
