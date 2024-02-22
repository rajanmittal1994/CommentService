package com.example.commentservice.service;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.CommentDto;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    CommentDto addComment(CommentDto comment);

    List<CommentDto> getRepliesInfo(Long commentId, Long offset, Integer limit);

    List<CommentDto> getCommentInfoForPost(Long postId, Long offset, Integer limit);

    ReactionCount addInteraction(ReactionDto reactionDto);

    ReactionCount removeInteraction(ReactionDto reactionDto) throws CommentServiceException;

    List<Reactions> getReactionInfo(Long commentId, Reaction reaction, Long offset, int limit);
}
