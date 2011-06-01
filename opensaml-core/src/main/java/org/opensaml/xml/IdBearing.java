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

package org.opensaml.xml;

import javax.xml.namespace.QName;

import org.opensaml.xml.util.XMLConstants;

/**
 * Interface for element having a <code>@xml:id</code> attribute.
 * 
 */
public interface IdBearing {

    /** The <code>id</code> attribute local name. */
    public static final String XML_ID_ATTR_LOCAL_NAME = "id";

    /** The <code>xml:id</code> qualified attribute name. */
    public static final QName XML_ID_ATTR_NAME =
        new QName(XMLConstants.XML_NS, XML_ID_ATTR_LOCAL_NAME, XMLConstants.XML_PREFIX);

    /**
     * Returns the <code>@xml:id</code> attribute value.
     * 
     * @return The <code>@xml:id</code> attribute value or <code>null</code>.
     */
    public String getXMLId();

    /**
     * Sets the <code>@xml:id</code> attribute value.
     * 
     * @param newId The <code>@xml:id</code> attribute value
     */
    public void setXMLId(String newId);

}
