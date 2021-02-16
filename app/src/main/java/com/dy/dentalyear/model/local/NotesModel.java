package com.dy.dentalyear.model.local;

import java.io.Serializable;

public class NotesModel implements Serializable {
    private int id, type;
    private String title, desc, date;

    public NotesModel(int id, String title, String desc, String date, int type) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }

    public NotesModel() {
        this.title = "";
        this.desc = "";
        this.date = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
