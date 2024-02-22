package com.example.commentservice.controller;

import com.example.commentservice.Entity.ReactionCount;
import com.example.commentservice.Entity.Reactions;
import com.example.commentservice.exception.CommentServiceException;
import com.example.commentservice.model.ApiResponse;
import com.example.commentservice.model.CommentDto;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import com.example.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/react")
    public ReactionCount react(@RequestBody ReactionDto reactionDto) {
        return commentService.addInteraction(reactionDto);
    }

    @PostMapping("/unreact")
    public ReactionCount unreact(@RequestBody ReactionDto reactionDto) throws CommentServiceException {
        try{
            return commentService.removeInteraction(reactionDto);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "abcd", e);
        }
    }

    @PostMapping("/addComment")
    public CommentDto addComment(@RequestBody CommentDto comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/getRepliesToComment/{comment_id}")
    public ApiResponse<List<CommentDto>> getRepliesInfo(@PathVariable(value = "comment_id") Long commentId,
                                        @RequestParam(value = "offset", defaultValue = "0") Long offset,
                                        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        List<CommentDto> data = commentService.getRepliesInfo(commentId, offset, limit);
        return getApiResponse(data, limit);
    }

    @GetMapping("/getCommentForPost/{post_id}")
    public ApiResponse<List<CommentDto>> getCommentInfo(@PathVariable(value = "post_id") Long postId,
                                        @RequestParam(value = "offset", defaultValue = "0") Long offset,
                                        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        List<CommentDto> data = commentService.getCommentInfoForPost(postId, offset, limit);
        return getApiResponse(data, limit);
    }

    private ApiResponse<List<CommentDto>> getApiResponse(List<CommentDto> data, Integer limit) {
        boolean hasNext = false;
        if(data.size() > limit) {
            data.remove(data.size() - 1);
            hasNext = true;
        }
        return new ApiResponse<>(data, hasNext);
    }

    @GetMapping("/reactionInfo/{comment_id}")
    public ApiResponse<List<Reactions>> getReactionInfo(@PathVariable("comment_id") Long commentId,
                                                        @RequestParam(value = "reaction") Reaction reaction,
                                                        @RequestParam(value = "offset", defaultValue = "0") Long offset,
                                                        @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        boolean hasNext = false;
        List<Reactions> data = commentService.getReactionInfo(commentId, reaction, offset, limit + 1);
        if(data.size() > limit) {
            data.remove(data.size() - 1);
            hasNext = true;
        }
        return new ApiResponse<>(data, hasNext);
    }

}
