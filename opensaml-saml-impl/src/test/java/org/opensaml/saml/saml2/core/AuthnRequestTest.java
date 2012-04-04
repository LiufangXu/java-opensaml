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

package org.opensaml.saml.saml2.core;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.core.xml.schema.XSBooleanValue;
import org.opensaml.saml.common.BaseComplexSAMLObjectTestCase;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.saml2.core.Audience;
import org.opensaml.saml.saml2.core.AudienceRestriction;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.RequestedAuthnContext;
import org.opensaml.saml.saml2.core.Subject;

/**
 * Tests unmarshalling and marshalling for various request messages.
 */
public class AuthnRequestTest extends BaseComplexSAMLObjectTestCase {

    /**
     * Constructor
     */
    public AuthnRequestTest(){
        elementFile = "/data/org/opensaml/saml/saml2/core/AuthnRequest.xml";
    }
    

    /** {@inheritDoc} */
    @Test
    public void testUnmarshall() {
        AuthnRequest request = (AuthnRequest) unmarshallElement(elementFile);
        
        AssertJUnit.assertNotNull("AuthnRequest was null", request);
        AssertJUnit.assertEquals("ForceAuthn", true, request.isForceAuthn().booleanValue());
        AssertJUnit.assertEquals("AssertionConsumerServiceURL", "http://www.example.com/", request.getAssertionConsumerServiceURL());
        AssertJUnit.assertEquals("AttributeConsumingServiceIndex", 0, request.getAttributeConsumingServiceIndex().intValue());
        AssertJUnit.assertEquals("ProviderName", "SomeProvider", request.getProviderName());
        AssertJUnit.assertEquals("ID", "abe567de6", request.getID());
        AssertJUnit.assertEquals("Version", SAMLVersion.VERSION_20.toString(), request.getVersion().toString());
        AssertJUnit.assertEquals("IssueInstant", new DateTime(2005, 1, 31, 12, 0, 0, 0, ISOChronology.getInstanceUTC()), request.getIssueInstant());
        AssertJUnit.assertEquals("Destination", "http://www.example.com/", request.getDestination());
        AssertJUnit.assertEquals("Consent", "urn:oasis:names:tc:SAML:2.0:consent:obtained", request.getConsent());
        AssertJUnit.assertEquals("Subject/NameID/@NameIdFormat", "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress", request.getSubject().getNameID().getFormat());
        AssertJUnit.assertEquals("Subject/NameID contents", "j.doe@company.com", request.getSubject().getNameID().getValue());
        Audience audience = request.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0);
        AssertJUnit.assertEquals("Conditions/AudienceRestriction[1]/Audience[1] contents", "urn:foo:sp.example.org", audience.getAudienceURI());
        AuthnContextClassRef classRef = (AuthnContextClassRef) request.getRequestedAuthnContext().getAuthnContextClassRefs().get(0);
        AssertJUnit.assertEquals("RequestedAuthnContext/AuthnContextClassRef[1] contents", "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport", classRef.getAuthnContextClassRef());
    }

    /** {@inheritDoc} */
    @Test
    public void testMarshall() {
        NameID nameid = (NameID) buildXMLObject(NameID.DEFAULT_ELEMENT_NAME);
        nameid.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
        nameid.setValue("j.doe@company.com");
        
        Subject subject = (Subject) buildXMLObject(Subject.DEFAULT_ELEMENT_NAME);
        subject.setNameID(nameid);
        
        Audience audience = (Audience) buildXMLObject(Audience.DEFAULT_ELEMENT_NAME);
        audience.setAudienceURI("urn:foo:sp.example.org");
        
        AudienceRestriction ar = (AudienceRestriction) buildXMLObject(AudienceRestriction.DEFAULT_ELEMENT_NAME);
        ar.getAudiences().add(audience);
        
        Conditions conditions = (Conditions) buildXMLObject(Conditions.DEFAULT_ELEMENT_NAME);
        conditions.getAudienceRestrictions().add(ar);
        
        AuthnContextClassRef classRef = (AuthnContextClassRef) buildXMLObject(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
        classRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
        
        RequestedAuthnContext rac = (RequestedAuthnContext) buildXMLObject(RequestedAuthnContext.DEFAULT_ELEMENT_NAME);
        rac.getAuthnContextClassRefs().add(classRef);
        
        AuthnRequest request = (AuthnRequest) buildXMLObject(AuthnRequest.DEFAULT_ELEMENT_NAME);
        request.setSubject(subject);
        request.setConditions(conditions);
        request.setRequestedAuthnContext(rac);
        
        request.setForceAuthn(XSBooleanValue.valueOf("true"));
        request.setAssertionConsumerServiceURL("http://www.example.com/");
        request.setAttributeConsumingServiceIndex(0);
        request.setProviderName("SomeProvider");
        request.setID("abe567de6");
        request.setVersion(SAMLVersion.VERSION_20);
        request.setIssueInstant(new DateTime(2005, 1, 31, 12, 0, 0, 0, ISOChronology.getInstanceUTC()));
        request.setDestination("http://www.example.com/");
        request.setConsent("urn:oasis:names:tc:SAML:2.0:consent:obtained");
        
        assertXMLEquals("Marshalled AuthnRequest", expectedDOM, request);
        
        
    }
}