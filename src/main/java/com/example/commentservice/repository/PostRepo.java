package com.example.commentservice.repository;

import com.example.commentservice.Entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends CrudRepository<Post, Long> {

    @Query(nativeQuery = true, value = "select * from post where user_id= ?1 and id > ?2 limit ?3")
    List<Post> findByUser(Integer user, Long offset, Integer limit);
}
