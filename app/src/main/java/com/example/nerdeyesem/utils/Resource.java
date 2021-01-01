package com.example.nerdeyesem.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Wrapper class for error handling in MVVM architecture.
//We used this wrapper class for both Firebase Auth and Zomato Api call.
public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String message;

    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public enum Status { SUCCESS, ERROR }
}