package com.finwingway.digicob.sil;

public class Variables {

	// Bluetooth Chat
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int MESSAGE_WRITELand = 6;

	public static final String PREFERENCE = "preference",
			PRINTER_ID = "printerId", PRINTERNAME = "printername",
			Name = "name";

	public static String REPRINTPREF = "ReprintPref";
	public static String PRINTDATA = "printdata";
	public static String PRINTDUPDATA = "printdupdata";
	public static String PRINTERADD = "printadd";
	public static String SERIALNUM = "serialnum";
	public static String PRINTERCHARGE = "printercharge";

	public static int counter = 0;
	public static byte[] ToSend = new byte[1000];
	public static int imageWidth = 48;

}
