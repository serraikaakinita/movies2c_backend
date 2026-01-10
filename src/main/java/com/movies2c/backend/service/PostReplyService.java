package com.movies2c.backend.service;

import com.movies2c.backend.model.PostReply;
import com.movies2c.backend.repositories.PostReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostReplyService {

    private final PostReplyRepository postReplyRepository;

    public PostReplyService(PostReplyRepository postReplyRepository) {
        this.postReplyRepository = postReplyRepository;
    }

    public PostReply createReply(String postId, String userId, String userName, String text, String parentReplyId) {
        if (text == null || text.trim().isEmpty()) {
            throw new RuntimeException("Reply text is required");
        }

        String cleanedText = text.trim();

        if (cleanedText.length() > 300) {
            throw new RuntimeException("Reply text must be at most 300 characters");
        }


        PostReply reply = new PostReply();
        reply.setPostId(postId);
        reply.setUserId(userId);
        reply.setUserName(userName);
        reply.setText(cleanedText);
        reply.setParentReplyId(parentReplyId);
        reply.setCreatedAt(System.currentTimeMillis());

        return postReplyRepository.save(reply);
    }

    public Page<PostReply> getTopLevelReplies(String postId, Pageable pageable) {
        return postReplyRepository.findAllByPostIdAndParentReplyIdIsNullOrderByCreatedAtDesc(postId, pageable);
    }

    public Page<PostReply> getChildReplies(String postId, String parentReplyId, Pageable pageable) {
        return postReplyRepository.findAllByPostIdAndParentReplyIdOrderByCreatedAtDesc(postId, parentReplyId, pageable);
    }
}
