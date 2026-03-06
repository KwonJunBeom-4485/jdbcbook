import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import controller.BookAndMemberController;
import dto.BookDTO;
import dto.MemberDTO;
import dto.RentalDTO;

public class App {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static Scanner scanner = new Scanner(System.in, "cp949"); // 이상하게 한글이 안써지네..
    private static boolean flag = false;
    private static BookAndMemberController controller = new BookAndMemberController();
    private static MemberDTO loginMember = null; // 로그인 한 유저 정보를 담는 변수

    public static void main(String[] args) throws Exception {
        System.out.println("[도서 관리 시스템]");
        menu();
    }

    public static void menu() {
        while (true) {
            System.out.println("1. 회원 관리");
            System.out.println("2. 도서 관리");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택 : ");

            String choice = scanner.nextLine();
            System.out.println("===".repeat(15));

            switch (choice) {
                case "1":
                    if (loginMember != null) {
                        userManage();
                    } else {
                        userManageMenu();
                    }
                    break;
                case "2":
                    bookManageMenu();
                    break;
                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("메뉴를 잘못 입력했습니다.\n");
                    break;
            }
        }
    }

    public static void userManageMenu() {
        while (true) {
            System.out.println("1) 로그인");
            System.out.println("2) 회원가입");
            if (loginMember != null) {
                System.out.println("3) 회원 메뉴");
            }
            System.out.println("0) 메인으로 이동");
            System.out.print("메뉴 선택 : ");

            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1":
                    // 로그인 처리 메서드 호출
                    if (loginMember != null) {
                        System.out.println("이미 로그인 상태입니다.\n");
                        userManage();
                        break;
                    }
                    signIn();
                    break;
                case "2":
                    // 회원 가입 정보 처리 메서드 호출
                    signUp();
                    break;
                case "3":
                    if (loginMember == null) {
                        System.out.println("메뉴를 잘못 입력했습니다.\n");
                        break;
                    }

                    userManage();
                    break;
                case "0":
                    System.out.println("메인으로 이동합니다.\n");
                    return;
                default:
                    System.out.println("메뉴를 잘못 입력했습니다.\n");
                    break;
            }
        }
    }

    public static void signUp() {
        while (true) {
            System.out.println("[ 회원 가입 정보 처리 ]");
            System.out.print("아이디 : ");
            String userId = scanner.next();
            System.out.print("비밀번호 : ");
            String userPw = scanner.next();
            scanner.nextLine(); // 버퍼 정리
            System.out.print("사용자 이름 : ");
            String userName = scanner.nextLine();
            System.out.print("나이 : ");
            int userAge;
            try {
                userAge = scanner.nextInt();
                scanner.nextLine(); // 버퍼 정리

            } catch (InputMismatchException e) { // 입력 타입 잘못되면
                scanner.nextLine(); // 버퍼 정리(try에서 하기 전에 catch로 오니까 써줘야 함)
                System.out.println("나이는 숫자(정수)만 입력해주세요!");
                break;
            }
            System.out.print("사용자 이메일 : ");
            String userEmail = scanner.next();
            scanner.nextLine(); // 버퍼 정리
            System.out.print("사용자 전화번호 : ");
            String userPhone = scanner.nextLine();
            System.out.print("사용자 주소 : ");
            String userAddress = scanner.nextLine();

            // 중복된 회원 정보가 있는 지 확인

            System.out.print("[ 입력한 정보를 확인 ]");
            System.out.println("사용자 ID : " + userId);
            System.out.println("사용자 PW : " + userPw);
            System.out.println("사용자 이름 : " + userName);
            System.out.println("사용자 이메일 : " + userEmail);
            System.out.println("사용자 전화번호 : " + userPhone);
            System.out.println("나이 : " + userAge);
            System.out.println("주소 : " + userAddress);

            while (true) {
                System.out.print("입력한 정보로 회원 가입을 하시겠습니까?(Y/N) ");
                char done = scanner.nextLine().toUpperCase().charAt(0);

                if (done == 'Y') {
                    // 회원 가입 처리
                    boolean status = controller.join(userId, userPw, userName, userAge,
                            userEmail, userPhone, userAddress);

                    // 회원 가입 메뉴 나가고, 실패하면 다시 while문으로
                    if (status) {
                        System.out.println();
                        return;
                    } else {
                        System.out.println();
                        System.out.println("회원 가입 실패\n");
                        break;
                    }

                } else if (done == 'N')
                    return;
            }
        }
    }

    public static void signIn() {
        if (loginMember != null) {
            System.out.println("이미 로그인 상태입니다.\n");
            return;
        }

        System.out.println("[ 로그인 ]");
        System.out.print("사용자 아이디 : ");
        String userId = scanner.next();
        System.out.print("사용자 패스워드 : ");
        String userPw = scanner.next();
        scanner.nextLine(); // 버퍼 정리

        System.out.print("로그인 하시겠습니까?(Y/N) ");
        String input = scanner.nextLine().toUpperCase();
        System.out.println();
        if (input.isEmpty() || input.charAt(0) == 'N')
            return;

        if (!userId.isEmpty() && !userPw.isEmpty()) {
            MemberDTO loginMem = controller.memberInfo(userId);

            if (loginMem != null && loginMem.getMemPw().equals(userPw)) {
                loginMember = loginMem;
                userManage(); // 관리 메뉴 진입
                return;
            } else {
                System.out.println("아이디 또는 패스워드가 일치하지 않습니다.\n");
            }
        }
    }

    public static void userManage() {
        while (true) {

            // controller를 통해서 정보를 입력
            System.out.println("1) 회원 정보 확인");
            System.out.println("2) 회원 정보 수정");
            System.out.println("3) 로그아웃");
            System.out.println("4) 회원 탈퇴");
            System.out.println("0) 이전 메뉴로 이동");
            System.out.print("메뉴 선택 : ");
            char choice = scanner.nextLine().charAt(0);

            System.out.println();

            switch (choice) {
                case '1':
                    // 회원 정보 출력
                    System.out.print("[ 회원 정보 출력 ]");
                    System.out.println("사용자 ID : " + loginMember.getMemId());
                    System.out.println("사용자 패스워드 : " + "*".repeat(loginMember.getMemPw().length()));
                    System.out.println("사용자 이름 : " + loginMember.getName());
                    System.out.println("사용자 이메일 : " + loginMember.getEmail());
                    System.out.println("사용자 전화번호 : " + loginMember.getPhone());
                    System.out.println("나이 : " + loginMember.getAge());
                    System.out.println("주소 : " + loginMember.getAddress());
                    System.out.println("회원가입 날짜 : " + loginMember.getRegDate());
                    System.out.println();
                    break;
                case '2':
                    // 회원 정보 출력 후 수정
                    System.out.println("[ 회원 정보 수정 ]");
                    System.out.printf("사용자 ID(%s) : ", loginMember.getMemId());
                    String userId = scanner.nextLine();
                    // 패스워드 수정은 별도의 로직으로 구성해야 합니다. (일단 임시방편으로 수정시켜버렷~~)
                    System.out.printf("사용자 PW(%s) : ", "*".repeat(loginMember.getMemPw().length()));
                    String userPw = scanner.next();
                    scanner.nextLine(); // 버퍼
                    System.out.printf("사용자 이름(%s) : ", loginMember.getName());
                    String userName = scanner.nextLine();
                    System.out.printf("사용자 이메일(%s) : ", loginMember.getEmail());
                    String userEmail = scanner.nextLine();
                    System.out.printf("사용자 전화번호(%s) : ", loginMember.getPhone());
                    String userPhone = scanner.nextLine();
                    System.out.printf("나이(%d) : ", loginMember.getAge());
                    int userAge = scanner.nextInt();
                    scanner.nextLine(); // 버퍼
                    System.out.printf("주소(%s) : ", loginMember.getAddress());
                    String userAddress = scanner.nextLine();

                    if (controller.memberInfo(userId) != null) {
                        System.out.println("아이디가 중복 되었습니다.");
                        return;
                    } else if (controller.memberSeachEmail(userEmail)) {
                        System.out.println("이메일이 중복 되었습니다.");
                        return;
                    } else if (controller.memberSeachPhone(userPhone)) {
                        System.out.println("이미 등록된 전화번호 입니다.");
                    }

                    boolean status = controller.memberModify(loginMember.getId(), userId, userPw, userName,
                            userAge, userEmail, userPhone, userAddress, loginMember.getMemId());

                    if (status) {
                        System.out.println("회원 정보가 수정 되었습니다.\n");
                        loginMember = controller.memberInfo(userId); // 회원 정보 갱신
                    } else {
                        System.out.println("회원 정보 수정 실패\n");
                    }

                    break;
                case '3':
                    loginMember = null;
                    System.out.println("로그아웃 처리 완료");
                    return;
                case '4':
                    // 회원 정보 출력 후 삭제
                    System.out.println("[ 회원 정보 삭제 및 확인 ]");
                    System.out.println("사용자 ID : " + loginMember.getMemId());
                    System.out.print("탈퇴하시겠습니까?(Y/N) ");
                    char done = scanner.nextLine().toUpperCase().charAt(0);

                    if (done == 'Y') {
                        System.out.print("사용자 PW : ");
                        String pw = scanner.next();
                        scanner.nextLine(); // 버퍼

                        // 회원 정보 넣어서 보낼 pw
                        // 회원 정보 삭제를 위한 확인 처리할 패스워드. 변경해서
                        MemberDTO userInfo = MemberDTO.builder().memId(loginMember.getMemId()).memPw(pw).build();

                        status = controller.removeUser(userInfo);
                        if (status) { // 회원 탈퇴
                            System.out.println("회원 탈퇴 완료");

                            // 1. loginMember 정리 => null
                            loginMember = null;

                        } else { // 회원 탈퇴 실패
                            System.out.println("회원 탈퇴 실패");
                            break;
                        }

                        System.out.println();
                        return;
                    }

                    System.out.println();
                    break;
                case '0':
                    flag = true;
                    return;
                default:
                    break;
            }
        }
    }

    public static void bookManageMenu() {
        while (true) {
            System.out.println("1) 도서 전체 조회");
            System.out.println("2) 도서 대여");
            System.out.println("3) 도서 반납");
            System.out.println("4) 신규 도서 등록");
            System.out.println("5) 도서 삭제(원래는 관리자만 있어야 하지만)");
            System.out.println("0) 메인으로 이동");
            System.out.print("메뉴 선택 : ");

            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1":
                    bookAllPrint();
                    break;
                case "2":
                    bookRental();
                    break;
                case "3":
                    bookReturn();
                    break;
                case "4":
                    bookRegister();
                    break;
                case "5":
                    bookRemove();
                    break;
                case "0":
                    System.out.println("메인으로 이동합니다.\n");
                    return;
                default:
                    System.out.println("메뉴를 잘못 입력했습니다.\n");
                    break;
            }
        }
    }

    public static void bookAllPrint() {
        List<BookDTO> bookList = controller.bookAll();

        if (bookList.isEmpty()) {
            System.out.println("등록된 도서가 없습니다.\n");
            return;
        }

        System.out.println("=====".repeat(20));
        System.out.printf("%s %s %s %s %s %s\n",
                padWithDots("book ID", 17),
                padWithDots("Title", 17),
                padWithDots("Author", 17),
                padWithDots("Publish", 17),
                padWithDots("PublicDate", 17),
                padWithDots("Stock", 17));
        System.out.println("=====".repeat(20));
        for (BookDTO bto : bookList) {
            System.out.printf("%s %s %s %s %s %d\n",
                    padWithDots(bto.getBookId(), 17),
                    padWithDots(bto.getTitle(), 17),
                    padWithDots(bto.getAuthor(), 17),
                    padWithDots(bto.getPublish(), 17),
                    padWithDots(dateFormat.format(bto.getPublicDate()), 17),
                    bto.getStock());
        }
        System.out.println("=====".repeat(20));
    }

    // 도서 출력 양식은 ai의 도움을 받아습니따
    public static String padWithDots(String s, int totalWidth) {
        if (s == null)
            s = "";
        int width = 0;
        for (char c : s.toCharArray()) {
            width += (c >= 0xAC00 && c <= 0xD7A3) ? 2 : 1; // 한글 2칸
        }
        if (width <= totalWidth) {
            return s + " ".repeat(totalWidth - width);
        } else {
            int count = 0, i = 0;
            for (; i < s.length() && count < totalWidth - 3; i++) {
                count += (s.charAt(i) >= 0xAC00 && s.charAt(i) <= 0xD7A3) ? 2 : 1;
            }
            return s.substring(0, i) + "...";
        }
    }

    public static void bookRegister() {
        while (true) {
            System.out.println("[ 신규 도서 등록 ]");
            System.out.print("도서명 : ");
            String title = scanner.nextLine();
            System.out.print("작가 : ");
            String author = scanner.nextLine();
            System.out.print("출판사 : ");
            String publish = scanner.nextLine();
            System.out.print("출판일(ex 2025-12-15) : ");
            String publicDateString = scanner.next();
            scanner.nextLine(); // 버퍼 정리
            Timestamp publicDate = null;

            try {
                Date parsedDate = dateFormat.parse(publicDateString);
                publicDate = new Timestamp(parsedDate.getTime());

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("날짜 형식이 잘못되었습니다. (yyyy-MM-dd 양식 필요)");
                break;
            }

            System.out.print("재고 수 : ");
            int stock;
            try {
                stock = scanner.nextInt();
                scanner.nextLine(); // 버퍼 정리

                if (stock < 0) {
                    System.out.println("재고 수는 0 이상이여야 합니다.");
                    break;
                }

            } catch (InputMismatchException e) { // 입력 타입 잘못되면
                scanner.nextLine(); // 버퍼 정리(try에서 하기 전에 catch로 오니까 써줘야 함)
                System.out.println("재고 수는 정수로 입력해주세요!");
                break;
            } catch (Exception e) {
                // int 초과일 경우도 있을 수 있으니까
                System.out.println(e.getMessage());
                break;
            }

            System.out.print("[ 입력한 정보를 확인 ]");
            System.out.println("도서명 : " + title);
            System.out.println("작가 : " + author);
            System.out.println("출판사 : " + publish);
            System.out.println("출판일 : " + publicDate); // timestamp 알아서 잘 나온다.
            System.out.println("재고 수 : " + stock);

            while (true) {
                System.out.print("입력한 정보로 도서 등록을 하시겠습니까?(Y/N) ");
                char done = scanner.nextLine().toUpperCase().charAt(0);

                if (done == 'Y') {

                    // 도서 등록
                    if (controller.registerBook(title, publish, author, publicDate, stock)) {
                        System.out.println("");
                        System.out.println("도서 등록 성공!\n");
                    } else {
                        System.out.println();
                        System.out.println("회원 가입 실패\n");
                    }
                    return;

                } else if (done == 'N')
                    return;
            }
        }
    }

    public static void bookRemove() {
        System.out.println("[ 도서 삭제 ]");
        bookAllPrint();
        List<BookDTO> bookList = controller.bookAll();

        if (bookList == null || bookList.isEmpty()) {
            System.out.println("삭제할 도서 데이터가 없습니다.");
            return;
        }

        System.out.print("\n어떤 도서를 삭제하시겠습니까? (ISBN 입력) : ");
        String removeBookId = scanner.next();
        scanner.nextLine(); // 버퍼 정리

        // 혹시 모르니 체크
        boolean check = false;
        for (BookDTO book : bookList) {
            if (book.getBookId().equals(removeBookId)) {
                check = true;
                break;
            }
        }

        if (check)
            if (controller.removeBook(removeBookId)) {
                System.out.println("도서 삭제 성공.");
            } else {
                System.out.println("도서 삭제 실패.");
            }
        return;
    }

    public static void bookRental() {
        if (loginMember == null) {
            System.out.println("[접근 불가] - 로그인 유저 전용 서비스입니다.\n");
            return;
        }

        while (true) {
            boolean rentedCheck = true;

            bookAllPrint();
            System.out.print("\n대여할 도서 ISBN : ");
            String bookId = scanner.next().toUpperCase();
            scanner.nextLine(); // 버퍼 정리

            // 입력한 도서 아이디가 존재하는 지 체크
            BookDTO bookInfo = controller.bookSeach(bookId);
            if (bookInfo == null || bookInfo.getBookId() == null || bookInfo.getBookId().isEmpty()) {
                System.out.println("해당 ISBN은 존재하지 않는 ID입니다.\n");
                return;
            }

            // 해당 사용자가 책을 이미 빌린 상태인지 아닌지 보기 위한 코드
            List<RentalDTO> rentalInfo = controller.rentalCheck(loginMember.getMemId());

            for (RentalDTO r : rentalInfo) {
                // 대여하려는 회원(로그인 유저)이 같은 책을 반납하지 않은 상태라면(Rented는 대여할 때 DB에서 default로 생성됨.)
                // 만약 이거 체크 안하고 다 넣으면 책 반납할 때
                if (r.getRentalMemId().equals(loginMember.getMemId())
                        && r.getRentalBookId().equals(bookInfo.getBookId())
                        && r.getStatus().equals("Rented")) {
                    System.out.println("[대여 불가] - 해당 도서는 회원님께서, 아직 반납하지 않은 책입니다.\n");
                    rentedCheck = false;
                    break;
                }
            }

            if (!rentedCheck)
                continue;

            // 보험으로 컨트롤러에서 처리할 코드가 있긴 하지만, 입력 단계에서 빠꾸 때리는 게 낫다.
            if (bookInfo.getStock() < 1) {
                System.out.println("도서 수량이 부족합니다!\n");
                continue;
            }

            System.out.println("[ 대여할 도서 정보 ]");
            System.out.printf("도서 ID : %s\n", bookInfo.getBookId());
            System.out.printf("도서명 : %s\n", bookInfo.getTitle());
            System.out.printf("작가 : %s\n", bookInfo.getAuthor());
            System.out.printf("출판사 : %s\n", bookInfo.getPublish());
            // 출판날짜는 default 설정이 없어서 null일 때 그냥 출력하면 뻗을 수 있다.
            System.out.printf("출판 날짜 : %s\n",
                    (bookInfo.getPublicDate() != null) ? bookInfo.getPublicDate().toString() : "날짜 미상");
            System.out.printf("도서 수량 : %d\n", bookInfo.getStock());

            System.out.print("\n대여하시겠습니까? (Y/N) : ");
            String done = scanner.next().toUpperCase();
            scanner.nextLine(); // 버퍼 정리

            if (done.equals("Y")) {
                if (controller.rentalBook(loginMember.getMemId(), bookId)) {
                    System.out.println("대여 성공!\n");
                    return;
                }
            } else {
                return;
            }

        }

    }

    public static void bookReturn() {

        if (loginMember == null) {
            System.out.println("[접근 불가] - 로그인 유저 전용 서비스입니다.\n");
            return;
        }

        List<RentalDTO> rentals = controller.onlyRented(loginMember.getMemId());

        if (rentals == null || rentals.isEmpty()) {
            System.out.println("해당 사용자가 대여 중인 도서는 없습니다.\n");
            return;
        }

        System.out.println("====================== [ 도서 반납 ] =======================");
        System.out.printf("%s %s %s %s\n",
                padWithDots("INDEX", 10),
                padWithDots("Book ID", 12),
                padWithDots("Member ID", 15),
                padWithDots("Rental Date", 12));

        System.out.println("=====".repeat(12));
        for (RentalDTO bto : rentals) {
            System.out.printf("%s %s %s %s\n",
                    padWithDots(String.valueOf(bto.getId()), 10),
                    padWithDots(bto.getRentalBookId(), 12),
                    padWithDots(bto.getRentalMemId(), 15),
                    padWithDots(dateFormat.format(bto.getRentalDate()), 12));
        }
        System.out.println("=====".repeat(12));

        System.out.print("어떤 도서를 반납하시겠습니까? (도서 ID) : ");
        String bookId = scanner.next().toUpperCase();
        scanner.nextLine(); // 버퍼 정리

        // 입력한 도서 아이디가 존재하는 지 체크
        BookDTO bookInfo = controller.bookSeach(bookId);
        if (bookInfo == null || bookInfo.getBookId() == null || bookInfo.getBookId().isEmpty()) {
            System.out.println("해당 ISBN은 존재하지 않는 ID입니다.\n");
            return;
        }

        if (controller.returnBook(bookId, loginMember.getMemId())) {
            System.out.println("반납 성공.\n");
        }
    }
}
