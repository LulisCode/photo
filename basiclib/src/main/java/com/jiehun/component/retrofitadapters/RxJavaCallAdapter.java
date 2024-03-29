/*
 * Copyright (C) 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jiehun.component.retrofitadapters;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import rx.Observable;
import rx.Scheduler;

final class RxJavaCallAdapter<R> implements CallAdapter<Object> {
  private final Type      responseType;
  private final Scheduler scheduler;
  private final boolean   isResult;
  private final boolean   isBody;
  private final boolean   isSingle;
  private final boolean   isCompletable;

  RxJavaCallAdapter(Type responseType, Scheduler scheduler, boolean isResult, boolean isBody,
                    boolean isSingle, boolean isCompletable) {
    this.responseType = responseType;
    this.scheduler = scheduler;
    this.isResult = isResult;
    this.isBody = isBody;
    this.isSingle = isSingle;
    this.isCompletable = isCompletable;
  }

  @Override
  public Type responseType() {
    return responseType;
  }


  @Override
  public <R> Object adapt(Call<R> call) {
      ResponseCallable<R> resultCallable = new ResponseCallable<>(call);

      Observable<?> observable;
      if (isResult) {
        observable = Observable.fromCallable(new ResultCallable<>(resultCallable));
      } else if (isBody) {
        observable = Observable.fromCallable(new BodyCallable<>(resultCallable));
      } else {
        observable = Observable.fromCallable(resultCallable);
      }

      if (scheduler != null) {
        observable = observable.subscribeOn(scheduler);
      }

      if (isSingle) {
        return observable.toSingle();
      }
      if (isCompletable) {
        return CompletableHelper.toCompletable(observable);
      }
      return observable;
  }

  /**
   * Separate static class defers classloading and bytecode verification since Completable is not an
   * RxJava stable API yet.
   */
  private static final class CompletableHelper {
    static Object toCompletable(Observable<?> observable) {
      return observable.toCompletable();
    }
  }
}
