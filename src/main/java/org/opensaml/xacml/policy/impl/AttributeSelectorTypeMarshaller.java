/*
 * Copyright 2008 University Corporation for Advanced Internet Development, Inc.
 * Copyright 2008 Members of the EGEE Collaboration.
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

package org.opensaml.xacml.policy.impl;

import org.opensaml.xacml.policy.AttributeDesignatorType;
import org.opensaml.xacml.policy.AttributeSelectorType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectMarshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Element;

/**
 * Marshaller for {@link AttributeSelectorType}.
 */
public class AttributeSelectorTypeMarshaller extends AbstractXMLObjectMarshaller {

    /** Constructor. */
    public AttributeSelectorTypeMarshaller() {
        super();
    }
    
    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        AttributeSelectorType attributeSelectorType = (AttributeSelectorType) xmlObject;
        
        if(!DatatypeHelper.isEmpty(attributeSelectorType.getDataType())){
            domElement.setAttribute(AttributeSelectorType.DATA_TYPE_ATTRIB_NAME, 
                    attributeSelectorType.getDataType());
        }
        if(!DatatypeHelper.isEmpty(attributeSelectorType.getRequestContextPath())){
            domElement.setAttribute(AttributeSelectorType.REQUEST_CONTEXT_PATH_ATTRIB_NAME, 
                    attributeSelectorType.getRequestContextPath());
        }
        if(attributeSelectorType.getMustBePresentXSBoolean() != null){
            domElement.setAttribute(AttributeDesignatorType.MUST_BE_PRESENT_ATTRIB_NAME, 
                    Boolean.toString(attributeSelectorType.getMustBePresentXSBoolean().getValue()));
        }
        if(!attributeSelectorType.getMustBePresent()){
            domElement.setAttribute(AttributeDesignatorType.MUST_BE_PRESENT_ATTRIB_NAME, 
                    Boolean.toString(attributeSelectorType.getMustBePresent()));
        }
    }

    /** {@inheritDoc} */
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        // TODO Auto-generated method stub

    }

}