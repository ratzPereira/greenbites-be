package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.auth.UserDTO;
import com.ratz.greenbites.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-18T19:08:10-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setEnabled( user.isEnabled() );
        userDTO.setNotLocked( user.isNotLocked() );
        userDTO.setUsingMfa( user.isUsingMfa() );
        userDTO.setCreatedAt( user.getCreatedAt() );

        return userDTO;
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setEmail( userDTO.getEmail() );
        user.setEnabled( userDTO.isEnabled() );
        user.setNotLocked( userDTO.isNotLocked() );
        user.setUsingMfa( userDTO.isUsingMfa() );
        user.setCreatedAt( userDTO.getCreatedAt() );

        return user;
    }
}
