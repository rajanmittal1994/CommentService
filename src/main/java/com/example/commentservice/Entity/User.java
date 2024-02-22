package com.example.commentservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class User {

    @Id
    Integer id;

    String firstName;

    String lastName;

    String email;

    Long createdAt;

}
