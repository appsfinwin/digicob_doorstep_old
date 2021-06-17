package com.finwingway.digicob.loanModule.JLGSplitSupportClasses;

public class JLGTransRcylrAdptrModelSplit {

    private String CustId;
    private String CustName;
    private String AccountNo;
    private String PrincipalBal;
    private String PrincipalDue;
    private String Interest;
    private String PenalInterest;
    private String TotalInterest;
    private String tRemittance;
    private boolean isSelected;

    public JLGTransRcylrAdptrModelSplit(String _CustId, String _CustName, String _AccountNo, String _PrincipalBal, String _PrincipalDue, String _Interest,
                                        String _PenalInterest, String _TotalInterest, String _tRemittance) {
        this.CustId = _CustId;
        this.CustName = _CustName;
        this.AccountNo = _AccountNo;
        this.PrincipalBal = _PrincipalBal;
        this.PrincipalDue = _PrincipalDue;
        this.Interest = _Interest;
        this.PenalInterest = _PenalInterest;
        this.TotalInterest = _TotalInterest;
        this.tRemittance = _tRemittance;
    }

    public boolean getSplitSelected() {
        return isSelected;
    }

    public void setSplitSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSplitCustId() {
        return CustId;
    }

    public String getSplitCustName() {
        return CustName;
    }

    public String getSplitAccountNo() {
        return AccountNo;
    }

    public String getSplitPrincipalBlnc() {
        return PrincipalBal;
    }

    public String getSplitPrincipalDue() {
        return PrincipalDue;
    }

    public String getSplitInterest() {
        return Interest;
    }

    public String getSplitPenalInterest() {
        return PenalInterest;
    }

    public String getSplitTotalInterest() {
        return TotalInterest;
    }

    public String getSplitRemittance() {
        return tRemittance;
    }

    public void setSplitRemittance(String _tRemittance) {
         tRemittance = _tRemittance;
    }


}
