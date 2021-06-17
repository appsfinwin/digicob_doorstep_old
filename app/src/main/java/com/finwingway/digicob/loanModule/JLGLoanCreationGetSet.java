package com.finwingway.digicob.loanModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JLGLoanCreationGetSet {

    private static String StrTempDisbrAmnt = "0.00";
    private static String StrScheme_no = "null";
    private static String StrIsSplit = "null";
    private static String StrEMIType = "null";
    private static String StrCalcType = "null";
    private static String StrBrnch_code = "null";
    private static String StrRemark = "null";
    private static String StrSector = "null";
    private static String StrSubSector = "null";
    private static String StrGroupData = "null";
    private static double StrTotalAmnt = 0;
    private static String StrApplctn_no = "null";
    private static String StrGroupCode = "";
    private static String StrSelect_grp = "null";

    static String LN_SLNO = "Slno";
    static String LN_CUSTID = "CustID";
    static String LN_CUSTNAME = "Customer Name";
    static String LN_COAPPLICNT = "CoApplicant";
    static String LN_MOB = "Mobile";
    static String LN_ADDRESS = "Address";//_-_-_-_-_-_-
    static String LN_CONSUMERGOODS = "ConsumerGoods";
    static String LN_AMOUNT = "Amount";
    static String LN_INSURANCE_FEE = "Insurance Fee";
    static String LN_DOCUMNT_FEE = "Documentation Fee";
    static String LN_CGST = "CGST";
    static String LN_SGST = "SGST";
    static String LN_CESS = "Cess";
    static String LN_DISBRSMNT_AMNT = "Disbursement Amount";
    static String LN_ACTION = "Action";

    public static final List<String> OptionList = new ArrayList<String>(Arrays.asList("Disbursement", "Consumer Goods"));

    public static String[] StrProductIdArry, StrProductNameArry;

    public static ArrayList<HashMap<String, String>> listAdapteTwo;

    //--------------------------

    public static String getTempDisbrsmntAmnt() {
        return StrTempDisbrAmnt;
    }

    public static void setTempDisbrsmntAmnt(String _StrTempDisbrAmnt) {
        StrTempDisbrAmnt = _StrTempDisbrAmnt;
    }

    //--------------------------

    public static String getApplctn_no() {
        return StrApplctn_no;
    }

    public static void setApplctn_no(String _StrApplctn_no) {
        StrApplctn_no = _StrApplctn_no;
    }


    public static String getSchemeNo() {
        return StrScheme_no;
    }

    public static void setSchemeNo(String _StrScheme_no) {
        StrScheme_no = _StrScheme_no;
    }


    public static String getIsSplit() {
        return StrIsSplit;
    }

    public static void setIsSplit(String _IsSplit) {
        StrIsSplit = _IsSplit;
    }


    public static String getEMIType() {
        return StrEMIType;
    }

    public static void setEMIType(String _StrEMIType) {
        StrEMIType = _StrEMIType;
    }


    public static String getCalcType() {
        return StrCalcType;
    }

    public static void setCalcType(String _StrCalcType) {
        StrCalcType = _StrCalcType;
    }


    public static String getBrnchCode() {
        return StrBrnch_code;
    }

    public static void setBrnchCode(String _StrBrnch_code) {
        StrBrnch_code = _StrBrnch_code;
    }


    public static String getRemark() {
        return StrRemark;
    }

    public static void setRemark(String _Remark) {
        StrRemark = _Remark;
    }


    public static String getSector() {
        return StrSector;
    }

    public static void setSector(String _Sector) {
        StrSector = _Sector;
    }


    public static String getSubSector() {
        return StrSubSector;
    }

    public static void setSubSector(String _SubSector) {
        StrSubSector = _SubSector;
    }


    public static String getGroupData() {
        return StrGroupData;
    }

    public static void setGroupData(String _GroupData) {
        StrGroupData = _GroupData;
    }


    public static double getTotalAmnt() {
        return StrTotalAmnt;
    }

    public static void setTotalAmnt(double _TotalAmnt) {
        StrTotalAmnt = _TotalAmnt;
    }



    public static String getGroupCode() {
        return StrGroupCode;
    }

    public static void setGroupCode(String _GroupData) {
        StrGroupCode = _GroupData;
    }


}
