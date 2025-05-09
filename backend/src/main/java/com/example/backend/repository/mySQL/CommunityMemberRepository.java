package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityMemberRepository  extends JpaRepository<CommunityMember, Long> {
}
