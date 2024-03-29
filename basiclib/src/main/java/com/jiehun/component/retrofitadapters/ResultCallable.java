/*
 * Copyright (C) 2016 Square, Inc.
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

import java.util.concurrent.Callable;

import retrofit2.Response;

final class ResultCallable<R> implements Callable<Result<R>> {
  private final Callable<Response<R>> responseCallable;

  ResultCallable(Callable<Response<R>> responseCallable) {
    this.responseCallable = responseCallable;
  }

  @Override
  public Result<R> call() {
    try {
      return Result.response(responseCallable.call());
    } catch (Throwable t) {
      return Result.error(t);
    }
  }
}
