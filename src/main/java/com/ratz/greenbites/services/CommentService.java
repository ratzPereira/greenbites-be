package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.comment.CreateCommentDTO;
import com.ratz.greenbites.entity.Comment;

public interface CommentService {

    Comment createComment(CreateCommentDTO comment, Long postId, Long userId);

    Comment updateComment(String content, Long commentId, Long userId);

    void deleteComment(Long commentId, Long userId);

    boolean toggleLike(Long commentId, Long userId);
}
