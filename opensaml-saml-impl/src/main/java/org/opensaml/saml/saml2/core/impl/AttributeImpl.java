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

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.util.AttributeMap;
import org.opensaml.core.xml.util.XMLObjectChildrenList;
import org.opensaml.saml.common.AbstractSAMLObject;
import org.opensaml.saml.saml2.core.Attribute;

/**
 * Concrete implementation of {@link org.opensaml.saml.saml2.core.Attribute}.
 */
public class AttributeImpl extends AbstractSAMLObject implements Attribute {

    /** Name of the attribute. */
    private String name;

    /** Format of the name of the attribute. */
    private String nameFormat;

    /** Human readable name of the attribute. */
    private String friendlyName;

    /** "anyAttribute" attributes. */
    private AttributeMap unknownAttributes;

    /** List of attribute values for this attribute. */
    private final XMLObjectChildrenList<XMLObject> attributeValues;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AttributeImpl(final String namespaceURI, final String elementLocalName, final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        attributeValues = new XMLObjectChildrenList<>(this);
    }

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public void setName(final String n) {
        name = prepareForAssignment(name, n);
    }

    /** {@inheritDoc} */
    public String getNameFormat() {
        return nameFormat;
    }

    /** {@inheritDoc} */
    public void setNameFormat(final String format) {
        nameFormat = prepareForAssignment(nameFormat, format);
    }

    /** {@inheritDoc} */
    public String getFriendlyName() {
        return friendlyName;
    }

    /** {@inheritDoc} */
    public void setFriendlyName(final String fname) {
        friendlyName = prepareForAssignment(friendlyName, fname);
    }

    /**
     * {@inheritDoc}
     */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getAttributeValues() {
        return attributeValues;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        final ArrayList<XMLObject> children = new ArrayList<>();

        children.addAll(attributeValues);

        return Collections.unmodifiableList(children);
    }
}