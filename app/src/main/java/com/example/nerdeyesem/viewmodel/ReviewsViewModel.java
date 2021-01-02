package com.example.nerdeyesem.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.model.ReviewsModel;
import com.example.nerdeyesem.repository.ZomatoReviewsRepository;
import com.example.nerdeyesem.utils.Resource;

public class ReviewsViewModel extends ViewModel {

    private final ZomatoReviewsRepository zomatoReviewsRepository;

    public ReviewsViewModel() {
        zomatoReviewsRepository = new ZomatoReviewsRepository();
    }

    public void findReviews(Integer resId) {
        zomatoReviewsRepository.findReviews(resId);
    }

    public LiveData<Resource<ReviewsModel>> getReviews() {
        return zomatoReviewsRepository.getReviews();
    }

}
