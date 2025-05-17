package com.rmks.website.repository;

import com.rmks.website.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByApprovedFalseOrderByCreatedAtDesc();
}
