package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.follower.UserSummaryDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.entity.UserFollower;
import com.ratz.greenbites.enums.NotificationType;
import com.ratz.greenbites.repository.UserFollowerRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.NotificationService;
import com.ratz.greenbites.services.UserFollowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFollowerServiceImpl implements UserFollowerService {

    private final UserRepository userRepository;
    private final UserFollowerRepository userFollowerRepository;
    private final NotificationService notificationService;

    @Override
    public void followUser(Long currentUserId, Long userIdToFollow) {
        if (currentUserId.equals(userIdToFollow)) {
            log.warn("User {} attempted to follow themselves.", currentUserId);
            throw new IllegalArgumentException("You cannot follow yourself.");
        }

        userRepository.findById(userIdToFollow).ifPresentOrElse(followedUser -> {
            if (userFollowerRepository.existsByFollowerIdAndFollowedId(currentUserId, userIdToFollow)) {
                log.info("User {} already follows user {}.", currentUserId, userIdToFollow);
            } else {
                UserFollower userFollower = new UserFollower();
                userFollower.setFollower(userRepository.findById(currentUserId).orElseThrow(() -> new UsernameNotFoundException("User not found")));
                userFollower.setFollowed(followedUser);
                userFollowerRepository.save(userFollower);
                log.info("User {} started following user {}.", currentUserId, userIdToFollow);
            }
        }, () -> {
            log.warn("Attempted to follow non-existing user with id {}.", userIdToFollow);
            throw new UsernameNotFoundException("User to follow not found");
        });

        notificationService.createNotification(userIdToFollow, NotificationType.FOLLOW, "User " + currentUserId + " started following you.");
    }

    @Override
    public void unfollowUser(Long currentUserId, Long userIdToUnfollow) {
        userFollowerRepository.findByFollowerIdAndFollowedId(currentUserId, userIdToUnfollow).ifPresentOrElse(userFollower -> {
            userFollowerRepository.delete(userFollower);
            log.info("User {} unfollowed user {}.", currentUserId, userIdToUnfollow);
        }, () -> {
            log.info("User {} does not follow user {}, no action taken.", currentUserId, userIdToUnfollow);
        });
    }

    @Override
    public Page<UserSummaryDTO> getFollowers(Long userId, Pageable pageable) {
        return userFollowerRepository.findFollowersByUserId(userId, pageable)
                .map(this::convertToUserSummaryDTO);
    }

    @Override
    public Page<UserSummaryDTO> getFollowing(Long userId, Pageable pageable) {
        return userFollowerRepository.findFollowingByUserId(userId, pageable)
                .map(this::convertToUserSummaryDTO);
    }

    private UserSummaryDTO convertToUserSummaryDTO(User user) {

        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
