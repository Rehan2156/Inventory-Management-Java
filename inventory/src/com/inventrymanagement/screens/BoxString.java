package com.inventrymanagement.screens;

public class BoxString {
    public int id;
    public String name;

    public BoxString(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
