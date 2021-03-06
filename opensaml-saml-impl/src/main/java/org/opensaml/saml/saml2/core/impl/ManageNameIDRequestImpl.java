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

/**
 * 
 */

package org.opensaml.saml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.saml2.core.EncryptedID;
import org.opensaml.saml.saml2.core.ManageNameIDRequest;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.NewEncryptedID;
import org.opensaml.saml.saml2.core.NewID;
import org.opensaml.saml.saml2.core.Terminate;

/**
 * A concrete implementation of {@link org.opensaml.saml.saml2.core.ManageNameIDRequest}.
 */
public class ManageNameIDRequestImpl extends RequestAbstractTypeImpl implements ManageNameIDRequest {

    /** NameID child element. */
    private NameID nameID;

    /** EncryptedID child element. */
    private EncryptedID encryptedID;

    /** NewID child element. */
    private NewID newID;

    /** NameID child element. */
    private NewEncryptedID newEncryptedID;

    /** Terminate child element. */
    private Terminate terminate;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected ManageNameIDRequestImpl(final String namespaceURI, final String elementLocalName,
            final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public NameID getNameID() {
        return this.nameID;
    }

    /** {@inheritDoc} */
    public void setNameID(final NameID newNameID) {
        this.nameID = prepareForAssignment(this.nameID, newNameID);
    }

    /** {@inheritDoc} */
    public EncryptedID getEncryptedID() {
        return this.encryptedID;
    }

    /** {@inheritDoc} */
    public void setEncryptedID(final EncryptedID newEncID) {
        this.encryptedID = prepareForAssignment(this.encryptedID, newEncID);
    }

    /** {@inheritDoc} */
    public NewID getNewID() {
        return this.newID;
    }

    /** {@inheritDoc} */
    public void setNewID(final NewID newNewID) {
        this.newID = prepareForAssignment(this.newID, newNewID);
    }

    /** {@inheritDoc} */
    public NewEncryptedID getNewEncryptedID() {
        return this.newEncryptedID;
    }

    /** {@inheritDoc} */
    public void setNewEncryptedID(final NewEncryptedID newNewEncryptedID) {
        this.newEncryptedID = prepareForAssignment(this.newEncryptedID, newNewEncryptedID);
    }

    /** {@inheritDoc} */
    public Terminate getTerminate() {
        return this.terminate;
    }

    /** {@inheritDoc} */
    public void setTerminate(final Terminate newTerminate) {
        this.terminate = prepareForAssignment(this.terminate, newTerminate);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        final ArrayList<XMLObject> children = new ArrayList<>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }
        if (nameID != null) {
            children.add(nameID);
        }
        if (encryptedID != null) {
            children.add(encryptedID);
        }
        if (newID != null) {
            children.add(newID);
        }
        if (newEncryptedID != null) {
            children.add(newEncryptedID);
        }
        if (terminate != null) {
            children.add(terminate);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}