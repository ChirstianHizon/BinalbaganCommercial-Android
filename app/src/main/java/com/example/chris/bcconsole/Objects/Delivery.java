package com.example.chris.bcconsole.Objects;

/**
 * Created by chris on 20/09/2017.
 */

public class Delivery {

    private String id,status,type,datestamp,timestamp,recieve,custid,custname;

    public Delivery (String _id,String _custname,String _datestamp,String _status) {
        this.id = _id;
        this.custname = _custname;
        this.datestamp = _datestamp;
        this.status = _status;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecieve() {
        return recieve;
    }

    public void setRecieve(String recieve) {
        this.recieve = recieve;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }
}