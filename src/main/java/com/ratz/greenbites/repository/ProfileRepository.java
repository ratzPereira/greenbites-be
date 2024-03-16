package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> getProfileByUserId(Long id);
}
