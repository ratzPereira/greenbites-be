package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.post.PostResponseDTO;
import com.ratz.greenbites.entity.Post;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T22:04:09-0100",
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
        List<String> list = post.getImageUrls();
        if ( list != null ) {
            postResponseDTO.setImageUrls( new ArrayList<String>( list ) );
        }
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
        List<String> list = postDTO.getImageUrls();
        if ( list != null ) {
            post.setImageUrls( new ArrayList<String>( list ) );
        }
        post.setContent( postDTO.getContent() );
        post.setCreatedAt( postDTO.getCreatedAt() );

        return post;
    }
}
