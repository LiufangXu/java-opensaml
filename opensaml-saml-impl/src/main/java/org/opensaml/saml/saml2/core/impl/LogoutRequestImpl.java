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

package org.opensaml.saml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.util.XMLObjectChildrenList;
import org.opensaml.saml.saml2.core.BaseID;
import org.opensaml.saml.saml2.core.EncryptedID;
import org.opensaml.saml.saml2.core.LogoutRequest;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.SessionIndex;

/**
 * A concrete implementation of {@link org.opensaml.saml.saml2.core.LogoutRequest}.
 */
public class LogoutRequestImpl extends RequestAbstractTypeImpl implements LogoutRequest {

    /** Reason attribute. */
    private String reason;

    /** NotOnOrAfter attribute. */
    private DateTime notOnOrAfter;

    /** BaseID child element. */
    private BaseID baseID;

    /** NameID child element. */
    private NameID nameID;
    
    /** EncryptedID child element. */
    private EncryptedID encryptedID;


    /** SessionIndex child elements. */
    private final XMLObjectChildrenList<SessionIndex> sessionIndexes;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected LogoutRequestImpl(final String namespaceURI, final String elementLocalName,
            final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        sessionIndexes = new XMLObjectChildrenList<>(this);
    }

    /** {@inheritDoc} */
    public String getReason() {
        return this.reason;
    }

    /** {@inheritDoc} */
    public void setReason(final String newReason) {
        this.reason = prepareForAssignment(this.reason, newReason);
    }

    /** {@inheritDoc} */
    public DateTime getNotOnOrAfter() {
        return this.notOnOrAfter;
    }

    /** {@inheritDoc} */
    public void setNotOnOrAfter(final DateTime newNotOnOrAfter) {
        this.notOnOrAfter = prepareForAssignment(this.notOnOrAfter, newNotOnOrAfter);
    }

    /** {@inheritDoc} */
    public BaseID getBaseID() {
        return baseID;
    }

    /** {@inheritDoc} */
    public void setBaseID(final BaseID newBaseID) {
        baseID = prepareForAssignment(baseID, newBaseID);
    }

    /** {@inheritDoc} */
    public NameID getNameID() {
        return nameID;
    }

    /** {@inheritDoc} */
    public void setNameID(final NameID newNameID) {
        nameID = prepareForAssignment(nameID, newNameID);
    }

    /** {@inheritDoc} */
    public EncryptedID getEncryptedID() {
        return this.encryptedID;
    }

    /** {@inheritDoc} */
    public void setEncryptedID(final EncryptedID newEncryptedID) {
        this.encryptedID = prepareForAssignment(this.encryptedID, newEncryptedID);
    }

    /** {@inheritDoc} */
    public List<SessionIndex> getSessionIndexes() {
        return sessionIndexes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        final ArrayList<XMLObject> children = new ArrayList<>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }

        if (baseID != null) {
            children.add(baseID);
        }

        if (nameID != null) {
            children.add(nameID);
        }
        
        if (encryptedID != null) {
            children.add(encryptedID);
        }

        children.addAll(sessionIndexes);

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}