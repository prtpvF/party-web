package com.review.service.Services;

import com.review.service.Exceptions.PartyNotFoundException;
import com.review.service.Model.Review;
import com.review.service.Rabbit.Consumer.ReviewConsumer;
import com.review.service.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private ReviewConsumer consumer;

    //todo add elastichSearch

    public void createReview(){
        if(isPartyExists(consumer)){
            Review review = new Review();
            setDefaultLikesAndDislikes(review);
            reviewRepository.save(review);
        }
        try {
            throw new PartyNotFoundException("party doesn't exist");
        } catch (PartyNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void setDefaultLikesAndDislikes(Review review){
        review.setDislikeCount(0);
        review.setLikeCount(0);
    }

    private static boolean isPartyExists(ReviewConsumer consumer){
        boolean isExists = consumer.isPartyExists();
        return isExists;
    }
}
