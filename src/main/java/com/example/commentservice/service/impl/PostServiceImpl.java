package com.example.commentservice.service.impl;

import com.example.commentservice.Entity.Post;
import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.Entity.User;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.PostDto;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import com.example.commentservice.repository.PostRepo;
import com.example.commentservice.repository.ReactionCountRepo;
import com.example.commentservice.repository.UserRepo;
import com.example.commentservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ReactionCountRepo reactionCountRepo;

    @Autowired
    private ReactionService reactionService;

    /**
     *  fetches post data for a user
     */
    public List<PostDto> getPostData(Integer userId, Long offset, Integer limit) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("provided userId does not exist.");
        }
        List<Post> postData = postRepo.findByUser(user.get().getId(), offset, limit + 1);
        return getPostReactData(postData);
    }

    /**
     * logs like/dislike interaction for a comment
     */
    @Override
    public ReactionCount addInteraction(ReactionDto reactionDto) {
        User user = validate(reactionDto);;
        reactionDto.setContentType(ContentType.POST);
        return reactionService.addInteraction(reactionDto, user);
    }

    /**
     * removes like/dislike interaction for a comment
     */
    @Override
    public ReactionCount removeInteraction(ReactionDto reactionDto) throws CommentServiceException {
        validate(reactionDto);
        reactionDto.setContentType(ContentType.POST);
        return reactionService.removeInteraction(reactionDto);
    }

    /**
     * fetches paginated like/dislike info for a comment
     */
    @Override
    public List<Reactions> getReactionInfo(Long postId, Long offset, Reaction reaction, Integer limit) {
        return reactionService.getReactionInfo(postId, ContentType.POST, reaction, offset, limit);
    }

    /**
     *  validates whether userId and commentId are correctly present in reactionDto
     */
    private User validate(ReactionDto reactionDto) {
        Optional<User> user = userRepo.findById(reactionDto.getUserId());
        if(user.isEmpty()) {
            throw new IllegalArgumentException("provided userId does not exist.");
        }

        Optional<Post> post = postRepo.findById(reactionDto.getId());
        if(post.isEmpty()) {
            throw new IllegalArgumentException("provided postId does not exist.");
        }
        return user.get();
    }

    /**
     * adds dependency between parent and child comments.
     */
    private List<PostDto> getPostReactData(List<Post> postData) {
        List<Long> idlist = postData.stream().map(Post::getId).collect(Collectors.toList());
        List<ReactionCount> reactionCounts = reactionCountRepo.findByContentTypeIdInAndContentType(idlist, ContentType.POST);
        Map<Long, ReactionCount> idToReactionCountMap = reactionCounts.stream()
                .collect(Collectors.toMap(ReactionCount::getId, a->a));

        List<PostDto> postDtos = new ArrayList<>();
        postData.forEach(
                post -> {
                    PostDto postDto = new PostDto();
                    ReactionCount reactionCount = idToReactionCountMap.get(post.getId());
                    Map<Reaction, Integer> reactionMap = new HashMap<>();
                    if(reactionCount == null) {
                        reactionMap.put(Reaction.LIKE, 0);
                        reactionMap.put(Reaction.DISLIKE, 0);
                    } else {
                        reactionMap.put(Reaction.LIKE, reactionCount.getLikeCount());
                        reactionMap.put(Reaction.DISLIKE, reactionCount.getDisLikeCount());
                    }
                    postDto.setReactionMap(reactionMap);
                    postDto.setId(post.getId());
                    postDto.setContent(post.getContent());
                    postDtos.add(postDto);
                }
        );
        return postDtos;
    }

}
