/*
 * Licensed to the University Corporation for Advanced Internet Development, Inc.
 * under one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache 
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

package org.opensaml.ws.wstrust;

import javax.xml.namespace.QName;

import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.ElementExtensibleXMLObject;

/**
 * SignChallengeType complex type. 
 * 
 */
public interface SignChallengeType extends AttributeExtensibleXMLObject, ElementExtensibleXMLObject, WSTrustObject {
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "SignChallengeType"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    /**
     * Returns the wst:Challenge child element.
     * 
     * @return the {@link Challenge} child element or <code>null</code>.
     */
    public Challenge getChallenge();

    /**
     * Sets the wst:Challenge child element.
     * 
     * @param challenge the {@link Challenge} child element to set.
     */
    public void setChallenge(Challenge challenge);

}
