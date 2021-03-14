package com.example.nerdeyesem.ui.restaurantsdetail;

import androidx.lifecycle.LiveData;

import com.example.nerdeyesem.livedata.SingleLiveEvent;
import com.example.nerdeyesem.network.ZomatoApiService;
import com.example.nerdeyesem.utils.Resource;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ViewModelScoped
public class ZomatoReviewsRepository {

    private final ZomatoApiService zomatoApiService;

    //Extended LiveData class as SingleLiveEvent(in the livedata package) that will only send an update once.
    //We needed it because in some scenarios livedata was updated more than once without updating location.
    //More details and explanations available on class declaration.

    private final SingleLiveEvent<Resource<ReviewsModel>> resourceSingleLiveEvent;

    @Inject
    public ZomatoReviewsRepository(ZomatoApiService zomatoApiService) {
        this.zomatoApiService = zomatoApiService;
        resourceSingleLiveEvent = new SingleLiveEvent<>();
    }

    public void findReviews(Integer resId) {
        resourceSingleLiveEvent.setValue(Resource.loading(null));
        zomatoApiService.getReviews(ZomatoApiService.API_KEY, resId)
                .enqueue(new Callback<ReviewsModel>() {
                    @Override
                    public void onResponse(@NotNull Call<ReviewsModel> call,
                                           @NotNull Response<ReviewsModel> response) {
                        if (!response.isSuccessful()) {
                            resourceSingleLiveEvent.setValue(Resource
                                    .error(String.valueOf(response.code()), null));
                            return;
                        }

                        if (response.body() != null) {
                            // Response successful.
                            resourceSingleLiveEvent.setValue(Resource.success(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ReviewsModel> call,
                                          @NotNull Throwable t) {
                        resourceSingleLiveEvent.setValue(Resource.error(t.getMessage(), null));
                    }
                });
    }

    public LiveData<Resource<ReviewsModel>> getReviews() {
        return resourceSingleLiveEvent;
    }
}
