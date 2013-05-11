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
