package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.ProfileDTO;
import com.ratz.greenbites.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileResponseMapper {

    ProfileResponseMapper INSTANCE = Mappers.getMapper(ProfileResponseMapper.class);

    ProfileDTO profileToProfileDTO(Profile profile);

    Profile profileDTOToProfile(ProfileDTO profileDTO);
}
