/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
package org.opensaml.saml2.metadata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Extensions;
import org.opensaml.saml2.metadata.AffiliateMember;
import org.opensaml.saml2.metadata.AffiliationDescriptor;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.metadata.AffiliationDescriptor}.
 */
public class AffiliationDescriptorImpl extends AbstractSignableSAMLObject implements AffiliationDescriptor {

    /** ID of the owner of this affiliation */
    private String ownerID;
    
    /** validUntil attribute */
    private DateTime validUntil;
    
    /** cacheDurection attribute */
    private Long cacheDuration;
    
    /** Extensions child */
    private Extensions extensions;
    
    /** Members of this affiliation */
    private XMLObjectChildrenList<AffiliateMember> members;
    
    /** Key descriptors for this role */
    private XMLObjectChildrenList<KeyDescriptor> keyDescriptors;
    
    /**
     * Constructor
     */
    public AffiliationDescriptorImpl(){
        super(SAMLConstants.SAML20MD_NS, AffiliationDescriptor.LOCAL_NAME);
        setElementNamespacePrefix(SAMLConstants.SAML20MD_PREFIX);
        members = new XMLObjectChildrenList<AffiliateMember>(this);
        keyDescriptors = new XMLObjectChildrenList<KeyDescriptor>(this);
    }

    /*
     * @see org.opensaml.saml2.metadata.AffiliationDescriptor#getOwnerID()
     */
    public String getOwnerID() {
        return ownerID;
    }

    /*
     * @see org.opensaml.saml2.metadata.AffiliationDescriptor#setOwnerID(java.lang.String)
     */
    public void setOwnerID(String newOwnerID) {
        ownerID = prepareForAssignment(ownerID, newOwnerID);
    }
    
    /*
     * @see org.opensaml.saml2.common.TimeBoundSAMLObject#isValid()
     */
    public boolean isValid() {
        return validUntil.isBeforeNow();
    }
    
    /*
     * @see org.opensaml.saml2.common.TimeBoundSAMLObject#getValidUntil()
     */
    public DateTime getValidUntil() {
        return validUntil;
    }

    /*
     * @see org.opensaml.saml2.common.TimeBoundSAMLObject#setValidUntil(java.util.GregorianCalendar)
     */
    public void setValidUntil(DateTime validUntil) {
        this.validUntil = prepareForAssignment(this.validUntil, validUntil.withZone(DateTimeZone.UTC));
    }

    /*
     * @see org.opensaml.saml2.common.CacheableSAMLObject#getCacheDuration()
     */
    public Long getCacheDuration() {
        return cacheDuration;
    }

    /*
     * @see org.opensaml.saml2.common.CacheableSAMLObject#setCacheDuration(java.lang.Long)
     */
    public void setCacheDuration(Long duration) {
        cacheDuration = prepareForAssignment(cacheDuration, duration);
    }
    
    /*
     * @see org.opensaml.saml2.metadata.AffiliationDescriptor#getExtensions()
     */
    public Extensions getExtensions() {
        return extensions;
    }

    /*
     * @see org.opensaml.saml2.metadata.AffiliationDescriptor#setExtensions(org.opensaml.saml2.core.Extensions)
     */
    public void setExtensions(Extensions extensions) throws IllegalArgumentException {
        this.extensions = prepareForAssignment(this.extensions, extensions);
    }

    /*
     * @see org.opensaml.saml2.metadata.AffiliationDescriptor#getMembers()
     */
    public List<AffiliateMember> getMembers() {
        return members;
    }

    /*
     * @see org.opensaml.saml2.metadata.KeyDescriptorDescriptorComp#getKeyDescriptors()
     */
    public List<KeyDescriptor> getKeyDescriptors() {
        return keyDescriptors;
    }
    
    /*
     * @see org.opensaml.common.SAMLObject#getOrderedChildren()
     */
    public List<SAMLObject> getOrderedChildren() {
        ArrayList<SAMLObject> children = new ArrayList<SAMLObject>();
        
        children.add(getExtensions());

        children.addAll(getMembers());
        
        children.addAll(getKeyDescriptors());
        
        return Collections.unmodifiableList(children);
    }
}