package com.example.commentservice.model;

public enum Reaction {
    LIKE(1),
    DISLIKE(2);

    final int val;
    Reaction(int val) {
        this.val = val;
    }
}
