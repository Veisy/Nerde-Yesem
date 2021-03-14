package com.example.nerdeyesem.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.utils.Resource;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final FirebaseEmailAuthRepository firebaseEmailAuthRepository;

    @Inject
    public UserViewModel(FirebaseEmailAuthRepository firebaseEmailAuthRepository) {
        this.firebaseEmailAuthRepository = firebaseEmailAuthRepository;
    }

    public LiveData<Resource<FirebaseUser>> getUser() {
        return firebaseEmailAuthRepository.getUserLiveData();
    }

    //Update userMutableLiveData and return success value.
    public void login(UserModel userModel) {
        firebaseEmailAuthRepository.login(userModel);
    }

    //Sign out current user.
    public void logOut() {
        firebaseEmailAuthRepository.logOut();
    }

}
