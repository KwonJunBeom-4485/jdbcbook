package dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private long id;
    private String rentalBookId;
    private String rentalMemId;
    private Timestamp rentalDate;
    private Timestamp returnDate;
    private String status;    
}
