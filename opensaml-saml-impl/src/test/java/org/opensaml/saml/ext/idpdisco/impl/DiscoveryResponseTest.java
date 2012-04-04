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

package org.opensaml.saml.ext.idpdisco.impl;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import org.opensaml.core.xml.schema.XSBooleanValue;
import org.opensaml.core.xml.XMLObjectProviderBaseTestCase;
import org.opensaml.saml.ext.idpdisco.DiscoveryResponse;
import org.opensaml.saml.ext.idpdisco.impl.DiscoveryResponseImpl;

/**
 * Test case for creating, marshalling, and unmarshalling {@link DiscoveryResponseImpl}.
 */
public class DiscoveryResponseTest extends XMLObjectProviderBaseTestCase {

    protected String expectedBinding;

    protected String expectedLocation;

    protected String expectedResponseLocation;

    protected Integer expectedIndex;

    protected XSBooleanValue expectedIsDefault;

    /**
     * Constructor
     */
    public DiscoveryResponseTest() {
        singleElementFile = "/data/org/opensaml/saml/ext/idpdisco/DiscoveryResponse.xml";
        singleElementOptionalAttributesFile = "/data/org/opensaml/saml/ext/idpdisco/DiscoveryResponseOptionalAttributes.xml";
    }

    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        expectedBinding = "urn:binding:foo";
        expectedLocation = "example.org";
        expectedResponseLocation = "example.org/response";
        expectedIndex = new Integer(3);
        expectedIsDefault = new XSBooleanValue(Boolean.TRUE, false);
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementUnmarshall() {
        DiscoveryResponse service = (DiscoveryResponse) unmarshallElement(singleElementFile);

        AssertJUnit.assertEquals("Binding URI was not expected value", expectedBinding, service.getBinding());
        AssertJUnit.assertEquals("Location was not expected value", expectedLocation, service.getLocation());
        AssertJUnit.assertEquals("Index was not expected value", expectedIndex, service.getIndex());
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementOptionalAttributesUnmarshall() {
        DiscoveryResponse service = (DiscoveryResponse) unmarshallElement(singleElementOptionalAttributesFile);

        AssertJUnit.assertEquals("Binding URI was not expected value", expectedBinding, service.getBinding());
        AssertJUnit.assertEquals("Location was not expected value", expectedLocation, service.getLocation());
        AssertJUnit.assertEquals("Index was not expected value", expectedIndex, service.getIndex());
        AssertJUnit.assertEquals("ResponseLocation was not expected value", expectedResponseLocation, service.getResponseLocation());
        AssertJUnit.assertEquals("isDefault was not expected value", expectedIsDefault, service.isDefaultXSBoolean());
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementMarshall() {
        DiscoveryResponse service = (DiscoveryResponse) buildXMLObject(DiscoveryResponse.DEFAULT_ELEMENT_NAME);

        service.setBinding(expectedBinding);
        service.setLocation(expectedLocation);
        service.setIndex(expectedIndex);

        assertXMLEquals(expectedDOM, service);
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementOptionalAttributesMarshall() {
        DiscoveryResponse service = (DiscoveryResponse) buildXMLObject(DiscoveryResponse.DEFAULT_ELEMENT_NAME);

        service.setBinding(expectedBinding);
        service.setLocation(expectedLocation);
        service.setIndex(expectedIndex);
        service.setResponseLocation(expectedResponseLocation);
        service.setIsDefault(expectedIsDefault);

        assertXMLEquals(expectedOptionalAttributesDOM, service);
    }

    /**
     * Test the proper behavior of the XSBooleanValue attributes.
     */
    @Test
    public void testXSBooleanAttributes() {
        DiscoveryResponse acs = (DiscoveryResponse) buildXMLObject(DiscoveryResponse.DEFAULT_ELEMENT_NAME);

        // isDefault attribute
        acs.setIsDefault(Boolean.TRUE);
        AssertJUnit.assertEquals("Unexpected value for boolean attribute found", Boolean.TRUE, acs.isDefault());
        AssertJUnit.assertNotNull("XSBooleanValue was null", acs.isDefaultXSBoolean());
        AssertJUnit.assertEquals("XSBooleanValue was unexpected value", new XSBooleanValue(Boolean.TRUE, false), acs
                .isDefaultXSBoolean());
        AssertJUnit.assertEquals("XSBooleanValue string was unexpected value", "true", acs.isDefaultXSBoolean().toString());

        acs.setIsDefault(Boolean.FALSE);
        AssertJUnit.assertEquals("Unexpected value for boolean attribute found", Boolean.FALSE, acs.isDefault());
        AssertJUnit.assertNotNull("XSBooleanValue was null", acs.isDefaultXSBoolean());
        AssertJUnit.assertEquals("XSBooleanValue was unexpected value", new XSBooleanValue(Boolean.FALSE, false), acs
                .isDefaultXSBoolean());
        AssertJUnit.assertEquals("XSBooleanValue string was unexpected value", "false", acs.isDefaultXSBoolean().toString());

        acs.setIsDefault((Boolean) null);
        AssertJUnit.assertEquals("Unexpected default value for boolean attribute found", Boolean.FALSE, acs.isDefault());
        AssertJUnit.assertNull("XSBooleanValue was not null", acs.isDefaultXSBoolean());
    }
}