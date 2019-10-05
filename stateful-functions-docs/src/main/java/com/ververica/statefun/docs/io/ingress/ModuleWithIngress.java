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

package com.ververica.statefun.docs.io.ingress;

import com.ververica.statefun.docs.io.MissingImplementationException;
import com.ververica.statefun.docs.models.User;
import com.ververica.statefun.sdk.io.IngressIdentifier;
import com.ververica.statefun.sdk.io.IngressSpec;
import com.ververica.statefun.sdk.spi.StatefulFunctionModule;
import java.util.Map;

public class ModuleWithIngress implements StatefulFunctionModule {

  @Override
  public void configure(Map<String, String> globalConfiguration, Binder binder) {
    IngressSpec<User> spec = createIngress(Identifiers.INGRESS);
    binder.bindIngress(spec);
  }

  private IngressSpec<User> createIngress(IngressIdentifier<User> identifier) {
    throw new MissingImplementationException("Replace with your specific ingress");
  }
}
