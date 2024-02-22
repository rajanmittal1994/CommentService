package com.example.commentservice.model;

import lombok.Data;

import java.util.Map;

@Data
public class PostDto {

    private String content;

    private Long id;

    private Map<Reaction,Integer> reactionMap;

}
