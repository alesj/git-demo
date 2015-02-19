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

import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * Handle external transactions; e.g. JTA, JTS.
 */
class ExternalTransactions {
    private static final Logger log = Logger.getLogger(ExternalTransactions.class.getName());

    static interface Transaction extends java.io.Serializable {
        Transaction create();
        void begin();
        void commit();
        void rollback();
    }

    private static UserTransactionLookup lookupUserTransaction() {
        ServiceLoader<UserTransactionLookup> loader = ServiceLoader.load(UserTransactionLookup.class);
        if (loader.iterator().hasNext()) return loader.iterator().next();
        return new JndiUserTransactionLookup();
    }

    static class JTATransaction implements Transaction {
        private static final long serialVersionUID = 1L;

        private transient UserTransactionLookup lookup;
        private boolean isNew;

        private synchronized UserTransactionLookup getLookup() {
            if (lookup == null) {
                lookup = lookupUserTransaction();
            }
            return lookup;
        }

        public Transaction create() {
            return new JTATransaction();
        }

        public void begin() {
            try {
                UserTransaction ut = getLookup().lookup();
                int status = ut.getStatus();
                // pretty simple check atm, could be better
                if (status != Status.STATUS_ACTIVE) {
                    ut.begin();
                    isNew = true;
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        public void commit() {
            try {
                if (isNew) {
                    UserTransaction ut = getLookup().lookup();
                    ut.commit();
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Cannot commit JTA transaction.", e);
            }
        }

        public void rollback() {
            try {
                UserTransaction ut = getLookup().lookup();
                if (isNew) {
                    ut.rollback();
                } else {
                    ut.setRollbackOnly();
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Cannot rollback JTA transaction.", e);
            }
        }
    }

    static class JTSTransaction implements Transaction {
        private static final long serialVersionUID = 1L;

        public Transaction create() {
            return new JTSTransaction();
        }

        public void begin() {

        }

        public void commit() {

        }

        public void rollback() {

        }
    }

}
