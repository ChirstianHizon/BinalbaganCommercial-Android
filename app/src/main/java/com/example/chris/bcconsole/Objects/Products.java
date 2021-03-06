package com.example.chris.bcconsole.Objects;

/**
 * Created by chris on 28/08/2017.
 */

public class Products {


//    ------  DESCRIPTION ------

    private String id,name, desc, datestamp, timestamp, image, category, status, price,qty;

//    ------  LEVELS  -----------

    private int level, optimal, warning;

//    ------  GET AND  SET --------


    public Products(String _id, String _name, String _price, String _qty) {
        this.id = _id;
        this.name = _name;
        this.price = _price;
        this.qty = _qty;
    }

    public Products(String _id, String _name, String _price, String _qty ,String _status) {
        this.id = _id;
        this.name = _name;
        this.price = _price;
        this.qty = _qty;
        this.status = _status;
    }

    public Products(String _id, String _name, String _desc, String _datestamp, String _timestamp,
                    String _price, int _level, int _optimal, int _warning,
                    String _image, String _category, String _status) {

        this.id = _id;
        this.name = _name;
        this.desc = _desc;
        this.datestamp = _datestamp;
        this.timestamp = _timestamp;
        this.image = _image;
        this.price = _price;
        this.level = _level;
        this.optimal = _optimal;
        this.warning = _warning;
        this.category = _category;
        this.status = _status;
    }

    public Products(String _id, String _name, String _category, String _status,int x) {

        this.id = _id;
        this.name = _name;
        this.category = _category;
        this.status = _status;
    }

    public Products(String _name, String _desc, String _datestamp, String _timestamp,
                    int _level, int _optimal, int _warning,
                    String _image, String _category, String _status) {

        this.name = _name;
        this.desc = _desc;
        this.datestamp = _datestamp;
        this.timestamp = _timestamp;
        this.image = _image;
        this.level = _level;
        this.optimal = _optimal;
        this.warning = _warning;
        this.category = _category;
        this.status = _status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() { return desc; }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOptimal() {
        return optimal;
    }

    public void setOptimal(int optimal) {
        this.optimal = optimal;
    }

    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
