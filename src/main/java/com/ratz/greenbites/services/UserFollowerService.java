package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.follower.UserSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFollowerService {

    void followUser(Long currentUserId, Long userIdToFollow);

    void unfollowUser(Long currentUserId, Long userIdToUnfollow);

    Page<UserSummaryDTO> getFollowers(Long userId, Pageable pageable);

    Page<UserSummaryDTO> getFollowing(Long userId, Pageable pageable);
}
