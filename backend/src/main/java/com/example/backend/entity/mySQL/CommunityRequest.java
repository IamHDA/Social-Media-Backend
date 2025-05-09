package com.example.backend.entity.mySQL;

import com.example.backend.entity.id.CommunityRequestId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "community_request")
@NoArgsConstructor
public class CommunityRequest {

    @EmbeddedId
    private CommunityRequestId id;

    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @MapsId("communityId")
    @JoinColumn(name = "community_id", nullable = false)
    @ManyToOne
    private Community community;

    public CommunityRequest(User user, Community community) {
        this.user = user;
        this.community = community;
        this.id = new CommunityRequestId(user.getId(), community.getId());
    }
}
