package com.movies2c.backend.service;

import com.movies2c.backend.repositories.PostRepository;
import org.springframework.stereotype.Service;
import com.movies2c.backend.model.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.movies2c.backend.model.PostRating;
import com.movies2c.backend.repositories.PostRatingRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostRatingRepository postRatingRepository;

    public PostService(PostRepository postRepository, PostRatingRepository postRatingRepository) {
        this.postRepository = postRepository;
        this.postRatingRepository = postRatingRepository;
    }


    public Post createPost(Post post) {
        return postRepository.save(post);
    }
    public List<Post> getFeed() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    public Page<Post> getFeed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    public Post ratePost(String postId, String userId, int value) {
        if (value < 1 || value > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostRating rating = postRatingRepository.findByPostIdAndUserId(postId, userId)
                .orElseGet(PostRating::new);

        rating.setPostId(postId);
        rating.setUserId(userId);
        rating.setValue(value);
        rating.setCreatedAt(System.currentTimeMillis());

        postRatingRepository.save(rating);

        // Recalculate avg + count
        List<PostRating> ratings = postRatingRepository.findAllByPostId(postId);

        int count = ratings.size();
        double sum = 0;
        for (PostRating r : ratings) {
            sum += r.getValue();
        }
        double avg = (count == 0) ? 0 : (sum / count);

        post.setRatingCount(count);
        post.setAvgRating(avg);

        return postRepository.save(post);
    }
    public Page<Post> getLatest(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Page<Post> getTopRated(Pageable pageable) {
        return postRepository.findAllByOrderByAvgRatingDescRatingCountDescCreatedAtDesc(pageable);
    }


}


