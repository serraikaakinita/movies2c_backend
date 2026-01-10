package com.movies2c.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PostReplies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostReply {

    @Id
    private String id;

    private String postId;

    private String userId;
    private String userName;

    private String text;

    private String parentReplyId;

    private long createdAt;
}
