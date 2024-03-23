package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.PrivateMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    Page<PrivateMessage> findBySenderId(Long senderId, Pageable pageable);

    Page<PrivateMessage> findByRecipientId(Long recipientId, Pageable pageable);
}
