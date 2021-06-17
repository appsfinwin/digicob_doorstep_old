package com.finwingway.digicob.loanModule;

public class ListviewModel {

    private int sNo;
    private String product;
    private String category;
    private String price;

    public ListviewModel(int sNo, String product, String category, String price) {
        this.sNo = sNo;
        this.product = product;
        this.category = category;
        this.price = price;
    }

    public int getsNo() {
        return sNo;
    }

    public String getProduct() {
        return product;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

}
