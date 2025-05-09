package com.example.backend.repository.mySQL;

import com.example.backend.entity.id.FriendshipId;
import com.example.backend.entity.mySQL.Friendship;
import com.example.backend.entity.mySQL.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
    @Query(value = "SELECT u.* FROM user u " +
            "WHERE u.id IN ( " +
            "SELECT user2_id FROM friendship WHERE user1_id = :userId " +
            "UNION " +
            "SELECT user1_id FROM friendship WHERE user2_id = :userId )",
            nativeQuery = true)
    List<User> findFriendsByUser(@Param("userId") Long userId);
    @Query("""
    select f from Friendship f
    where (f.user1.id = :user1Id and f.user2.id = :user2Id) 
    or (f.user1.id = :user2Id and f.user2.id = :user1Id) 
""")
    Optional<Friendship> findByUserId(@Param("user1Id") long user1Id, @Param("user2Id") long user2Id);
    @Query("""
    select distinct f from Friendship f
    where f.user1.id = :userId or f.user2.id = :userId
""")
    List<Friendship> findFriendsByUserId(@Param("userId") long userId, Pageable pageable);
}
