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

package org.opensaml.saml.config.impl;

import org.opensaml.core.xml.config.AbstractXMLObjectProviderInitializer;

/**
 * XMLObject provider initializer for module "saml-impl".
 */
public class XMLObjectProviderInitializer extends AbstractXMLObjectProviderInitializer {
    
    /** Config resources. */
    private static String[] configs = {
        "/v3/saml1-assertion-config.xml", 
        "/v3/saml1-metadata-config.xml", 
        "/v3/saml1-protocol-config.xml",
        "/v3/saml2-assertion-config.xml", 
        "/v3/saml2-assertion-delegation-restriction-config.xml",    
        "/v3/saml2-ecp-config.xml",
        "/v3/saml2-metadata-algorithm-config.xml",
        "/v3/saml2-metadata-attr-config.xml",
        "/v3/saml2-metadata-config.xml",
        "/v3/saml2-metadata-idp-discovery-config.xml",
        "/v3/saml2-metadata-query-config.xml", 
        "/v3/saml2-metadata-reqinit-config.xml", 
        "/v3/saml2-metadata-ui-config.xml",
        "/v3/saml2-metadata-rpi-config.xml",
        "/v3/saml2-protocol-config.xml",
        "/v3/saml2-protocol-thirdparty-config.xml",
        "/v3/saml2-req-attr-config.xml",
        "/v3/saml2-protocol-aslo-config.xml",
        "/v3/saml2-channel-binding-config.xml",
        "/v3/saml-ec-gss-config.xml",
        };

    /** {@inheritDoc} */
    @Override
    protected String[] getConfigResources() {
        return configs;
    }

}
