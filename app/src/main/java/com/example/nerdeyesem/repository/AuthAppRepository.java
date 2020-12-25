package com.example.nerdeyesem.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.nerdeyesem.model.UserModel;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class AuthAppRepository {
    public static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String WRONG_EMAIL = "WRONG_EMAIL";
    public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";
    public static final String LOGGED_OUT = "LOGGED_OUT";
    public static final String USER_NOT_LOGGED_IN = "USER_NOT_LOGGED_IN";

    private final FirebaseAuth firebaseAuth;
    private final MutableLiveData<Resource<FirebaseUser>> userLiveData;

    public AuthAppRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
    }

    //Check if login successful. Get error message if not.
    public void login(UserModel userModel) {
        firebaseAuth.signInWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                userLiveData.postValue(Resource.success(firebaseUser));
            }
        }).addOnFailureListener(e ->
                userLiveData.postValue(Resource.error(getMessageFromErrorCode(e),null)));
    }

    private String getMessageFromErrorCode(Exception e) {
        if (e instanceof FirebaseTooManyRequestsException) {
            return TOO_MANY_REQUEST;
        } else if (e instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) e).getErrorCode();
            switch (errorCode) {
                case "invalid-email":
                case "ERROR_INVALID_EMAIL":
                    return INVALID_EMAIL;
                case "user-not-found":
                case "ERROR_USER_NOT_FOUND":
                    return WRONG_EMAIL;
                case "wrong-password":
                case "ERROR_WRONG_PASSWORD":
                    return WRONG_PASSWORD;
                default:
                    return LOGIN_FAILED;
            }
        } else {
            return LOGIN_FAILED;
        }
    }

    public void logOut() {
       FirebaseAuth.getInstance().signOut();
       userLiveData.postValue(Resource.error(LOGGED_OUT,null));
    }

    public MutableLiveData<Resource<FirebaseUser>> getUserLiveData() {
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(Resource.success(firebaseAuth.getCurrentUser()));
        } else {
            userLiveData.postValue(Resource.error( USER_NOT_LOGGED_IN,null));
        }
        return userLiveData;
    }

}