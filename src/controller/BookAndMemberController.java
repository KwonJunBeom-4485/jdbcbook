package controller;

import dto.MemberDTO;
import repository.MembersDAOImpl;

public class BookAndMemberController {
    private MembersDAOImpl memberService = new MembersDAOImpl();

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
            yn = true;  // true ( = 중복이라 못만듬)

        return yn;
    }

    public boolean memberSeachPhone(String phone) {
        boolean yn = false; // 중복 데이터 없음

        if (memberService.memberSearchPhone(phone) != null) // 중복 데이터가 있다면
            yn = true;  // true ( = 중복이라 못만듬)

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

}
