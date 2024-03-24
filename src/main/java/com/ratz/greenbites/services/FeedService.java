package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.post.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {

    Page<PostResponseDTO> getFeedForUser(Long userId, Pageable pageable);
}
