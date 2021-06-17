package com.finwingway.digicob.loanModule.JLGwithSplit;

public class TransactionLoanGetSet {

    private static String StrAccountNo = "null";
    private static String StrTransType = "null";
    private static String StrEffctDate = "null";
    private static String StrDate = "null";
    private static String StrSubTransType = "null";
    private static String StrSchCode = "null";
    private static String TransStrAccno = "null";
    private static String StrRemitncAmnt = "null";

    public static void setAccountNo(String _StrAccountNo) {
        StrAccountNo = _StrAccountNo;
    }

    public static String getAccountNo() {
        return StrAccountNo;
    }


    public static void setTransAccountNo(String _TransStrAccno) {
        TransStrAccno = _TransStrAccno;
    }

    public static String getTransAccountNo() {
        return TransStrAccno;
    }


    public static void setTransType(String _StrTransType) {
        StrTransType = _StrTransType;
    }

    public static String getTransType() {
        return StrTransType;
    }


    public static void setEffctDate(String _StrEffctDate) {
        StrEffctDate = _StrEffctDate;
    }

    public static String getEffctDate() {
        return StrEffctDate;
    }


    public static void setDate(String _StrDate) {
        StrDate = _StrDate;
    }

    public static String getDate() {
        return StrDate;
    }


    public static void setSubTransType(String _StrSubTransType) {
        StrSubTransType = _StrSubTransType;
    }

    public static String getSubTransType() {
        return StrSubTransType;
    }


    public static void setSchCode(String _StrSchCode) {
        StrSchCode = _StrSchCode;
    }

    public static String getSchCode() {
        return StrSchCode;
    }



    public static void setTotalRemitncAmnt(String _StrRemitncAmnt) {
        StrRemitncAmnt = _StrRemitncAmnt;
    }

    public static String getTotalRemitncAmnt() {
        return StrRemitncAmnt;
    }

}
