/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.storage.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.annotation.constraint.Live;
import net.shibboleth.utilities.java.support.annotation.constraint.NonnullAfterInit;
import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

import org.opensaml.storage.AbstractMapBackedStorageService;
import org.opensaml.storage.MutableStorageRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link AbstractMapBackedStorageService} that stores data in-memory in a shared data structure 
 * with no persistence.
 */
public class MemoryStorageService extends AbstractMapBackedStorageService {

    /** Class logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(MemoryStorageService.class);

    /** Map of contexts. */
    @NonnullAfterInit @NonnullElements private Map<String, Map<String, MutableStorageRecord>> contextMap;
    
    /** A shared lock to synchronize access. */
    @NonnullAfterInit private ReadWriteLock lock;

    /** {@inheritDoc} */
    @Override
    protected void doInitialize() throws ComponentInitializationException {
        super.doInitialize();
        contextMap = new HashMap<>();
        lock = new ReentrantReadWriteLock(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void doDestroy() {
        contextMap = null;
        lock = null;
        super.doDestroy();
    }


    /** {@inheritDoc} */
    @Override
    @Nonnull @NonnullElements @Live protected Map<String, Map<String, MutableStorageRecord>> getContextMap() {
        return contextMap;
    }

    /** {@inheritDoc} */
    @Override
    @Nonnull protected ReadWriteLock getLock() {
        return lock;
    }
    
// Checkstyle: AnonInnerLength OFF
    /** {@inheritDoc} */
    @Override
    @Nullable protected TimerTask getCleanupTask() {
        return new TimerTask() {
            
            /** {@inheritDoc} */
            @Override
            public void run() {
                log.debug("Running cleanup task");
                
                final Long now = System.currentTimeMillis();
                final Lock writeLock = getLock().writeLock();
                boolean purged = false;
                
                try {
                    writeLock.lock();
                    
                    Collection<Map<String, MutableStorageRecord>> contexts = getContextMap().values();
                    Iterator<Map<String, MutableStorageRecord>> i = contexts.iterator();
                    while (i.hasNext()) {
                        final Map<String, MutableStorageRecord> context = i.next(); 
                        if (reapWithLock(context, now)) {
                            purged = true;
                            if (context.isEmpty()) {
                                i.remove();
                            }
                        }
                    }
                    
                } finally {
                    writeLock.unlock();
                }
                
                if (purged) {
                    log.debug("Purged expired record(s) from storage");
                } else {
                    log.debug("No expired records found in storage");
                }
            }
        };
    }
// Checkstyle: AnonInnerLength ON

}