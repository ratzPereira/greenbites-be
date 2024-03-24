package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.entity.UserFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {

    @Query("SELECT uf.follower FROM UserFollower uf WHERE uf.followed.id = :userId")
    Page<User> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT uf.followed FROM UserFollower uf WHERE uf.follower.id = :userId")
    Page<User> findFollowingByUserId(@Param("userId") Long userId, Pageable pageable);

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<UserFollower> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    @Query("SELECT uf.followed.id FROM UserFollower uf WHERE uf.follower.id = :followerId")
    List<Long> findFollowedUserIdsByFollowerId(Long followerId);
}

