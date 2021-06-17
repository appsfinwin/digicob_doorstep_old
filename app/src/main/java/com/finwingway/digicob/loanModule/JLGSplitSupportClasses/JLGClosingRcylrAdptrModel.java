package com.finwingway.digicob.loanModule.JLGSplitSupportClasses;

public class JLGClosingRcylrAdptrModel {

    private String CustId;
    private String CustName;
    private String PrincipalBal;
    private String Interest;
    private String PenalInterest;
    private String TotalInterest;
    private String tRemittance;
    private boolean isSelected;

    public JLGClosingRcylrAdptrModel(String _CustId, String _CustName, String _PrincipalBal, String _Interest,
                                        String _PenalInterest, String _TotalInterest, String _tRemittance) {
        this.CustId = _CustId;
        this.CustName = _CustName;
        this.PrincipalBal = _PrincipalBal;
        this.Interest = _Interest;
        this.PenalInterest = _PenalInterest;
        this.TotalInterest = _TotalInterest;
        this.tRemittance = _tRemittance;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCustId() {
        return CustId;
    }

    public String getCustName() {
        return CustName;
    }

    public String getPrincipalBlnc() {
        return PrincipalBal;
    }

    public String getInterest() {
        return Interest;
    }

    public String getPenalInterest() {
        return PenalInterest;
    }

    public String getTotalInterest() {
        return TotalInterest;
    }

    public String getRemittance() {
        return tRemittance;
    }


}