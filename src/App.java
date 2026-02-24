import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    private static Scanner scanner = new Scanner(System.in);
    private static boolean flag = false;

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

            char choice = scanner.nextLine().charAt(0);
            System.out.println();
            System.out.println("===".repeat(10));

            switch (choice) {
                case '1':
                    userManageMenu();
                    break;
                case '2':
                    break;
                case '0':
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("메뉴를 잘못 입력했습니다.");
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

            char choice = scanner.nextLine().charAt(0);
            System.out.println();
            System.out.println();

            switch (choice) {
                case '1':
                    // 로그인 처리 메서드 호출
                    signIn();
                    break;
                case '2':
                    // 회원 가입 정보 처리 메서드 호출
                    signUp();
                    break;
                case '0':
                    System.out.println("메인으로 이동합니다.");
                    return;
                default:
                    System.out.println("메뉴를 잘못 입력했습니다.");
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
            String userAddress1 = scanner.nextLine();

            // 중복된 회원 정보가 있는 지 확인
            


            System.out.print("[ 입력한 정보를 확인 ]");
            System.out.println("사용자 ID : " + userId);
            System.out.println("사용자 PW : " + userPw);
            System.out.println("사용자 이름 : " + userName);
            System.out.println("사용자 이메일 : " + userEmail);
            System.out.println("사용자 전화번호 : " + userPhone);
            System.out.println("나이 : " + userAge);
            System.out.println("주소 : " + userAddress1);

            while (true) {
                System.out.print("입력한 정보로 회원 가입을 하시겠습니까?(Y/N) ");
                char done = scanner.nextLine().toUpperCase().charAt(0);

                if (done == 'Y') {
                    // 회원 가입 처리
                    boolean status = controller.join(userId, userPw, userName, userEmail,
                            userPhone, userAge, userAddress1, userAddress2);

                    // 회원 가입 메뉴 나가고, 실패하면 다시 while문으로
                    if (status)
                        return;
                    else {
                        System.out.println("회원 가입 실패");
                        break;
                    }

                } else if (done == 'N') {
                    break;
                }
            }
        }
    }
}
