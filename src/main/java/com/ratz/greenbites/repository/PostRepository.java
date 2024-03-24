package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN p.likedBy likes GROUP BY p.id ORDER BY COUNT(likes.id) DESC, p.createdAt DESC")
    Page<Post> findPopularPosts(Pageable pageable);
}
