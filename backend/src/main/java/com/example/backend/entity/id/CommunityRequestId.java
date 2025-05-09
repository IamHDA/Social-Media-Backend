package com.example.backend.entity.id;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class CommunityRequestId {
    private long userId;
    private long communityId;

    public CommunityRequestId(long userId, long communityId){
        this.userId = userId;
        this.communityId = communityId;
    }
}
