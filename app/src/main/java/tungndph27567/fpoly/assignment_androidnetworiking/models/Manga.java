package tungndph27567.fpoly.assignment_androidnetworiking.models;

import java.io.Serializable;
import java.util.List;

public class Manga implements Serializable {
    private String id;
    private String name;
    private String author;
    private String date;
    private String describe;
    private String img_cover;
    private List<String> listImg_content;
    private int numberRead;

    public Manga() {
    }

    public Manga(String id, String name, String author, String date, String describe, String img_cover, List<String> listImg_content, int numberRead) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.date = date;
        this.describe = describe;
        this.img_cover = img_cover;
        this.listImg_content = listImg_content;
        this.numberRead = numberRead;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImg_cover() {
        return img_cover;
    }

    public void setImg_cover(String img_cover) {
        this.img_cover = img_cover;
    }

    public List<String> getListImg_content() {
        return listImg_content;
    }

    public void setListImg_content(List<String> listImg_content) {
        this.listImg_content = listImg_content;
    }

    public int getNumberRead() {
        return numberRead;
    }

    public void setNumberRead(int numberRead) {
        this.numberRead = numberRead;
    }
}
