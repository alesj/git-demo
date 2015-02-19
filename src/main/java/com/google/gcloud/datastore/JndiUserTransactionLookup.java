/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gcloud.datastore;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

/**
 * JNDI based UserTransaction lookup.
 */
class JndiUserTransactionLookup implements UserTransactionLookup {
    @Override
    public UserTransaction lookup() {
        try {
            Context context = new InitialContext();
            try {
                return (UserTransaction) context.lookup(System.getProperty("ut.lookup.jndi", "java:comp/UserTransaction"));
            } finally {
                context.close();
            }
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }
}
