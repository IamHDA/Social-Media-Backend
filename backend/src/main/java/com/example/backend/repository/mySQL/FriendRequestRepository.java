package com.example.backend.repository.mySQL;

import com.example.backend.dto.FriendRequestDTO;
import com.example.backend.entity.id.FriendRequestId;
import com.example.backend.entity.mySQL.FriendRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {
    @Query("""
    select fr from FriendRequest fr
    where (fr.user1.id = :user1Id and fr.user2.id = :user2Id)
    or (fr.user2.id = :user1Id and fr.user1.id = :user2Id)
""")
    FriendRequest findExistRequestByUser1IdAndUser2Id(@Param("user1Id") long user1Id, @Param("user2Id") long user2Id);

    @Query("""
    select fr from FriendRequest fr
    where fr.user2.id = :userId
""")
    List<FriendRequest> findByUserId(@Param("userId") long id, Pageable pageable);
}
