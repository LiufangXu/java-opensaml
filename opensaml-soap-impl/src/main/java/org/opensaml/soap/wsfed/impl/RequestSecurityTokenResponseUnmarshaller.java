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

package org.opensaml.soap.wsfed.impl;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.soap.wsfed.AppliesTo;
import org.opensaml.soap.wsfed.RequestSecurityTokenResponse;
import org.opensaml.soap.wsfed.RequestedSecurityToken;
import org.w3c.dom.Attr;

/** A thread-safe unmarshaller for {@link RequestSecurityTokenResponse} objects. */
public class RequestSecurityTokenResponseUnmarshaller extends AbstractXMLObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(final XMLObject parentSAMLObject, final XMLObject childSAMLObject) {
        RequestSecurityTokenResponse response = (RequestSecurityTokenResponse) parentSAMLObject;

        if (childSAMLObject instanceof RequestedSecurityToken) {
            response.getRequestedSecurityToken().add((RequestedSecurityToken) childSAMLObject);
        } else if (childSAMLObject instanceof AppliesTo) {
            response.setAppliesTo((AppliesTo) childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(final XMLObject fedObject, final Attr attribute) {

    }

    /** {@inheritDoc} */
    protected void processElementContent(final XMLObject fedObject, final String content) {

    }
}