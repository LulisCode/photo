package com.jiehun.component.rxjavabaselib;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by zhouyao
 * on 2017/12/12.
 */

public class RxAppCompatActivity extends AppCompatActivity implements RxLifecycleProvider<ActivityEvent> {
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }
    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycleDestroy() {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, ActivityEvent.DESTROY);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }
    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }
    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }
    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }
    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }
    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }
    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }
}