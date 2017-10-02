package com.example.chris.bcconsole.Objects;

/**
 * Created by chris on 02/10/2017.
 */

public class Orders {
    private String id, total,datestamp,customer,status,qty;

    public Orders(String id,String total,String datestamp,String customer,String status,String qty){
        this.id = id;
        this.total = total;
        this.datestamp = datestamp;
        this.customer = customer;
        this.status = status;
        this.qty = qty;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
