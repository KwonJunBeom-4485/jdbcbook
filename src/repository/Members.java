package repository;

import java.util.List;
import java.util.Optional;

import dto.MemberDTO;

public interface Members {
    boolean memberAdd(MemberDTO memberDTO);
    // 내 기존 memId를 이용해 정보 수정(그래서 로그인하면 내 계정 정보를 저장해야함)
    boolean memberMod(MemberDTO memberDTO, String memId);
    boolean memberDel(MemberDTO memberDTO);

    List<MemberDTO> memberAll();
    Optional<MemberDTO> login(String memId, String memPw);
    Optional<MemberDTO> memberSearch(String memId);
    

}
