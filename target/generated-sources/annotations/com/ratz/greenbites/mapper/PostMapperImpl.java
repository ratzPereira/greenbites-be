package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.PostResponseDTO;
import com.ratz.greenbites.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-18T11:42:45-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostResponseDTO postToPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostResponseDTO postResponseDTO = new PostResponseDTO();

        postResponseDTO.setId( post.getId() );
        postResponseDTO.setContent( post.getContent() );
        postResponseDTO.setCreatedAt( post.getCreatedAt() );

        return postResponseDTO;
    }

    @Override
    public Post postDTOToPost(PostResponseDTO postDTO) {
        if ( postDTO == null ) {
            return null;
        }

        Post post = new Post();

        post.setId( postDTO.getId() );
        post.setContent( postDTO.getContent() );
        post.setCreatedAt( postDTO.getCreatedAt() );

        return post;
    }
}
