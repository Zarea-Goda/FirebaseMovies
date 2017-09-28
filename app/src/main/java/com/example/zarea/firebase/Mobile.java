package com.example.zarea.firebase;

/**
 * Created by Zarea on 27/09/2017.
 */

public class Mobile
{
    private String id;

    private String image_title;

    private String image_url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getImage_title ()
    {
        return image_title;
    }

    public void setImage_title (String image_title)
    {
        this.image_title = image_title;
    }

    public String getImage_url ()
    {
        return image_url;
    }

    public void setImage_url (String image_url)
    {
        this.image_url = image_url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", image_title = "+image_title+", image_url = "+image_url+"]";
    }
}