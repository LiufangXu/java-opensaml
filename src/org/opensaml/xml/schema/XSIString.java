/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
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

package org.opensaml.xml.schema;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLConstants;

/**
 * XMLObject that represents an XML Schema String.
 */
public interface XSIString extends XMLObject {

    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "String"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(XMLConstants.XSI_NS, TYPE_LOCAL_NAME, XMLConstants.XSI_PREFIX);
    
    /**
     * Gets the string.
     * 
     * @return the string
     */
    public String getValue();
    
    /**
     * Sets the string.
     * 
     * @param newValue the string value
     */
    public void setValue(String newValue);
}