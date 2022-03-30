package com.yenaly.cqupttoolbox.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/29 029 20:15
 * @Description : Description...
 */
public class SingleLiveData<T> extends MutableLiveData<T> {

    private final HashMap<Observer<? super T>, AtomicBoolean> mPendingMap = new HashMap<>();

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        Lifecycle lifecycle = owner.getLifecycle();
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        mPendingMap.put(observer, new AtomicBoolean(false));
        lifecycle.addObserver((LifecycleEventObserver) (source, event) -> {
            if (event == Lifecycle.Event.ON_DESTROY) {
                mPendingMap.remove(observer);
            }
        });
        super.observe(owner, t -> {
            AtomicBoolean pending = mPendingMap.get(observer);
            if (pending != null && pending.compareAndSet(true, false)) {
                observer.onChanged(t);
            }
        });
    }

    @MainThread
    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        mPendingMap.put(observer, new AtomicBoolean(false));
        super.observeForever(observer);
    }

    @MainThread
    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        mPendingMap.remove(observer);
        super.removeObserver(observer);
    }

    @MainThread
    @Override
    public void removeObservers(@NonNull LifecycleOwner owner) {
        mPendingMap.clear();
        super.removeObservers(owner);
    }

    @MainThread
    @Override
    public void setValue(@Nullable T t) {
        for (AtomicBoolean value : mPendingMap.values()) {
            value.set(true);
        }
        super.setValue(t);
    }
}
