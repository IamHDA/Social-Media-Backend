package com.example.backend.repository.mySQL;

import com.example.backend.entity.mySQL.Friendship;
import com.example.backend.entity.mySQL.Post;
import com.example.backend.entity.mySQL.PostRecipient;
import com.example.backend.entity.mySQL.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilterRepository {
    private final EntityManager em;

    public FilterRepository(EntityManager em) {
        this.em = em;
    }

    public List<User> searchUser(String keyword, long currentUserId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
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
                    )
                );
        Expression<Object> priority = cb.selectCase()
                .when(cb.exists(friendSubquery), cb.literal(0))
                .otherwise(cb.literal(1));
        Predicate userCondition = cb.and(
                cb.notEqual(userRoot.get("id"), currentUserId),
                cb.or(
                        cb.like(cb.lower(userRoot.get("username")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(userRoot.get("email")), "%" + keyword.toLowerCase() + "%"),
                        cb.like(cb.lower(userRoot.get("bio")), "%" + keyword.toLowerCase() + "%")
                ),
                cb.isNotNull(userRoot.get("id"))
        );
        query.select(userRoot)
                .where(userCondition)
                .orderBy(cb.asc(priority), cb.asc(userRoot.get("username")));
        return em.createQuery(query).getResultList();
    }

    public List<Post> getPosts(long currentUserId){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> postRoot = query.from(Post.class);
        Join<Post, PostRecipient> postRecipients = postRoot.join("postRecipients");
        Root<User> userRoot = query.from(User.class);
        Subquery<Long> friendSubquery = query.subquery(Long.class);
        Root<Friendship> friendshipRoot = friendSubquery.from(Friendship.class);
        friendSubquery.select(cb.literal(1L))
                .where(cb.or(
                        cb.and(
                                cb.equal(friendshipRoot.get("user1").get("id"), currentUserId),
                                cb.equal(friendshipRoot.get("user2"), userRoot)
                        ),
                        cb.and(
                                cb.equal(friendshipRoot.get("user1"), userRoot),
                                cb.equal(friendshipRoot.get("user2").get("id"), currentUserId)
                        )
                ));
        System.out.println(friendSubquery);
        Predicate predicate = cb.and(
                cb.equal(postRecipients.get("user").get("id"), currentUserId),
                cb.equal(postRecipients.get("disabled"), false)
        );
        Expression<Object> priority = cb.selectCase()
                .when(cb.equal(postRecipients.get("isReviewed"), false), cb.literal(0))
                .when(cb.exists(friendSubquery), cb.literal(1))
                .otherwise(cb.literal(2));
        query.select(postRoot)
                .where(predicate)
                .orderBy(cb.asc(priority), cb.desc(postRoot.get("createdAt")));
        return em.createQuery(query).getResultList();
    }
}
