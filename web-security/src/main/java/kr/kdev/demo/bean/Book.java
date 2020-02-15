package kr.kdev.demo.bean;

import lombok.Data;

@Data
public class Book {
    private String bookId;
    private String title;
    private String content;
    private String author;
    private String authorId;
}
