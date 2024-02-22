package com.example.commentservice.service.impl;

import com.example.commentservice.Entity.Comment;
import com.example.commentservice.Entity.Post;
import com.example.commentservice.Entity.User;
import com.example.commentservice.model.CommentDto;
import com.example.commentservice.repository.CommentRepo;
import com.example.commentservice.repository.PostRepo;
import com.example.commentservice.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


public class CommentServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PostRepo postRepo;

    @Mock
    private CommentRepo commentRepo;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddComment_successFullCase() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(1L);
        commentDto.setUserId(1);
        commentDto.setParentCommentId(-1L);

        User user = new User();
        user.setId(1);
        Post post = new Post();
        post.setId(1L);

        when(userRepo.findById(commentDto.getUserId())).thenReturn(Optional.of(user));
        when(postRepo.findById(commentDto.getPostId())).thenReturn(Optional.of(post));
        Comment comment = new Comment();
        comment.setId(123L);
        when(commentRepo.save(Mockito.any(Comment.class))).thenReturn(comment);
        commentDto = commentService.addComment(commentDto);
        Long expected = 123L;
        assertThat(commentDto.getId()).isEqualTo(expected);
    }

}
