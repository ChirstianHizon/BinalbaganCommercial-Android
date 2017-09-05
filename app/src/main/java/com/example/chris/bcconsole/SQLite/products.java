package com.example.chris.bcconsole.SQLite;

/**
 * Created by chris on 28/08/2017.
 */

public class products {

    //   ------  ID ----------------
    private int id;

//    ------  DESCRIPTION ------

    private String name, desc, datestamp, timestamp, image;
    private int category;

//    ------  LEVELS  -----------

    private int status, level, optimal, warning;

//    ------  GET AND  SET --------
public products() {

}

    public products(int _id, String _name, String _desc, String _datestamp, String _timestamp,
                    int _level, int _optimal, int _warning,
                    String _image, int _category, int _status) {

        this.id = _id;
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

    public products(String _name, String _desc, String _datestamp, String _timestamp,
                    int _level, int _optimal, int _warning,
                    String _image, int _category, int _status) {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
}
