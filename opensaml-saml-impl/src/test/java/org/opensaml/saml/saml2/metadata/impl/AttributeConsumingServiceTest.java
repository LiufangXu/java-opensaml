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

package org.opensaml.saml.saml2.metadata.impl;

import org.opensaml.core.xml.schema.XSBooleanValue;
import org.opensaml.core.xml.XMLObjectProviderBaseTestCase;
import org.opensaml.saml.saml2.metadata.AttributeConsumingService;
import org.opensaml.saml.saml2.metadata.RequestedAttribute;
import org.opensaml.saml.saml2.metadata.ServiceDescription;
import org.opensaml.saml.saml2.metadata.ServiceName;

/**
 * Test case for creating, marshalling, and unmarshalling
 * {@link org.opensaml.saml.saml2.metadata.impl.AssertionConsumerServiceImpl}.
 */
public class AttributeConsumingServiceTest extends XMLObjectProviderBaseTestCase {
    
    protected int expectedIndex;
    protected XSBooleanValue expectedIsDefault;
    protected int expectedServiceNameCount;
    protected int expectedServiceDecsriptionCount;
    protected int expectedRequestedAttributeCount;
    
    /**
     * Constructor
     */
    public AttributeConsumingServiceTest() {
        singleElementFile = "/data/org/opensaml/saml/saml2/metadata/impl/AttributeConsumingService.xml";
        singleElementOptionalAttributesFile = "/data/org/opensaml/saml/saml2/metadata/impl/AttributeConsumingServiceOptionalAttributes.xml";
        childElementsFile = "/data/org/opensaml/saml/saml2/metadata/impl/AttributeConsumingServiceChildElements.xml";
    }
    
    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
        
        expectedIndex = 1;
        expectedIsDefault = new XSBooleanValue(Boolean.TRUE, false);
        expectedServiceNameCount = 2;
        expectedServiceDecsriptionCount = 3;
        expectedRequestedAttributeCount = 1;
        
    }

    /** {@inheritDoc} */
    public void testSingleElementUnmarshall() {
        AttributeConsumingService service = (AttributeConsumingService) unmarshallElement(singleElementFile);
        
        assertEquals("Index was not expected value", expectedIndex, service.getIndex());
    }

    /** {@inheritDoc} */
    public void testSingleElementOptionalAttributesUnmarshall() {
        AttributeConsumingService service = (AttributeConsumingService) unmarshallElement(singleElementOptionalAttributesFile);
        
        assertEquals("Index was not expected value", expectedIndex, service.getIndex());
        assertEquals("isDefault was not expected value", expectedIsDefault, service.isDefaultXSBoolean());
    }
    
    /** {@inheritDoc} */

    public void testChildElementsUnmarshall(){
        AttributeConsumingService service = (AttributeConsumingService) unmarshallElement(childElementsFile);
        
        assertEquals("<ServiceName> count", expectedServiceNameCount, service.getNames().size());
        assertEquals("<ServiceDescription> count", expectedServiceDecsriptionCount, service.getDescriptions().size());
        assertEquals("<ReqestAttribute> count", expectedRequestedAttributeCount, service.getRequestAttributes().size());
       
    }

    /** {@inheritDoc} */
    public void testSingleElementMarshall() {
        AttributeConsumingService service = (AttributeConsumingService) buildXMLObject(AttributeConsumingService.DEFAULT_ELEMENT_NAME);
        
        service.setIndex(expectedIndex);

        assertXMLEquals(expectedDOM, service);
    }

    /** {@inheritDoc} */
    public void testSingleElementOptionalAttributesMarshall() {
        AttributeConsumingService service = (AttributeConsumingService) buildXMLObject(AttributeConsumingService.DEFAULT_ELEMENT_NAME);
        
        service.setIndex(expectedIndex);
        service.setIsDefault(expectedIsDefault);

        assertXMLEquals(expectedOptionalAttributesDOM, service);
    }
    

    public void testChildElementsMarshall()
    {
        AttributeConsumingService service = (AttributeConsumingService) buildXMLObject(AttributeConsumingService.DEFAULT_ELEMENT_NAME);
        
        service.setIndex(expectedIndex);
        
        for (int i = 0; i < expectedServiceNameCount; i++) {
            service.getNames().add((ServiceName) buildXMLObject(ServiceName.DEFAULT_ELEMENT_NAME));
        }

        for (int i = 0; i < expectedServiceDecsriptionCount; i++) {
            service.getDescriptions().add((ServiceDescription) buildXMLObject(ServiceDescription.DEFAULT_ELEMENT_NAME));
        }

        service.getRequestAttributes().add((RequestedAttribute) buildXMLObject(RequestedAttribute.DEFAULT_ELEMENT_NAME));

        assertXMLEquals(expectedChildElementsDOM, service);
    
    }
    
    /**
     * Test the proper behavior of the XSBooleanValue attributes.
     */
    public void testXSBooleanAttributes() {
        AttributeConsumingService acs = 
            (AttributeConsumingService) buildXMLObject(AttributeConsumingService.DEFAULT_ELEMENT_NAME);
        
        // isDefault attribute
        acs.setIsDefault(Boolean.TRUE);
        assertEquals("Unexpected value for boolean attribute found", Boolean.TRUE, acs.isDefault());
        assertNotNull("XSBooleanValue was null", acs.isDefaultXSBoolean());
        assertEquals("XSBooleanValue was unexpected value", new XSBooleanValue(Boolean.TRUE, false),
                acs.isDefaultXSBoolean());
        assertEquals("XSBooleanValue string was unexpected value", "true", acs.isDefaultXSBoolean().toString());
        
        acs.setIsDefault(Boolean.FALSE);
        assertEquals("Unexpected value for boolean attribute found", Boolean.FALSE, acs.isDefault());
        assertNotNull("XSBooleanValue was null", acs.isDefaultXSBoolean());
        assertEquals("XSBooleanValue was unexpected value", new XSBooleanValue(Boolean.FALSE, false),
                acs.isDefaultXSBoolean());
        assertEquals("XSBooleanValue string was unexpected value", "false", acs.isDefaultXSBoolean().toString());
        
        acs.setIsDefault((Boolean) null);
        assertEquals("Unexpected default value for boolean attribute found", Boolean.FALSE, acs.isDefault());
        assertNull("XSBooleanValue was not null", acs.isDefaultXSBoolean());
    }
}
