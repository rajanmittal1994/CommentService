package com.example.commentservice.service.impl;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.Entity.User;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import com.example.commentservice.repository.ReactionCountRepo;
import com.example.commentservice.repository.ReactionsRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionsRepo reactionsRepo;

    @Autowired
    private ReactionCountRepo reactionCountRepo;

    /**
     *  1. updates like/dislike raw data with user info in reactions table
     *  2. update like/dislike aggregated data for post/comment in reaction_count table
     */
    @Transactional
    public ReactionCount addInteraction(ReactionDto reactionDto, User user) {
        Reactions reactions = reactionsRepo.findByContentTypeIdAndContentTypeAndUserIdAndReaction(
                reactionDto.getId(), reactionDto.getContentType(), reactionDto.getUserId(), reactionDto.getReaction());
        if(reactions != null) {
            throw new IllegalArgumentException("like/dislike data already exist for provided user and comment.");
        }

        reactions = new Reactions(reactionDto, user);
        reactionsRepo.save(reactions);

        ReactionCount reactionCount = reactionCountRepo.findByContentTypeIdAndContentType(reactionDto.getId(),
                reactionDto.getContentType());
        if(reactionCount == null) {
            reactionCount = new ReactionCount(reactionDto);
        }
        if(reactionDto.getReaction() == Reaction.LIKE) {
            reactionCount.incrementLikeCount();
        } else if(reactionDto.getReaction() == Reaction.DISLIKE) {
            reactionCount.incrementDisLikeCount();
        }
        reactionCountRepo.save(reactionCount);
        return reactionCount;
    }

    /**
     *  1. removes like/dislike raw data with user info from reactions table
     *  2. reduces like/dislike counters for post/comment in reaction_count table
     */
    @Transactional
    public ReactionCount removeInteraction(ReactionDto reactionDto) throws CommentServiceException {
        Reactions reactions = reactionsRepo.findByContentTypeIdAndContentTypeAndUserIdAndReaction(
                reactionDto.getId(), reactionDto.getContentType(), reactionDto.getUserId(), reactionDto.getReaction());
        if(reactions == null) {
            throw new CommentServiceException("like/dislike data does not exist for provided commentId.");
        }
        reactionsRepo.deleteById(reactions.getId());

        ReactionCount reactionCount = reactionCountRepo.findByContentTypeIdAndContentType(reactionDto.getId(),
                reactionDto.getContentType());
        if(reactionCount == null) {
            throw new CommentServiceException("Data is not synced properly. Aggregate data of counts not available.");
        }
        if(reactionDto.getReaction() == Reaction.LIKE) {
            reactionCount.decrementLikeCount();
        } else if(reactionDto.getReaction() == Reaction.DISLIKE) {
            reactionCount.decrementDisLikeCount();
        }
        reactionCountRepo.save(reactionCount);
        return reactionCount;
    }

    /**
     *  fetches the reaction(like/dislike) info for a comment/post
     */
    @Override
    public List<Reactions> getReactionInfo(Long contentId, ContentType contentType, Reaction reaction, Long offset, Integer limit) {
        return reactionsRepo.findByContentTypeIdAndContentType(contentId, contentType, reaction, offset, limit);
    }
}
