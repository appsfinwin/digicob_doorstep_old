package com.finwingway.digicob;

/**
 * Created by AnVin on 4/12/2017.
 */

public final class GlobalApplicationConstants {
    public static String[] Customername,CustomerAccountNumber;
    public static void setCustomerName(String[] name){
        Customername=name;
    }
    public static String[] getCustomerName(){
        return Customername;
    }
    public static void setCustomerAccountNumber(String[] acc_no){
        CustomerAccountNumber=acc_no;
    }
    public static String[] getCustomerAccountNumber(){
        return CustomerAccountNumber;
    }

}