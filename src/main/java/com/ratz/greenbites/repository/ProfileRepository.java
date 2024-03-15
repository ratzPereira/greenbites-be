package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile getProfileByUserId(Long id);
}
