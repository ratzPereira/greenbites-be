package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.post.PostResponseDTO;
import com.ratz.greenbites.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostResponseDTO postToPostDTO(Post post);

    Post postDTOToPost(PostResponseDTO postDTO);
}
