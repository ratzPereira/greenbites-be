package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.comment.CreateCommentDTO;
import com.ratz.greenbites.entity.Comment;

public interface CommentService {

    Comment createComment(CreateCommentDTO comment, Long postId, Long userId);
}
