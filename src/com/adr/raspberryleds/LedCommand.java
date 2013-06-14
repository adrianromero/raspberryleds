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

public class LedCommand {

    public static final String[] LED_0 = {"LED0"};
    public static final String[] LED_1 = {"LED1"};
    public static final String[] LED_2 = {"LED2"};
    public static final String[] LED_3 = {"LED3"};
    public static final String[] LED_4 = {"LED4"};
    public static final String[] LED_5 = {"LED5"};
    public static final String[] LED_6 = {"LED6"};
    public static final String[] LED_7 = {"LED7"};
    public static final String[] LED_ALL = {"LED0", "LED1", "LED2", "LED3", "LED4", "LED5", "LED6", "LED7"};

    public static final String CMD_SWITCH_ON = "on";
    public static final String CMD_SWITCH_OFF = "off";
    public static final String CMD_TOGGLE = "toggle";

    private String command = null;
    private String[] leds = {};

    public LedCommand(String[] leds, String command) {
        this.leds = leds;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String[] getLeds() {
        return leds;
    }
}
