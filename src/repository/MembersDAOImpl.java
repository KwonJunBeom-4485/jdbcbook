package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dbutil.DButil;
import dto.MemberDTO;

public class MembersDAOImpl implements Members {

    @Override
    public Optional<MemberDTO> login(String memId, String memPw) {

        Optional<MemberDTO> member = null;

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from members where memId = ? and memPw = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);
            pstmt.setString(2, memPw);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                member = Optional.of(MemberDTO.builder()
                        .id(rs.getLong("id"))
                        .memId(rs.getString("memId"))
                        .memPw(rs.getString("memPw"))
                        .name(rs.getString("name"))
                        .age(rs.getInt("age"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .address(rs.getString("address"))
                        .regDate(rs.getTimestamp("regDate"))
                        .modifyDate(rs.getTimestamp("modifyDate"))
                        .build());
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return member; // 반환된 유저 정보를 가지고 일치하면 로그인 정보를 따로 저장하고 아니면 로그인 실패를 시키면 된다.
    }

    @Override
    public boolean memberAdd(MemberDTO member) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "insert into members(memId, memPw, name, age, email, phone, address) values(?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, member.getMemId());
            pstmt.setString(2, member.getMemPw());
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, member.getPhone());
            pstmt.setString(7, member.getAddress());

            if (pstmt.executeUpdate() != 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    // 자동 생성된 1번 컬럼 = id 값 가져오기
                    long generatedId = rs.getLong(1);
                    member.setId(generatedId); // 이미 매개변수로 가져온 memberDTO 타입의 member에는 id 값이 없기 때문에 여기서 저장
                }

                result = true;
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<MemberDTO> memberAll() {
        List<MemberDTO> list = new ArrayList<>();

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from members";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(MemberDTO.builder()
                        .id(rs.getLong("id"))
                        .memId(rs.getString("memId"))
                        .memPw(rs.getString("memPw")) // 일단 받아만 놓고 출력할 때 안할 예정
                        .name(rs.getString("name"))
                        .age(rs.getInt("age"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .address(rs.getString("address"))
                        .regDate(rs.getTimestamp("regDate"))
                        .modifyDate(rs.getTimestamp("modifyDate"))
                        .build());
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return list;
    }

    @Override
    public boolean memberDel(MemberDTO member) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "delete from members where memId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getMemId());

            if (pstmt.executeUpdate() != 0)
                result = true;

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean memberMod(MemberDTO member, String memId) {
        // 로그인 후 내가 어떤 유저인지 저장하고, 유저 정보 조회, 수정, 삭제 시 해당 데이터를 그냥 갖고와서 처리하면 된다.
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {

            // App.java에서 수정 데이터 입력할 때 미리 중복체크 하고 여기로 와야함. (안하면 memId, email, phone 같이
            // unique한 값 때문에 오류 날 수 있음)
            // regDate는 계정 생성일이기 때문에 바뀌지 않는다.
            String sql = "update members set memId = ?, memPw = ?, name = ?, age = ?, email = ?, phone = ?, address = ?, modifyDate = ? where memId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, member.getMemId());
            pstmt.setString(2, member.getMemPw());
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, member.getPhone());
            pstmt.setString(7, member.getAddress());
            // 로그인 한 유저 데이터 저장해올거라 괜찮긴 한데, 가능한 안정적이게 바로 넣어주자.(저장 안하고 대충 줘버리면 null이 나올 수 있음)
            pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, memId);

            if (pstmt.executeUpdate() != 0)
                result = true;

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public Optional<MemberDTO> memberSearch(String memId) {
        Optional<MemberDTO> result = null;

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from members where memId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                result = Optional.of(MemberDTO.builder()
                        .id(rs.getLong("id"))
                        .memId(rs.getString("memId"))
                        .memPw(rs.getString("memPw"))
                        .name(rs.getString("name"))
                        .age(rs.getInt("age"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .address(rs.getString("address"))
                        .regDate(rs.getTimestamp("regDate"))
                        .modifyDate(rs.getTimestamp("modifyDate"))
                        .build());
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패!!");
            System.out.println(e.getMessage());
        }

        return result;
    }

}
