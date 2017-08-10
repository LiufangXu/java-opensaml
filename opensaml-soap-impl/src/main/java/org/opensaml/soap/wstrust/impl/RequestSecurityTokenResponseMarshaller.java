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

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.util.XMLObjectSupport;
import org.opensaml.soap.wstrust.RequestSecurityTokenResponse;
import org.w3c.dom.Element;

/**
 * Marshaller for the RequestSecurityTokenResponse element.
 * 
 */
public class RequestSecurityTokenResponseMarshaller extends AbstractWSTrustObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(final XMLObject xmlObject, final Element domElement) throws MarshallingException {
        RequestSecurityTokenResponse rstr= (RequestSecurityTokenResponse) xmlObject;
        if (rstr.getContext() != null) {
            domElement.setAttributeNS(null, RequestSecurityTokenResponse.CONTEXT_ATTRIB_NAME, rstr.getContext());
        }
        XMLObjectSupport.marshallAttributeMap(rstr.getUnknownAttributes(), domElement);
    }

}
