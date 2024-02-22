package com.example.commentservice.service;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.PostDto;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    List<PostDto> getPostData(Integer userId, Long offset, Integer limit);

    ReactionCount addInteraction(ReactionDto reactionDto);

    ReactionCount removeInteraction(ReactionDto reactionDto) throws CommentServiceException;

    List<Reactions> getReactionInfo(Long postId, Long offset, Reaction reaction, Integer limit);
}
