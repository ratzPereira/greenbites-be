package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByIdAndUserId(Long id, Long userId);

    Page<Recipe> findByUserId(Long userId, Pageable pageable);

    Page<Recipe> findByUserIdAndIsPublicTrue(Long userId, Pageable pageable);
}
