package com.example.nerdeyesem.livedata;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

// This SingleLiveEvent class was created to make sure that observer of restaurants fired once
// and restaurants updates only when location is updated.
// It is a LiveData that will only send an update once.
// We needed it because in some scenarios restaurants was updated more than once without updating location.
// Possible Problem: https://medium.com/better-programming/how-to-fix-a-serious-problem-in-livedata-android-594a3f18e981
// My Solution: https://github.com/NordicSemiconductor/Android-nRF-Blinky/blob/master/app/src/main/java/no/nordicsemi/android/blinky/viewmodels/SingleLiveEvent.java
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean pending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull final LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, t -> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    @Override
    public void setValue(@Nullable final T t) {
        pending.set(true);
        super.setValue(t);
    }
}
