/*
 * Copyright 2019 Ververica GmbH.
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

package com.ververica.statefun.flink.core.feedback;

import com.ververica.statefun.flink.core.queue.Locks;
import com.ververica.statefun.flink.core.queue.MpscQueue;
import java.util.Deque;

public final class LockFreeBatchFeedbackQueue<ElementT> implements FeedbackQueue<ElementT> {
  private static final int INITIAL_BUFFER_SIZE = 32 * 1024; // 32k

  private final MpscQueue<ElementT> queue = new MpscQueue<>(INITIAL_BUFFER_SIZE, Locks.spinLock());

  @Override
  public boolean addAndCheckIfWasEmpty(ElementT element) {
    final int size = queue.add(element);
    return size == 1;
  }

  @Override
  public Deque<ElementT> drainAll() {
    return queue.drainAll();
  }
}
