package repository;

import java.util.List;

import dto.BookDTO;
import dto.RentalDTO;

public interface Books {
    boolean bookAdd(BookDTO bookDTO);
    boolean bookMod(BookDTO bookDTO, String bookId);
    boolean bookDel(String bookId);
    List<BookDTO> bookAll();
    BookDTO bookSeachInfo(String bookId);

    boolean rentalBookAdd(RentalDTO rentalDTO);
    List<RentalDTO> rentalSearch(String memId);   // 로그인한 유저 아이디를 기준으로 조회
    boolean rentalMod(String bookId, String memId);
    // rental 테이블은 cascade로 종속성 관련 설정을 해서,
    // 부모 테이블이 수정/삭제 될 때 같이 적용 되기 때문에 따로 수정/삭제 메서드 필요x
}
