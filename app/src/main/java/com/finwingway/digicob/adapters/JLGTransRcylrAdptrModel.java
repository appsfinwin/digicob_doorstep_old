package com.finwingway.digicob.adapters;

public class JLGTransRcylrAdptrModel {

    private String CustId;
    private String CustName;
    private String Principal;
    private String Interest;
    private String PenalInterest;
    private String TotalInterest;
    private String tRemittance;
    private boolean isSelected;

    public JLGTransRcylrAdptrModel(String _CustId, String _CustName, String _Principal, String _Interest,
                                   String _PenalInterest, String _TotalInterest, String _tRemittance) {
        this.CustId = _CustId;
        this.CustName = _CustName;
        this.Principal = _Principal;
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

    public String getPrincipal() {
        return Principal;
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

    public void setRemittance(String _tRemittance) {
        tRemittance = _tRemittance;
    }

}

