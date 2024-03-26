package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.profile.ProfileDTO;
import com.ratz.greenbites.entity.Profile;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T22:49:12-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class ProfileResponseMapperImpl implements ProfileResponseMapper {

    @Override
    public ProfileDTO profileToProfileDTO(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setFirstName( profile.getFirstName() );
        profileDTO.setLastName( profile.getLastName() );
        profileDTO.setBio( profile.getBio() );
        profileDTO.setLocation( profile.getLocation() );
        profileDTO.setBirthDate( profile.getBirthDate() );
        List<String> list = profile.getPhotos();
        if ( list != null ) {
            profileDTO.setPhotos( new ArrayList<String>( list ) );
        }
        profileDTO.setProfileImageUrl( profile.getProfileImageUrl() );

        return profileDTO;
    }

    @Override
    public Profile profileDTOToProfile(ProfileDTO profileDTO) {
        if ( profileDTO == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setFirstName( profileDTO.getFirstName() );
        profile.setLastName( profileDTO.getLastName() );
        profile.setBio( profileDTO.getBio() );
        profile.setLocation( profileDTO.getLocation() );
        profile.setBirthDate( profileDTO.getBirthDate() );
        List<String> list = profileDTO.getPhotos();
        if ( list != null ) {
            profile.setPhotos( new ArrayList<String>( list ) );
        }
        profile.setProfileImageUrl( profileDTO.getProfileImageUrl() );

        return profile;
    }
}
