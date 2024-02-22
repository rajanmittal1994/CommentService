package com.example.commentservice.service.impl;

import com.example.commentservice.Entity.*;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.CommentDto;
import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import com.example.commentservice.repository.*;
import com.example.commentservice.service.CommentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ParentChildCommentRepo pcCommentRepo;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private ReactionCountRepo reactionCountRepo;


    /**
     * adds comment to a post (or it could be reply to a comment).
     */
    @Override
    @Transactional
    public CommentDto addComment(CommentDto commentDto) {
        Optional<User> user = userRepo.findById(commentDto.getUserId());
        if(user.isEmpty()) {
            throw new IllegalArgumentException("provided userId does not exist.");
        }

        Optional<Post> post = postRepo.findById(commentDto.getPostId());

        if(post.isEmpty()) {
            throw new IllegalArgumentException("provided postId does not exist.");
        }

        Optional<Comment> parentComment = commentRepo.findById(commentDto.getParentCommentId());
        if(commentDto.getParentCommentId() != -1 && parentComment.isEmpty()) {
            throw new IllegalArgumentException("parent commentId does not exist.");
        }

        Comment comment = new Comment(commentDto, user.get(), post.get(), commentDto.getParentCommentId());
        comment = commentRepo.save(comment);
        commentDto.setId(comment.getId());
        addParentChildComments(commentDto.getId(), commentDto.getParentCommentId());
        return commentDto;
    }

    /**
     * fetch replies to comment with commentID
     */
    @Override
    public List<CommentDto> getRepliesInfo(Long commentId, Long offset, Integer limit) {
        Optional<Comment> comment = commentRepo.findById(commentId);
        if(comment.isEmpty()) {
            throw new IllegalArgumentException("provided postId does not exist.");
        }
        List<Comment> comments = commentRepo.findCommentByParentCommentId(commentId, offset, limit + 1);
        return getCommentReactData(comments, getReplyExistMap(comments));
    }

    /**
     * fetch comments to a post with postID
     */
    @Override
    public List<CommentDto> getCommentInfoForPost(Long postId, Long offset, Integer limit) {
        Optional<Post> post = postRepo.findById(postId);

        if(post.isEmpty()) {
            throw new IllegalArgumentException("provided postId does not exist.");
        }
        List<Comment> comments = commentRepo.findCommentByPost(postId, offset, limit + 1);

        return getCommentReactData(comments, getReplyExistMap(comments));
    }

    /**
     * returns map of ids and boolean which explain whether reply exist for a particular comment
     */
    private Map<Long,Boolean> getReplyExistMap(List<Comment> comments) {
        List<Long> ids = comments.stream().map(Comment::getId).collect(Collectors.toList());
        List<Comment> nextLevelComments = commentRepo.findCommentByParentCommentIdIn(ids);
        Map<Long,Boolean> map = new HashMap<>();
        nextLevelComments.forEach(
                nextLevelComment-> {
                    map.put(nextLevelComment.getParentCommentId(), true);
                }
        );
        return map;
    }

    /**
     * logs like/dislike interaction for a comment
     */
    @Override
    public ReactionCount addInteraction(ReactionDto reactionDto) {
        User user = validate(reactionDto);
        reactionDto.setContentType(ContentType.COMMENT);
        return reactionService.addInteraction(reactionDto, user);
    }

    /**
     * removes like/dislike interaction for a comment
     */
    @Override
    public ReactionCount removeInteraction(ReactionDto reactionDto) throws CommentServiceException {
        validate(reactionDto);
        reactionDto.setContentType(ContentType.COMMENT);
        return reactionService.removeInteraction(reactionDto);
    }

    /**
     * fetches paginated like/dislike info for a comment
     */
    @Override
    public List<Reactions> getReactionInfo(Long commentId, Reaction reaction, Long offset, int limit) {
        return reactionService.getReactionInfo(commentId, ContentType.COMMENT, reaction, offset, limit);
    }

    /**
     *  validates whether userId and commentId are correctly present in reactionDto
     */
    private User validate(ReactionDto reactionDto) {
        Optional<User> user = userRepo.findById(reactionDto.getUserId());
        if(user.isEmpty()) {
            throw new IllegalArgumentException("provided userId does not exist.");
        }

        Optional<Comment> comment = commentRepo.findById(reactionDto.getId());

        if(comment.isEmpty()) {
            throw new IllegalArgumentException("provided commentId does not exist.");
        }
        return user.get();
    }

    /**
     * adds dependency between parent and child comments.
     */
    private void addParentChildComments(Long id, Long parentCommentId) {
        List<ParentChildComment> itemList = new ArrayList<>();
        ParentChildComment curParentChildComment = new ParentChildComment();
        curParentChildComment.setParentCommentId(id);
        curParentChildComment.setChildCommentId(id);
        curParentChildComment.setLevel(0);
        itemList.add(curParentChildComment);
        if(parentCommentId != -1) {
            List<ParentChildComment> parentChildCommentList = pcCommentRepo.findByChildCommentId(parentCommentId);
            for(ParentChildComment parComment:parentChildCommentList) {
                ParentChildComment newParentChildComment = new ParentChildComment();
                newParentChildComment.setParentCommentId(parComment.getParentCommentId());
                newParentChildComment.setChildCommentId(id);
                newParentChildComment.setLevel(parComment.getLevel() + 1);
                itemList.add(newParentChildComment);
            }
        }
        pcCommentRepo.saveAll(itemList);
    }

    /**
     *  add like/dislike aggregated data to a comment
     */
    private List<CommentDto> getCommentReactData(List<Comment> comments, Map<Long, Boolean> replyExistMap) {
        List<Long> idlist = comments.stream().map(Comment::getId).collect(Collectors.toList());
        List<ReactionCount> reactionCounts = reactionCountRepo.findByContentTypeIdInAndContentType(idlist, ContentType.COMMENT);
        Map<Long, ReactionCount> idToReactionCountMap = reactionCounts.stream()
                .collect(Collectors.toMap(ReactionCount::getId, a->a));

        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(
                comment -> {
                    CommentDto commentDto = new CommentDto();
                    ReactionCount reactionCount = idToReactionCountMap.get(comment.getId());
                    Map<Reaction, Integer> reactionMap = new HashMap<>();
                    if(reactionCount == null) {
                        reactionMap.put(Reaction.LIKE, 0);
                        reactionMap.put(Reaction.DISLIKE, 0);
                    } else {
                        reactionMap.put(Reaction.LIKE, reactionCount.getLikeCount());
                        reactionMap.put(Reaction.DISLIKE, reactionCount.getDisLikeCount());
                    }
                    commentDto.setReactionMap(reactionMap);
                    commentDto.setId(comment.getId());
                    commentDto.setReplyExist(replyExistMap.getOrDefault(comment.getId(), false));
                    commentDto.setContent(comment.getContent());
                    commentDto.setParentCommentId(comment.getParentCommentId());
                    commentDtos.add(commentDto);
                }
        );
        return commentDtos;
    }
}
