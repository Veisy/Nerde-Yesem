package com.example.nerdeyesem.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.model.UserModel;
import com.example.nerdeyesem.repository.FirebaseEmailAuthRepository;
import com.example.nerdeyesem.utils.Resource;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {
    private final FirebaseEmailAuthRepository firebaseEmailAuthRepository;

    public UserViewModel() {
        firebaseEmailAuthRepository = new FirebaseEmailAuthRepository();
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
