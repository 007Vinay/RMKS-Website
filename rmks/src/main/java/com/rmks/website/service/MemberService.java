package com.rmks.website.service;

import com.rmks.website.model.Member;
import com.rmks.website.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllPendingRequests() {
        return memberRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }

    public void approveMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        member.setApproved(true);
        member.setStatus("APPROVED");
        memberRepository.save(member);
    }

    public void rejectOrDeleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        member.setStatus("REJECTED");
        memberRepository.save(member);
    }

    public Member saveMemberRequest(Member member) {
        member.setApproved(false);
        member.setStatus("PENDING");
        return memberRepository.save(member);
    }
}
