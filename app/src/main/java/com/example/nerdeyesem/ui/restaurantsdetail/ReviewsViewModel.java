package com.example.nerdeyesem.ui.restaurantsdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewsViewModel extends ViewModel {

    private final ZomatoReviewsRepository zomatoReviewsRepository;

    @Inject
    public ReviewsViewModel(ZomatoReviewsRepository zomatoReviewsRepository) {
        this.zomatoReviewsRepository = zomatoReviewsRepository;
    }

    public void findReviews(Integer resId) {
        zomatoReviewsRepository.findReviews(resId);
    }

    public LiveData<Resource<ReviewsModel>> getReviews() {
        return zomatoReviewsRepository.getReviews();
    }

}
