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

package com.ververica.statefun.docs.match;

import com.ververica.statefun.sdk.Context;
import com.ververica.statefun.sdk.StatefulFunction;

public class FnUserGreeter implements StatefulFunction {

  @Override
  public void invoke(Context context, Object input) {
    if (input instanceof Employee) {
      Employee message = (Employee) input;

      if (message.isManager()) {
        greetManager(context, message);
      } else {
        greetEmployee(context, message);
      }
    } else if (input instanceof Customer) {
      Customer message = (Customer) input;
      greetCustomer(context, message);
    } else {
      throw new IllegalStateException("Unknown message type " + input.getClass());
    }
  }

  private void greetManager(Context context, Employee message) {
    System.out.println("Hello manager " + message.getEmployeeId());
  }

  private void greetEmployee(Context context, Employee message) {
    System.out.println("Hello employee " + message.getEmployeeId());
  }

  private void greetCustomer(Context context, Customer message) {
    System.out.println("Hello customer " + message.getName());
  }
}
