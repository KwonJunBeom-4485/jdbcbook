package domain.members;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "memPw")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembersVO {
    private long id;
    private String memId;
    private String memPw;
    private String name;
    private int age;
    private String email;
    private String phone;
    private String address;
    private Timestamp regDate;
    private Timestamp modifyDate;
}
