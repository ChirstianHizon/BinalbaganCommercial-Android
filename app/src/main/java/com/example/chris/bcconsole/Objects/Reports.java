package com.example.chris.bcconsole.Objects;

/**
 * Created by chris on 01/10/2017.
 */

public class Reports {

    private String id,prdname,datestamp,type,employee,logqty,total,supplier,qty;

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
}
