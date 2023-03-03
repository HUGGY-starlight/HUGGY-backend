package com.starlight.huggy.repository;


import com.starlight.huggy.domain.auth.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findBySocialId(String socialId);
}
