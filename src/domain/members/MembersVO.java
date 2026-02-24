package domain.members;

import java.sql.Timestamp;


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
