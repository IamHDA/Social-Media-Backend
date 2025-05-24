package com.example.backend.repository.mySQL;

import com.example.backend.entity.mongoDB.ConversationParticipant;
import com.example.backend.entity.mySQL.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilterRepository {
    private final EntityManager em;
    private final MongoTemplate mongoTemplate;

    public FilterRepository(EntityManager em, MongoTemplate mongoTemplate) {
        this.em = em;
        this.mongoTemplate = mongoTemplate;
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
        Predicate predicate;
        if(keyword.isBlank()) predicate = cb.conjunction();
        else{
            predicate = cb.and(
                    cb.notEqual(userRoot.get("id"), currentUserId),
                    cb.or(
                            cb.like(cb.lower(userRoot.get("username")), "%" + keyword.toLowerCase() + "%"),
                            cb.like(cb.lower(userRoot.get("email")), "%" + keyword.toLowerCase() + "%")
                    ),
                    cb.isNotNull(userRoot.get("id"))
            );
        }
        query.select(userRoot)
                .where(predicate)
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
        Predicate predicate = cb.and(
                cb.equal(postRecipients.get("recipient").get("id"), currentUserId),
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

    public List<Notification> findNotificationByUserSortByNoticeTime(User user){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Notification> query = cb.createQuery(Notification.class);
        Root<Notification> notificationRoot = query.from(Notification.class);
        Join<Notification, NotificationUser> notificationUsers = notificationRoot.join("notificationUsers");

        Predicate predicate = cb.and(cb.equal(notificationUsers.get("user"), user));

        Expression<Object> priority = cb.selectCase()
                .when(cb.equal(notificationUsers.get("read"), false), cb.literal(0))
                .otherwise(cb.literal(1));
        query.select(notificationRoot)
                .where(predicate)
                .orderBy(cb.asc(priority), cb.desc(notificationRoot.get("noticeAt")));
        return em.createQuery(query).getResultList();
    }

    public List<ConversationParticipant> findConversationParticipantSortByRole(String conversationId){
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("conversationId").is(conversationId)),
                Aggregation.addFields().addField("customPriority")
                        .withValue(
                                ConditionalOperators.switchCases(
                                        ConditionalOperators.Switch.CaseOperator
                                                .when(ComparisonOperators.Eq.valueOf("role").equalToValue("CREATOR")).then(1),
                                        ConditionalOperators.Switch.CaseOperator
                                                .when(ComparisonOperators.Eq.valueOf("role").equalToValue("MOD")).then(2)
                                ).defaultTo(3)
                        ).build(),

                Aggregation.sort(Sort.by(
                        Sort.Order.asc("customPriority"),
                        Sort.Order.asc("participantName")
                ))
        );
        return mongoTemplate.aggregate(agg, "conversation_participant", ConversationParticipant.class).getMappedResults();
    }
//    public List<Post> getPostsInProfile(User currentUser, User opponent){
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Post> query = cb.createQuery(Post.class);
//
//        Root<Post> postRoot = query.from(Post.class);
//        Join<Post, PostRecipient> postRecipients = postRoot.join("postRecipients");
//
//        Predicate predicate = cb.and(
//                cb.equal(postRecipients.get("sender"), opponent),
//                cb.equal(postRecipients.get("recipient"), currentUser),
//                cb.equal(postRecipients.get("disabled"), false)
//        );
//        Expression<Object> priority = cb.selectCase()
//                .when(cb.equal(postRecipients.get("isReviewed"), false), cb.literal(0))
//                .otherwise(cb.literal(1));
//
//        query.select(postRoot)
//                .where(predicate)
//                .orderBy(cb.asc(priority), cb.desc(postRoot.get("createdAt")));
//        return em.createQuery(query).getResultList();
//    }
}
