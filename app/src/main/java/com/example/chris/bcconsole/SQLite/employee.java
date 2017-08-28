package com.example.chris.bcconsole.SQLite;

/**
 * Created by chris on 28/08/2017.
 */

public class employee {

    //    -------  ID  -------------
    private int id;

    //    ------  DETAILS  ---------
    private String username, firstname, lastname, datestamp, timestamp, image;
    private int type;

//    ------  GETTERS AND SETTERS  --------

    public employee() {

    }

    public employee(int _id, String _username, String _firstname, String _lastname, String _image) {
        this.id = _id;
        this.username = _username;
        this.firstname = _firstname;
        this.lastname = _lastname;
        this.image = _image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
