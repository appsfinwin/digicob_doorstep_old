package com.finwingway.digicob.sil;

public class DataToPrint {

    public static void setPrintData(String printData) {
        int b = 0 + 0 + 0 + 0 + 0 + 0 + 0 + 0;// <= font properties
        Variables.ToSend[Variables.counter++] = (byte) 0x1B;
        Variables.ToSend[Variables.counter++] = (byte) 0x21;
        Variables.ToSend[Variables.counter++] = (byte) b;

        String Typed_Text = printData.trim();
        for (int i = 0; i < Typed_Text.length(); i++) {
            Variables.ToSend[Variables.counter++] = (byte) Typed_Text.charAt(i);
        }
    }

//            BluetoothChat.printData();

    //           Intent intent = new Intent(getApplicationContext(), BluetoothChat.class);
    //           startActivity(intent);

//     if (mChatService != null) {
//        if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
//            new PrintTask().execute();
//        }
//    } else {
//        new SweetAlertDialog(Enquiry_Mini_Statement_List.this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("BT Device Not Connected")
//                .setContentText("please connect the device")
//                .setCancelText("NO")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        startActivity(new Intent(Enquiry_Mini_Statement_List.this, BluetoothChat.class));
//                        sweetAlertDialog.dismiss();
//                    }
//                }).show();
//    }

}
