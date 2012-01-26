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

package org.opensaml.messaging.context;

import net.shibboleth.utilities.java.support.primitive.StringSupport;

/** A {@link Context} intended to be used as a subcontext of a {@link MessageContext}  that carries 
 * some basic metadata about the message. */
public class BasicMessageMetadataSubcontext extends BaseContext {

    /** The ID of the message. */
    private String messageId;

    /** The issue instant, in milliseconds since the epoch, of the message. */
    private long issueInstant;

    /** The issuer of the message. */
    private String issuer;

    /**
     * Gets the ID of the message.
     * 
     * @return ID of the message, may be null
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the ID of the message.
     * 
     * @param newMessageId ID of the message
     */
    public void setMessageId(String newMessageId) {
        messageId = StringSupport.trimOrNull(newMessageId);
    }

    /**
     * Gets the issue instant, in milliseconds since the epoch, of the message.
     * 
     * @return issue instant, in milliseconds since the epoch, of the message
     */
    public long getMessageIssueInstant() {
        return issueInstant;
    }

    /**
     * Sets the issue instant, in milliseconds since the epoch, of the message.
     * 
     * @param messageIssueInstant issue instant, in milliseconds since the epoch, of the message
     */
    public void setMessageIssueInstant(long messageIssueInstant) {
        issueInstant = messageIssueInstant;
    }

    /**
     * Gets the issuer of the message.
     * 
     * @return issuer of the message, may be null
     */
    public String getMessageIssuer() {
        return issuer;
    }

    /**
     * Sets the issuer of the message.
     * 
     * @param messageIssuer issuer of the message
     */
    public void setMessageIssuer(String messageIssuer) {
        issuer = StringSupport.trimOrNull(messageIssuer);
    }
}