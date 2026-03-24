package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

    @Query("SELECT c FROM Conversation c JOIN c.members m WHERE m.id = :userId ORDER BY c.createdAt DESC")
    List<Conversation> findByMemberId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Conversation c JOIN c.members m1 JOIN c.members m2 " +
           "WHERE c.type = 'DIRECT' AND m1.id = :userId1 AND m2.id = :userId2")
    Optional<Conversation> findDirectConversation(@Param("userId1") Integer userId1,
                                                   @Param("userId2") Integer userId2);
}
