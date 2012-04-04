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

package org.opensaml.saml.saml2.metadata.provider;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Timer;

import net.shibboleth.utilities.java.support.resource.FilesystemResource;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.RoleDescriptor;
import org.opensaml.saml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.saml.saml2.metadata.provider.ResourceBackedMetadataProvider;

/** Unit test for {@link ResourceBackedMetadataProvider}. */
public class ResourceBackedMetadataProviderTest extends XMLObjectBaseTestCase {

    private ResourceBackedMetadataProvider metadataProvider;

    private String entityID;

    private String supportedProtocol;

    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        entityID = "urn:mace:incommon:washington.edu";
        supportedProtocol = "urn:oasis:names:tc:SAML:1.1:protocol";

        URL mdURL = ResourceBackedMetadataProviderTest.class
                .getResource("/data/org/opensaml/saml/saml2/metadata/InCommon-metadata.xml");
        FilesystemResource mdResource = new FilesystemResource(new File(mdURL.toURI()).getAbsolutePath());
        mdResource.initialize();

        metadataProvider = new ResourceBackedMetadataProvider(new Timer(), mdResource);
        metadataProvider.setParserPool(parserPool);
        metadataProvider.setMaxRefreshDelay(500000);
        metadataProvider.initialize();
    }

    /**
     * Tests the {@link ResourceBackedMetadataProvider#getEntityDescriptor(String)} method.
     */
    @Test
    public void testGetEntityDescriptor() throws MetadataProviderException {
        EntityDescriptor descriptor = metadataProvider.getEntityDescriptor(entityID);
        AssertJUnit.assertNotNull("Retrieved entity descriptor was null", descriptor);
        AssertJUnit.assertEquals("Entity's ID does not match requested ID", entityID, descriptor.getEntityID());
    }

    /**
     * Tests the {@link ResourceBackedMetadataProvider#getRole(String, javax.xml.namespace.QName) method.
     */
    @Test
    public void testGetRole() throws MetadataProviderException {
        List<RoleDescriptor> roles = metadataProvider.getRole(entityID, IDPSSODescriptor.DEFAULT_ELEMENT_NAME);
        AssertJUnit.assertNotNull("Roles for entity descriptor was null", roles);
        AssertJUnit.assertEquals("Unexpected number of roles", 1, roles.size());
    }

    /**
     * Test the {@link ResourceBackedMetadataProvider#getRole(String, javax.xml.namespace.QName, String) method.
     */
    @Test
    public void testGetRoleWithSupportedProtocol() throws MetadataProviderException {
        RoleDescriptor role = metadataProvider.getRole(entityID, IDPSSODescriptor.DEFAULT_ELEMENT_NAME,
                supportedProtocol);
        AssertJUnit.assertNotNull("Roles for entity descriptor was null", role);
    }
}