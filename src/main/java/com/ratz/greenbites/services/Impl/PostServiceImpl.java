package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.post.CreatePostDTO;
import com.ratz.greenbites.entity.Collection;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.NotificationType;
import com.ratz.greenbites.exception.ForbiddenException;
import com.ratz.greenbites.repository.CollectionRepository;
import com.ratz.greenbites.repository.PostRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.NotificationService;
import com.ratz.greenbites.services.PostService;
import com.ratz.greenbites.services.UserService;
import com.ratz.greenbites.services.external.AzureStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AzureStorageService azureStorageService;
    private final NotificationService notificationService;
    private final CollectionRepository collectionRepository;
    private final UserService userService;


    @Override
    public Post createPost(CreatePostDTO post, Long userId) {

        log.info("Creating post for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        Post newPost = new Post();
        newPost.setContent(post.getContent());

        uploadImages(post, newPost);

        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());

        return postRepository.save(newPost);
    }


    @Override
    @Transactional
    public Post updatePost(Long postId, CreatePostDTO post, Long userId) {

        log.info("Updating post with ID: {}", postId);

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to update this post");
        }

        existingPost.setContent(post.getContent());
        uploadImages(post, existingPost);

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long postId, Long userId) {

        log.info("Deleting post with ID: {}", postId);

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to delete this post");
        }

        postRepository.deleteById(postId);
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }

    @Override
    public Page<Post> getPostByUserId(Long userId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        //if no user throw exception
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return postRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public boolean likeOrDislikePost(Long postId, Long userId) {

        log.info("Like or dislike post with ID: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        if (post.getLikedBy().contains(user)) {
            post.getLikedBy().remove(user);
            return false;

        } else {
            post.getLikedBy().add(user);
            notificationService.createNotification(post.getUser().getId(), NotificationType.LIKE,
                    user.getProfile().getFirstName() + " " + user.getProfile().getLastName() + " liked your post");
        }
        postRepository.save(post);
        return true;
    }

    @Override
    @Transactional
    public void removePostFromCollection(Long postId, Long collectionId, Long userId) {
        log.info("Removing post with ID: {} from collection with ID: {}", postId, collectionId);

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!collection.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not have permission to modify this collection");
        }
        collection.getPosts().remove(post);
        collectionRepository.save(collection);
    }

    @Override
    @Transactional
    public void addPostToCollection(Long postId, Long collectionId, Long userId) {
        log.info("Adding post with ID: {} to collection with ID: {}", postId, collectionId);

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!collection.getUser().getId().equals(userId)) {
            throw new RuntimeException("User does not have permission to modify this collection");
        }
        collection.getPosts().add(post);
        collectionRepository.save(collection);
    }


    private void uploadImages(CreatePostDTO post, Post newPost) {
        if (post.getImageFiles() == null) {
            return;
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : post.getImageFiles()) {
            if (!file.isEmpty()) {
                String imageUrl = azureStorageService.uploadFile(file);
                imageUrls.add(imageUrl);
            }
        }
        newPost.setImageUrls(imageUrls);
    }
}
