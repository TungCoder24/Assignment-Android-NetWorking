package tungndph27567.fpoly.assignment_androidnetworiking.models;

import java.io.Serializable;

public class Comment implements Serializable {
    private String id;
    private String id_mangar;
    private String username;
    private String content;
    private String date_time;
    private String image_user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_mangar() {
        return id_mangar;
    }

    public void setId_mangar(String id_mangar) {
        this.id_mangar = id_mangar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }
}
