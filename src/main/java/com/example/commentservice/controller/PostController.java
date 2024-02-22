package com.example.commentservice.controller;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.ApiResponse;
import com.example.commentservice.model.PostDto;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import com.example.commentservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/data")
    public ApiResponse<List<PostDto>> getPostData(@RequestParam ("user_id") Integer userId,
                                                  @RequestParam(value = "offset", defaultValue = "0") Long offset,
                                                  @RequestParam(value = "limit", defaultValue = "20") Integer limit) {

        boolean hasNext = false;
        List<PostDto> postData = postService.getPostData(userId, offset, limit + 1);
        if(postData.size() > limit) {
            postData.remove(postData.size() - 1);
            hasNext = true;
        }

        return new ApiResponse<>(postData, hasNext);
    }

    @PostMapping("/react")
    public ReactionCount react(@RequestBody ReactionDto reactionDto) {
        return postService.addInteraction(reactionDto);
    }

    @PostMapping("/unreact")
    public ReactionCount unreact(@RequestBody ReactionDto reactionDto) throws CommentServiceException {
        return postService.removeInteraction(reactionDto);
    }

    @GetMapping("/reactionInfo/{post_id}")
    public ApiResponse<List<Reactions>> getReactionInfo(@PathVariable("post_id") Long postId,
                                                        @RequestParam(value = "reaction") Reaction reaction,
                                                        @RequestParam(value = "offset", defaultValue = "0") Long offset,
                                                        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
            boolean hasNext = false;
            List<Reactions> data = postService.getReactionInfo(postId, offset, reaction, limit + 1);
            if(data.size() > limit) {
                data.remove(data.size() - 1);
                hasNext = true;
            }
            return new ApiResponse<>(data, hasNext);
    }

}
