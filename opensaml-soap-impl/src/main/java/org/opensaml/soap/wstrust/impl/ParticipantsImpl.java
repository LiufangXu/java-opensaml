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

package org.opensaml.soap.wstrust.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.soap.wstrust.Participant;
import org.opensaml.soap.wstrust.Participants;
import org.opensaml.soap.wstrust.Primary;

/**
 * ParticipantsImpl.
 * 
 */
public class ParticipantsImpl extends AbstractWSTrustObject implements Participants {

    /** The {@link Primary} child element. */
    private Primary primary;

    /** The list of {@link Participant} child elements. */
    private List<Participant> participants;
    
    /** Wildcard child elements. */
    private IndexedXMLObjectChildrenList<XMLObject> unknownChildren;

    /**
     * Constructor.
     * 
     * @param namespaceURI The namespace of the element
     * @param elementLocalName The local name of the element
     * @param namespacePrefix The namespace prefix of the element
     */
    public ParticipantsImpl(final String namespaceURI, final String elementLocalName, final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        participants = new ArrayList<>();
        unknownChildren = new IndexedXMLObjectChildrenList<>(this);
    }

    /** {@inheritDoc} */
    public Primary getPrimary() {
        return primary;
    }

    /** {@inheritDoc} */
    public void setPrimary(final Primary newPrimary) {
        primary = prepareForAssignment(primary, newPrimary);
    }

    /** {@inheritDoc} */
    public List<Participant> getParticipants() {
        return participants;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(final QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        final List<XMLObject> children = new ArrayList<>();
        if (primary != null) {
            children.add(primary);
        }
        
        children.addAll(participants);
        
        children.addAll(unknownChildren);
        
        return Collections.unmodifiableList(children);
    }

}
