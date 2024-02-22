package com.example.commentservice.Entity;

import com.example.commentservice.model.ContentType;
import com.example.commentservice.model.Reaction;
import com.example.commentservice.model.ReactionDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uniqueContentTypeAndIdAndUserAndReaction", columnNames = {"contentType",
                "contentTypeId", "userId", "reaction"})
})
public class Reactions {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private ContentType contentType;

    private Long contentTypeId;

    private Long createdAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private Reaction reaction;

    public Reactions(ReactionDto reactionDto, User user) {
        this.contentType = reactionDto.getContentType();
        this.contentTypeId = reactionDto.getId();
        this.createdAt = System.currentTimeMillis();
        this.user = user;
        this.reaction = reactionDto.getReaction();
    }
}
