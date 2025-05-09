package com.example.backend.repository.mySQL;

import com.example.backend.Enum.SearchType;
import com.example.backend.dto.SearchDTO;
import com.example.backend.entity.mySQL.Community;
import com.example.backend.entity.mySQL.Friendship;
import com.example.backend.entity.mySQL.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilterRepository {
    private final EntityManager em;

    public FilterRepository(EntityManager em) {
        this.em = em;
    }

    public List<SearchDTO> unifiedSearch(String keyword, long currentUserId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // Tạo CriteriaQuery với SearchDTO
        CriteriaQuery<SearchDTO> query = cb.createQuery(SearchDTO.class);

        // Tạo các root cho từng entity
        Root<Community> communityRoot = query.from(Community.class);
        Root<User> userRoot = query.from(User.class);

        // Tạo subquery để kiểm tra bạn bè
        Subquery<Long> friendSubquery = query.subquery(Long.class);
        Root<Friendship> friendshipRoot = friendSubquery.from(Friendship.class);
        friendSubquery.select(cb.literal(1L))
                .where(cb.or(
                        cb.and(
                                cb.equal(friendshipRoot.get("user1").get("id"), currentUserId),
                                cb.equal(friendshipRoot.get("user2"), userRoot)
                        ),
                        cb.and(
                                cb.equal(friendshipRoot.get("user2").get("id"), currentUserId),
                                cb.equal(friendshipRoot.get("user1"), userRoot)
                        )
                ));

        // Tạo các expression cho kết quả
        Expression<Object> id = cb.selectCase()
                .when(communityRoot.isNotNull(), communityRoot.get("id"))
                .otherwise(userRoot.get("id"));

        Expression<Object> name = cb.selectCase()
                .when(communityRoot.isNotNull(), communityRoot.get("name"))
                .otherwise(userRoot.get("username"));

        Expression<Object> type = cb.selectCase()
                .when(communityRoot.isNotNull(), cb.literal(SearchType.COMMUNITY))
                .otherwise(cb.literal(SearchType.USER));

        Expression<Object> priority = cb.selectCase()
                .when(communityRoot.isNotNull(), cb.literal(0))
                .when(cb.exists(friendSubquery), cb.literal(1))
                .otherwise(cb.literal(2));

        // Tạo điều kiện tìm kiếm
        Predicate communityCondition = cb.and(
                cb.like(cb.lower(communityRoot.get("name")), "%" + keyword.toLowerCase() + "%"),
                cb.isNotNull(communityRoot.get("id"))
        );

        Predicate userCondition = cb.and(
                cb.notEqual(userRoot.get("id"), currentUserId),
                cb.or(
                        cb.like(cb.lower(userRoot.get("username")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(userRoot.get("email")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(userRoot.get("bio")), "%" + keyword.toLowerCase() + "%")
                ),
                cb.isNotNull(userRoot.get("id"))
        );

        // Tạo union query
        query.select(cb.construct(
                        SearchDTO.class,
                        id,
                        name,
                        type,
                        priority
                ))
                .where(cb.or(communityCondition, userCondition))
                .orderBy(cb.asc(priority), cb.asc(name));

        return em.createQuery(query).getResultList();
    }
}
