package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.CommunityMember;
import com.example.backend.entity.mySQL.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityMemberRepository  extends JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findByUser_Id(long userId);

    void deleteByUser_IdAndCommunity_Id(long userId, long communityId);
}
