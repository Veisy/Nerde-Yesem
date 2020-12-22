package com.example.nerdeyesem.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.model.User;
import com.example.nerdeyesem.repository.AuthAppRepository;
import com.example.nerdeyesem.repository.Resource;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {
    private final AuthAppRepository authAppRepository;
    private final LiveData<Resource<FirebaseUser>> userMutableLiveData;

    public UserViewModel() {
        authAppRepository = new AuthAppRepository();
        userMutableLiveData = authAppRepository.getUserLiveData();
    }

    public LiveData<Resource<FirebaseUser>> getUser() {
        return userMutableLiveData;
    }

    //Update userMutableLiveData and return success value.
    public void login(User user) {
        authAppRepository.login(user);
    }

    //Sign out current user.
    public void logOut() {
        authAppRepository.logOut();
    }

}
