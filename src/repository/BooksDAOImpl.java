package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dbutil.DButil;
import dto.BookDTO;
import dto.RentalDTO;

public class BooksDAOImpl implements Books {

    @Override
    public boolean bookAdd(BookDTO bookDTO) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "insert into Books(bookId, title, publish, author, publicDate, stock) values(?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, bookDTO.getBookId());
            pstmt.setString(2, bookDTO.getTitle());
            pstmt.setString(3, bookDTO.getPublish());
            pstmt.setString(4, bookDTO.getAuthor());
            pstmt.setTimestamp(5, bookDTO.getPublicDate());
            pstmt.setInt(6, bookDTO.getStock());

            if (pstmt.executeUpdate() != 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    // DB 내부에서 자동 생성된 id값 가져와서 이쪽 데이터에도 반영하기
                    long generatedId = rs.getLong(1);
                    bookDTO.setId(generatedId);
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
    public List<BookDTO> bookAll() {
        List<BookDTO> bookList = new ArrayList<>();

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from Books";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                bookList.add(BookDTO.builder()
                        .id(rs.getLong("id"))
                        .bookId(rs.getString("bookId"))
                        .title(rs.getString("title"))
                        .publish(rs.getString("publish"))
                        .author(rs.getString("author"))
                        .publicDate(rs.getTimestamp("publicDate"))
                        .stock(rs.getInt("stock"))
                        .build());
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return bookList;
    }

    // 중복 확인용 메서드
    public boolean bookSearch(String bookId) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from Books where bookId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookId);

            ResultSet rs = pstmt.executeQuery();

            // 결과(중복) 없으면 true 반환 (ResultSet 객체는 null이 아니라 == null 이렇게 비교x)
            if (!rs.next())
                result = true;

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public BookDTO bookSeachInfo(String bookId) {
        BookDTO list = null;

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from Books where bookId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                list = BookDTO.builder()
                        .id(rs.getLong("id"))
                        .bookId(rs.getString("bookId"))
                        .title(rs.getString("title"))
                        .publish(rs.getString("publish"))
                        .author(rs.getString("author"))
                        .publicDate(rs.getTimestamp("publicDate"))
                        .stock(rs.getInt("stock"))
                        .build();
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return list;
    }

    @Override
    public boolean bookDel(String bookId) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "delete from Books where bookId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookId);

            if (pstmt.executeUpdate() != 0)
                result = true;

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean bookMod(BookDTO bookDTO, String bookId) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "update Books set bookId = ?, title = ?, publish = ?, author = ?, publicDate = ?, stock = ? where bookId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, bookDTO.getBookId());
            pstmt.setString(2, bookDTO.getTitle());
            pstmt.setString(3, bookDTO.getPublish());
            pstmt.setString(4, bookDTO.getAuthor());
            pstmt.setTimestamp(5, bookDTO.getPublicDate());
            pstmt.setInt(6, bookDTO.getStock());
            pstmt.setString(7, bookId); // 기존 bookId를 매개변수로 따로 받아 수정

            if (pstmt.executeUpdate() != 0)
                result = true;

        } catch (Exception e) {
            System.out.println("DB 작업 실패");
            System.out.println(e.getMessage());
        }

        return result;
    }

    // 기왕이면 빌린 상태에서 똑같은 사람이 똑같은 책을 빌릴 수 없도록 조회하는 코드가 있으면 좋을 듯 하다.
    @Override
    public boolean rentalBookAdd(RentalDTO rentalDTO) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "insert into Rental(bookId, memId) values(?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, rentalDTO.getRentalBookId());
            pstmt.setString(2, rentalDTO.getRentalMemId());

            if (pstmt.executeUpdate() != 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    rentalDTO.setId(generatedId);
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
    public List<RentalDTO> rentalSearch(String memId) {
        List<RentalDTO> list = new ArrayList<>();

        try (Connection conn = DButil.getConnection()) {
            String sql = "select * from Rental where memId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, memId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(RentalDTO.builder()
                        .id(rs.getLong("id"))
                        .rentalBookId(rs.getString("bookId"))
                        .rentalMemId(rs.getString("memId"))
                        .rentalDate(rs.getTimestamp("rentalDate"))
                        .returnDate(rs.getTimestamp("returnDate"))
                        .status(rs.getString("status"))
                        .build());
            }

        } catch (Exception e) {
            System.out.println("DB 작업 실패!!");
            System.out.println(e.getMessage());
        }

        return list;
    }

    @Override
    public boolean rentalMod(RentalDTO rentalDTO) {
        boolean result = false;

        try (Connection conn = DButil.getConnection()) {
            String sql = "update Rental set returnDate = ?, status = ? where id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(2, "Returned");
            pstmt.setLong(3, rentalDTO.getId());
            if (pstmt.executeUpdate() != 0)
                result = true;

        } catch (Exception e) {
            System.out.println("DB 작업 실패!!");
            System.out.println(e.getMessage());
        }

        return result;
    }

}