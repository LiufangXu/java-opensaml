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

package org.opensaml.common.binding.decoding.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opensaml.common.binding.decoding.HTTPMessageDecoder;

/**
 * Base class for HTTP message decoders handling much of boilerplate code.
 * 
 */
public abstract class AbstractHTTPMessageDecoder 
    extends AbstractMessageDecoder<HttpServletRequest> implements HTTPMessageDecoder {
    
    /** Class logger. */
    private final Logger log = Logger.getLogger(AbstractHTTPMessageDecoder.class);

    /** HTTP method used in the request. */
    private String httpMethod;
    
    /** Request relay state. */
    private String relayState;    
    
    /** {@inheritDoc} */
    public String getMethod(){
        return httpMethod;
    }
    
    /**
     * Sets the HTTP method used by the request.
     * 
     * @param method HTTP method used by the request
     */
    protected void setHttpMethod(String method){
        this.httpMethod = method.toUpperCase();
    }
    
    /** {@inheritDoc} */
    public String getRelayState() {
        return relayState;
    }
    
    /**
     * Sets the relay state of the request.
     * 
     * @param state relay state of the request
     */
    protected void setRelayState(String state){
        this.relayState = state;
    }
}