package com.movies2c.backend.repositories;

import com.movies2c.backend.model.PostReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostReplyRepository extends MongoRepository<PostReply, String> {

    Page<PostReply> findAllByPostIdAndParentReplyIdIsNullOrderByCreatedAtDesc(String postId, Pageable pageable);

    Page<PostReply> findAllByPostIdAndParentReplyIdOrderByCreatedAtDesc(String postId, String parentReplyId, Pageable pageable);

    Optional<PostReply> findByIdAndPostId(String id, String postId);
}
