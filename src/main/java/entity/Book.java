package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {

    private Author author;
    private int numberOfPages;
    private String title;
    private String publisher;
}
