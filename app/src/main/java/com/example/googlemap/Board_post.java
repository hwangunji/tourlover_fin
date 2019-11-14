package com.example.googlemap;

import android.content.Intent;
import android.view.View;

import java.util.Date;

import static android.support.v4.content.ContextCompat.startActivity;

public class Board_post {
    private String id;//게시판 id
    private String title;
    private String content;
    private String name;//user의비밀번호
    private Date dateExample;//게시글 시간
    private String thema;//공사,자유,건의


    //새성자생산시작
    public Board_post(){
    }

    public Board_post(String id, String title, String content, String name,Date dateExample ) {
        this.id = id;
        this.title = title;//건물이름
        this.content = content;
        this.name = name;//비번
        this.dateExample = dateExample;//비번

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateExample() {
        return dateExample;
    }

    public void setDateExample(Date dateExample) {
        this.dateExample = dateExample;
    }



    @Override

    public String toString() {
        return "Board_post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", name='" + name + '\'' +
                ", dateExample='" + dateExample + '\'' +
//                ", thema='" + thema + '\'' +
                '}';
    }
    //새성자생산끝


}
