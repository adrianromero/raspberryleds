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
import android.content.res.Resources;

public class LedCommandParser {

    private String[] cmdSwitchOn;
    private String[] cmdSwitchOff;
    private String[] cmdToggle;

    private String[] ledZero;
    private String[] ledOne;
    private String[] ledTwo;
    private String[] ledThree;
    private String[] ledFour;
    private String[] ledFive;
    private String[] ledSix;
    private String[] ledSeven;
    private String[] ledAll;

    public LedCommandParser(Resources res) {

        cmdSwitchOn = res.getStringArray(R.array.switch_on);
        cmdSwitchOff = res.getStringArray(R.array.switch_off);
        cmdToggle = res.getStringArray(R.array.toggle);

        ledZero = res.getStringArray(R.array.zero);
        ledOne = res.getStringArray(R.array.one);
        ledTwo = res.getStringArray(R.array.two);
        ledThree = res.getStringArray(R.array.three);
        ledFour = res.getStringArray(R.array.four);
        ledFive = res.getStringArray(R.array.five);
        ledSix = res.getStringArray(R.array.six);
        ledSeven = res.getStringArray(R.array.seven);
        ledAll = res.getStringArray(R.array.all);
    }

    public LedCommand parseVoiceCommand(ArrayList<String> speechmatches) {

        String[] leds;
        String command;

        for (String s: speechmatches) {
            String[] words = s.split(" ");

            if (contains(words, ledAll)) {
                leds = LedCommand.LED_ALL;
            } else if (contains(words, ledZero)) {
                leds = LedCommand.LED_0;
            } else if (contains(words,ledOne)) {
                leds = LedCommand.LED_1;
            } else if (contains(words, ledTwo)) {
                leds = LedCommand.LED_2;
            } else if (contains(words, ledThree)) {
                leds = LedCommand.LED_3;
            } else if (contains(words, ledFour)) {
                leds = LedCommand.LED_4;
            } else if (contains(words, ledFive)) {
                leds = LedCommand.LED_5;
            } else if (contains(words, ledSix)) {
                leds = LedCommand.LED_6;
            } else if (contains(words, ledSeven)) {
                leds = LedCommand.LED_7;
            } else {
                continue;
            }

            if (contains(words, cmdSwitchOn)) {
                command = LedCommand.CMD_SWITCH_ON;
            } else if (contains(words, cmdSwitchOff)) {
                command = LedCommand.CMD_SWITCH_OFF;
            } else if (contains(words, cmdToggle)) {
                command = LedCommand.CMD_TOGGLE;
            } else {
                continue;
            }
            return new LedCommand(leds, command);
        }
        return null;
    }

    private boolean contains(String[] words, String[] res) {
        for (String w: words) {
            for (String r: res) {
                if (w.toUpperCase().equals(r.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
