package com.example.testappsfuntash.Model;

public class Model_Adapter
{
    String text;
    String images;

    public Model_Adapter(String text, String images) {
        this.text = text;
        this.images = images;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
