package com.example.chris.bcconsole.Objects;

/**
 * Created by chris on 01/10/2017.
 */

public class Reports {

    private String id,prdname,datestamp,type,employee,logqty,total,supplier,qty,user,orderid,status,price;

    public Reports (String id,String prdname,String datestamp,String type,String employee,
                    String logqty,String total,String supplier){
        this.id = id;
        this.prdname = prdname;
        this.datestamp = datestamp;
        this.type = type;
        this.employee = employee;
        this.logqty = logqty;
        this.total = total;
        this.supplier = supplier;

    }

    public Reports(String id,String datestamp,String user,String type,String qty,String total){
        this.id = id;
        this.datestamp = datestamp;
        this.user = user;
        this.type = type;
        this.qty = qty;
        this.total = total;
    }

    public Reports(String id,String orderid,String datestamp,String user,String status,String qty,String price){

        this.id = id;
        this.datestamp = datestamp;
        this.user = user;
        this.qty = qty;
        this.orderid = orderid;
        this.status = status;
        this.price = price;
    }
    public String getPrdname() {
        return prdname;
    }

    public void setPrdname(String prdname) {
        this.prdname = prdname;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getLogqty() {
        return logqty;
    }

    public void setLogqty(String logqty) {
        this.logqty = logqty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
