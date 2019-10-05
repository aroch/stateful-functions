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

package com.ververica.statefun.flink.core.state;

import com.ververica.statefun.sdk.annotations.Persisted;
import com.ververica.statefun.sdk.state.PersistedValue;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class PersistedValues {

  static List<PersistedValue<Object>> findReflectively(@Nullable Object instance) {
    PersistedValues visitor = new PersistedValues();
    visitor.visit(instance);
    return visitor.getPersistedValues();
  }

  private final List<PersistedValue<Object>> persistedValues = new ArrayList<>();

  private void visit(@Nullable Object instance) {
    if (instance == null) {
      return;
    }
    for (Field field : instance.getClass().getDeclaredFields()) {
      visitField(instance, field);
    }
  }

  private List<PersistedValue<Object>> getPersistedValues() {
    return persistedValues;
  }

  private void visitField(@Nonnull Object instance, @Nonnull Field field) {
    @Nonnull Persisted[] annotationsByType = field.getAnnotationsByType(Persisted.class);
    if (annotationsByType.length == 0) {
      return;
    }
    if (field.getType() != PersistedValue.class) {
      throw new IllegalArgumentException(
          "Unknown persisted value type "
              + field.getType()
              + " on "
              + instance.getClass().getName());
    }
    PersistedValue<Object> persistedValue = getPersistedValueReflectively(instance, field);
    if (persistedValue == null) {
      throw new IllegalStateException(
          "The field " + field + " of a " + instance.getClass().getName() + " was not initialized");
    }
    persistedValues.add(persistedValue);
  }

  @SuppressWarnings("unchecked")
  private static PersistedValue<Object> getPersistedValueReflectively(
      Object instance, Field persistedValueField) {
    try {
      persistedValueField.setAccessible(true);
      return (PersistedValue<Object>) persistedValueField.get(instance);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(
          "Unable access field " + persistedValueField.getName() + " of " + instance.getClass());
    }
  }
}
