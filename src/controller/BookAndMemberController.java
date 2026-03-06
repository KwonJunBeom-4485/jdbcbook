package controller;

import java.sql.Timestamp;
import java.util.List;

import dto.BookDTO;
import dto.MemberDTO;
import dto.RentalDTO;
import repository.BooksDAOImpl;
import repository.MembersDAOImpl;

public class BookAndMemberController {
    private MembersDAOImpl memberService = new MembersDAOImpl();
    private BooksDAOImpl bookService = new BooksDAOImpl();

    // 여기는 Members 관련

    public boolean join(String userId, String userPw, String userName, int age, String email, String phone,
            String address) {

        MemberDTO memberDTO = MemberDTO.builder()
                .memId(userId)
                .memPw(userPw)
                .name(userName)
                .age(age)
                .email(email)
                .phone(phone)
                .address(address)
                .build();

        return memberService.memberAdd(memberDTO);
    }

    public MemberDTO memberInfo(String userId) {
        return memberService.memberSearch(userId);
    }

    public boolean memberSeachEmail(String email) {
        boolean yn = false; // 중복 데이터 없음

        if (memberService.memberSearchEmail(email) != null) // 중복 데이터가 있다면
            yn = true; // true ( = 중복이라 못만듬)

        return yn;
    }

    public boolean memberSeachPhone(String phone) {
        boolean yn = false; // 중복 데이터 없음

        if (memberService.memberSearchPhone(phone) != null) // 중복 데이터가 있다면
            yn = true; // true ( = 중복이라 못만듬)

        return yn;
    }

    public boolean memberModify(long id, String userId, String userPw, String userName, int age, String email,
            String phone, String address, String beforeMemberId) {

        MemberDTO memberDTO = MemberDTO.builder()
                .id(id)
                .memId(userId)
                .memPw(userPw)
                .name(userName)
                .age(age)
                .email(email)
                .phone(phone)
                .address(address)
                .build();

        return memberService.memberMod(memberDTO, beforeMemberId);
    }

    public boolean removeUser(MemberDTO member) {
        MemberDTO memberDTO = memberService.memberSearch(member.getMemId());

        if (member.getMemPw().equals(memberDTO.getMemPw()))
            return memberService.memberDel(memberDTO);

        return false;
    }

    public MemberDTO login(String userId, String userPw) {
        return memberService.login(userId, userPw);
    }

    // 여기는 Books 관련
    public boolean registerBook(String title, String publish, String author, Timestamp publicDate, int stock) {
        String bookId;

        if (title.isEmpty() || title.isBlank()) {
            System.out.println("도서 등록 불가!\n제목이 비어있습니다.\n");
            return false;
        }

        if (stock < 0) {
            System.out.println("도서 등록 불가!\n도서 수량은 최소 0개 이상입니다!\n");
            return false;
        }

        while (true) {
            int isbnNum = (int) (Math.random() * 100000);
            bookId = "ISBN" + String.format("%05d", isbnNum);

            // ISBN 중복 검사
            if (bookService.bookSearch(bookId))
                break;
        }

        BookDTO bookDTO = BookDTO.builder()
                .bookId(bookId)
                .title(title)
                .publish(publish)
                .author(author)
                .publicDate(publicDate)
                .stock(stock)
                .build();

        return bookService.bookAdd(bookDTO); // 들어가면 true, 아니면 false
    }

    public List<BookDTO> bookAll() {
        // 조회했을 때 비어있으면 리스트말고, 걍 없다고 출력.
        return bookService.bookAll();
    }

    public boolean removeBook(String bookId) {

        // 실제 데이터에 bookId 값이 존재하는지 체크(없으면 true, 있으면 false)
        if (!bookService.bookSearch(bookId))
            return bookService.bookDel(bookId);

        return false;
    }

    public boolean modifyBook(BookDTO book, String bookId) {
        return bookService.bookMod(book, bookId);
    }

    // 여기는 Rental 관련
    public boolean rentalBook(String memId, String bookId) {
        boolean result = false;

        // 가져온 bookId로 BookDTO 객체 정보 불러오기
        BookDTO bookInfo = bookService.bookSeachInfo(bookId);
        if (bookInfo == null)
            return false;

        // 해당 북의 수량이 1 미만이라면 대여 불가
        if (bookInfo.getStock() < 1) {
            System.out.println("도서 수량이 부족합니다!");
            return false;
        }

        // 둘 중 하나라도 검색 되지 않는다면 = 없다면 false (bookSearch는 없으면 true)
        // if (!memberService.checkedByMemId(memId) ||
        // bookService.bookSearch(bookInfo.getBookId()))
        // return false;

        // 대여 테이블에 넣을 정보 입력 (null이나 0은 어짜피 rentalBookAdd() 에서 다루지 않는 데이터)
        RentalDTO rentalDTO = new RentalDTO(0, bookInfo.getBookId(), memId, null, null, null);

        // 대여 추가 성공 시 수량이 1 감소된 BookDTO 객체를 생성해 정보를 수정한다.(그냥 setter로 하면 안됨? -> 안돼 하지마)
        BookDTO bookAfterInfo = BookDTO.builder()
                .id(bookInfo.getId())
                .bookId(bookInfo.getBookId())
                .title(bookInfo.getTitle())
                .publish(bookInfo.getPublish())
                .author(bookInfo.getAuthor())
                .publicDate(bookInfo.getPublicDate())
                .stock(bookInfo.getStock() - 1)
                .build();

        // 수정 성공 시, 최종적으로 작업 성공 = true
        if (bookService.rentalBookAdd(rentalDTO) && bookService.bookMod(bookAfterInfo, bookId))
            result = true;

        return result;
    }

    public boolean returnBook(String bookId, String memId) {

        boolean result = false;
        BookDTO bookInfo = bookService.bookSeachInfo(bookId);

        if (bookInfo != null) {

            BookDTO incrementBook = BookDTO.builder()
                    .id(bookInfo.getId())
                    .bookId(bookInfo.getBookId())
                    .title(bookInfo.getTitle())
                    .publish(bookInfo.getPublish())
                    .author(bookInfo.getAuthor())
                    .publicDate(bookInfo.getPublicDate())
                    .stock(bookInfo.getStock() + 1) // 반납하면 재고가 늘어나도록
                    .build();

            if (bookService.rentalMod(bookId, memId) && bookService.bookMod(incrementBook, bookId))
                result = true;
        }

        return result;
    }

    public BookDTO bookSeach(String bookId) {
        return bookService.bookSeachInfo(bookId);
    }

    public List<RentalDTO> rentalCheck(String memId) {
        return bookService.rentalSearch(memId);
    }

    public List<RentalDTO> onlyRented(String memId) {
        return bookService.onlyRented(memId);
    }
}
