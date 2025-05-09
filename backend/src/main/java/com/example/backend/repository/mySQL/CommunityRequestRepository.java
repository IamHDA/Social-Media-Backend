package com.example.backend.repository.mySQL;

import com.example.backend.entity.id.CommunityRequestId;
import com.example.backend.entity.mySQL.CommunityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRequestRepository extends JpaRepository<CommunityRequest, CommunityRequestId> {
}
