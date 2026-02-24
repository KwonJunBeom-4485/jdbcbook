package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DButil {
    // 도구 : Connection 객체를 반환하는 도구 생성...
    // method 이름 : getConnection()

    // 멤버 변수
    private static final String url = "jdbc:mysql://localhost:3306/jdbc";
    public static final String ORACLE = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
    public static final String MARIADB = "jdbc:mariadb://localhost:4306/jdbc";
    private static final String sql_user = "jdbcuser";
    private static final String password = "jdbcuser";

    // 드라이버 로드 확인
    // static {} : 은 한번 실행하고 두 번 실행 안함.
    static {
        try {
            // 사실 요즘은 DriverManager.~~ 하면서 자동으로 드라이버를 잡아주기 때문에 굳이 설정 안해도 ok.
            
            // Class.forName("com.mysql.cj.jdbc.Driver");  // mysql
            // Class.forName("oracle.jdbc.OracleDriver");  // oracle
            Class.forName("org.mariadb.jdbc.Driver");   // mariadb
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로드 실패!");
            System.out.println(e.getMessage());
        }
    }
    

    // 메서드 생성
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, sql_user, password);
    }

    // url을 통한 연결 connection
    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url, sql_user, password);
    }
}
