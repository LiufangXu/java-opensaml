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

package org.opensaml.saml.saml2.metadata.support;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.io.File;
import java.net.URL;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.ext.saml2mdquery.AttributeQueryDescriptorType;
import org.opensaml.saml.saml2.metadata.AttributeConsumingService;
import org.opensaml.saml.saml2.metadata.RoleDescriptor;
import org.opensaml.saml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.saml.saml2.metadata.support.AttributeConsumingServiceSelector;

/**
 * Tests of AttributeConsumingServiceSelector.
 */
public class AttributeConsumingServiceSelectorTest extends XMLObjectBaseTestCase {
    
    private String mdFileName;
    
    private FilesystemMetadataProvider mdProvider;
    
    private AttributeConsumingServiceSelector acsSelector;

    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        mdFileName = "/data/org/opensaml/saml/saml2/metadata/support/metadata-AttributeConsumingService.xml";
        
        URL mdURL = AttributeConsumingServiceSelectorTest.class.getResource(mdFileName);
        File mdFile = new File(mdURL.toURI());
        
        mdProvider = new FilesystemMetadataProvider(mdFile);
        mdProvider.setParserPool(parserPool);
        mdProvider.initialize();
        
        acsSelector = new AttributeConsumingServiceSelector();
    }
    
    // Success cases
    
    /**
     * Test valid index.
     * @throws MetadataProviderException
     */
    @Test
    public void testWithValidIndex() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:A", SPSSODescriptor.DEFAULT_ELEMENT_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        acsSelector.setIndex(1);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNotNull(acs);
        
        AssertJUnit.assertEquals("Wrong service selected", "A-SP-1", getName(acs));
    }
    
    /**
     * Test explicit isDefault="true".
     * @throws MetadataProviderException 
     * @throws MetadataProviderException
     */
    @Test
    public void testExplicitDefault() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:A", SPSSODescriptor.DEFAULT_ELEMENT_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNotNull(acs);
        
        AssertJUnit.assertEquals("Wrong service selected", "A-SP-0", getName(acs));
    }
    
    /**
     * Test default as first missing default.
     * @throws MetadataProviderException
     */
    @Test
    public void testFirstMissingDefault() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:B", SPSSODescriptor.DEFAULT_ELEMENT_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNotNull(acs);
        
        AssertJUnit.assertEquals("Wrong service selected", "B-SP-2", getName(acs));
    }
    
    /**
     * Test default as first isDefault="false".
     * @throws MetadataProviderException
     */
    @Test
    public void testFirstFalseDefault() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:C", SPSSODescriptor.DEFAULT_ELEMENT_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNotNull(acs);
        
        AssertJUnit.assertEquals("Wrong service selected", "C-SP-0", getName(acs));
    }
    
    /**
     * Test AttributeQueryDescriptorType.
     * @throws MetadataProviderException
     */
    @Test
    public void testAttributeQueryType() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:A", AttributeQueryDescriptorType.TYPE_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        acsSelector.setIndex(0);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNotNull(acs);
        
        AssertJUnit.assertEquals("Wrong service selected", "A-AQ-0", getName(acs));
    }
    
    /**
     * Test invalid index.
     * @throws MetadataProviderException
     */
    @Test
    public void testInvalidIndex() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:A", SPSSODescriptor.DEFAULT_ELEMENT_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        acsSelector.setIndex(3);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNull("Service should have been null due to invalid index", acs);
    }
    
    /**
     * Test invalid index with onBadIndexUseDefault of true.
     * @throws MetadataProviderException
     */
    @Test
    public void testInvalidIndexWithUseDefault() throws MetadataProviderException {
        RoleDescriptor role =  mdProvider.getRole("urn:test:entity:A", SPSSODescriptor.DEFAULT_ELEMENT_NAME,
                SAMLConstants.SAML20P_NS);
        AssertJUnit.assertNotNull(role);
        acsSelector.setRoleDescriptor(role);
        
        acsSelector.setIndex(3);
        acsSelector.setOnBadIndexUseDefault(true);
        
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNotNull(acs);
        
        AssertJUnit.assertEquals("Wrong service selected", "A-SP-0", getName(acs));
    }
    
    /**
     * Test missing RoleDescriptor input.
     * @throws MetadataProviderException
     */
    @Test
    public void testNoRoleDescriptor() throws MetadataProviderException {
        AttributeConsumingService acs = acsSelector.selectService();
        AssertJUnit.assertNull("Service should have been null due to lack of role descriptor", acs);
    }
    
    
    /////////////////////////////////
    
    /**
     * Get  the first service name of an AttributeConsumingService.
     * 
     * @param acs the attribute consuming service
     * @return the first name of the service
     */
    private String getName(AttributeConsumingService acs) {
        return acs.getNames().get(0).getValue();
    }

}