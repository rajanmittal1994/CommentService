package com.example.commentservice.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uniqueParentAndChildId", columnNames = {"parent_comment_id", "child_comment_id"})
})
public class ParentChildComment {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="parent_comment_id", nullable=false)
    private Long parentCommentId;

    @Column(name="level", nullable=false)
    private Integer level;

    @Column(name="child_comment_id", nullable=false)
    private Long childCommentId;

}
