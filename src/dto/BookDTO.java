package dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private long id;
    private String bookId;
    private String title;
    private String publish;
    private String author;
    private Timestamp publicDate;
    private int stock;
}
