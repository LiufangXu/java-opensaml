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

package org.opensaml.saml2.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Scoping;

/**
 * Builder of {@link org.opensaml.saml2.core.impl.ScopingImpl}
 */
public class ScopingBuilder extends AbstractSAMLObjectBuilder<Scoping> {

    /**
     * Constructor
     */
    public ScopingBuilder() {
    }

    /** {@inheritDoc} */
    public Scoping buildObject() {
        return buildObject(SAMLConstants.SAML20P_NS, Scoping.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    }

    /** {@inheritDoc} */
    public Scoping buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new ScopingImpl(namespaceURI, localName, namespacePrefix);
    }
}