package com.example.commentservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ReactionDto {

    @NonNull
    private Long id;
    @NonNull
    private Reaction reaction;
    @NonNull
    private Integer userId;
    private ContentType contentType;
}
