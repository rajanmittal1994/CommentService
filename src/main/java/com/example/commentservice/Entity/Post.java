package com.example.commentservice.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

@Data
@Lazy
@Entity(name = "post")
@NoArgsConstructor
public class Post {

    @Id
    private Long id;

    private String content;

    Long createdAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

}
