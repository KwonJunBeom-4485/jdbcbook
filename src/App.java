import java.util.InputMismatchException;
import java.util.Scanner;

import controller.BookAndMemberController;
import dto.MemberDTO;

public class App {

    private static Scanner scanner = new Scanner(System.in);
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
                    } else {
                        userManageMenu();
                    }
                    break;
                case "2":
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
                    }
                    signIn();
                    break;
                case "2":
                    // 회원 가입 정보 처리 메서드 호출
                    signUp();
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

                } else if (done == 'N') {
                    break;
                }
            }
        }
    }

    public static void signIn() {
        while (true) {
            if (flag == true) { // 로그인 후 나왔을 때 메뉴로 가기 위한 처리
                flag = !flag;
                return;
            }

            System.out.println("[ 로그인 ]");
            System.out.print("사용자 아이디 : ");
            String userId = scanner.next();
            System.out.print("사용자 패스워드 : ");
            String userPw = scanner.next();
            scanner.nextLine();

            while (true) {
                if (flag == true) {
                    return;
                }

                System.out.print("로그인 하시겠습니까?(Y/N) ");
                char done = scanner.nextLine().toUpperCase().charAt(0);
                System.out.println();

                if (done == 'Y') {
                    // 로그인

                    if (!userId.isEmpty() && !userPw.isEmpty()) { // 아이디 패스워드 일치 여부 확인

                        MemberDTO loginMem = controller.memberInfo(userId);

                        if (loginMem != null && loginMem.getMemPw().equals(userPw)) {
                            loginMember = loginMem; // 로그인 정보 저장
                            userManage();
                        } else {
                            System.out.println("아이디 또는 패스워드가 일치하지 않습니다.");
                            return;
                        }

                    } else {
                        System.out.println("아이디 또는 패스워드가 일치하지 않습니다.");
                        return;
                    }
                }

                return;
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
}
