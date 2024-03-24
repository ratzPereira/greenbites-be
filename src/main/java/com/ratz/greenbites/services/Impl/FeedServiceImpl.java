package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.post.PostResponseDTO;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.mapper.PostMapper;
import com.ratz.greenbites.repository.PostRepository;
import com.ratz.greenbites.repository.UserFollowerRepository;
import com.ratz.greenbites.services.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedServiceImpl implements FeedService {

    private final PostRepository postRepository;
    private final UserFollowerRepository userFollowerRepository;

    @Override
    public Page<PostResponseDTO> getFeedForUser(Long userId, Pageable pageable) {
        List<Long> followedUserIds = userFollowerRepository.findFollowedUserIdsByFollowerId(userId);
        Page<Post> followedPosts = postRepository.findByUserIdInOrderByCreatedAtDesc(followedUserIds, pageable);

        if (followedPosts.getContent().isEmpty()) {
            Page<Post> popularPosts = postRepository.findPopularPosts(pageable);
            return popularPosts.map(PostMapper.INSTANCE::postToPostDTO);
        }

        return followedPosts.map(PostMapper.INSTANCE::postToPostDTO);
    }
}
