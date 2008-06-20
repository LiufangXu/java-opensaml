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

package org.opensaml.ws.soap.soap11;

import javax.xml.namespace.QName;

import org.opensaml.ws.soap.common.SOAPObject;
import org.opensaml.ws.soap.util.SOAPConstants;
import org.opensaml.xml.schema.XSQName;

/**
 * SOAP 1.1 faultcode.
 */
public interface FaultCode extends SOAPObject, XSQName {

    /** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "faultcode";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(null, DEFAULT_ELEMENT_LOCAL_NAME);

    /** FaultCode value <code>VersionMismatch</code> */
    static public QName VERSION_MISMATCH = new QName(SOAPConstants.SOAP11_NS, "VersionMismatch",
            SOAPConstants.SOAP11_PREFIX);

    /** FaultCode value <code>MustUnderstand</code> */
    static public QName MUST_UNDERSTAND = new QName(SOAPConstants.SOAP11_NS, "MustUnderstand",
            SOAPConstants.SOAP11_PREFIX);

    /** FaultCode value <code>Server</code> */
    static public QName SERVER = new QName(SOAPConstants.SOAP11_NS, "Server", SOAPConstants.SOAP11_PREFIX);

    /** FaultCode value <code>Client</code> */
    static public QName CLIENT = new QName(SOAPConstants.SOAP11_NS, "Client", SOAPConstants.SOAP11_PREFIX);

}
