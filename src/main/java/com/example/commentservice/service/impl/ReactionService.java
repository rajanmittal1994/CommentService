package com.example.commentservice.service.impl;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.Entity.User;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReactionService {

    ReactionCount addInteraction(ReactionDto reactionDto, User user);
    ReactionCount removeInteraction(ReactionDto reactionDto) throws CommentServiceException;

    List<Reactions> getReactionInfo(Long postId, ContentType contentType, Reaction reaction, Long offset, Integer limit);
}
