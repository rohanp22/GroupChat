package com.wielabs.groupchat;

public class Post {

    private String id,cid,title,description,infoline,image_url;

    public Post(String id,String cid,String title,String description,String infoLine,String image_url){
        this.id = id;
        this.cid = cid;
        this.title = title;
        this.description = description;
        this.infoline = infoLine;
        this.image_url = image_url;
    }

    public String getCid() {
        return cid;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public String getInfoline() {
        return infoline;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }
}
