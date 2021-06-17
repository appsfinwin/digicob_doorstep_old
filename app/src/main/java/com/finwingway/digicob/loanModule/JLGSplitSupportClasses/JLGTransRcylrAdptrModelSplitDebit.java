package com.finwingway.digicob.loanModule.JLGSplitSupportClasses;

public class JLGTransRcylrAdptrModelSplitDebit {

    private String AccountNo;
    private String CustId;
    private String CustName;
    private String Amount;
    private boolean isSelected;

    public JLGTransRcylrAdptrModelSplitDebit(String _AccountNo, String _CustId, String _CustName, String _Amount) {
        this.AccountNo = _AccountNo;
        this.CustId = _CustId;
        this.CustName = _CustName;
        this.Amount = _Amount;
    }

    public boolean getSplitDebitSelected() {
        return isSelected;
    }

    public void setSplitDebitSelected(boolean selected) {
        isSelected = selected;
    }


    public String getSplitDebitAccountNo() {
        return AccountNo;
    }

    public String getSplitDebitCustId() {
        return CustId;
    }

    public String getSplitDebitCustName() {
        return CustName;
    }

    public String getSplitDebitAmount() {
        return Amount;
    }

    public void setSplitDebitAmount(String _amount) {
        Amount = _amount;
    }


}
