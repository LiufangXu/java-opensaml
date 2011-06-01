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

package org.opensaml.xacml.config;

import javax.xml.namespace.QName;

import org.opensaml.core.config.Initializer;
import org.opensaml.core.config.XMLObjectProviderInitializerBaseTestCase;
import org.opensaml.xacml.ctx.EnvironmentType;
import org.opensaml.xacml.policy.PolicySetType;

/**
 * Test XMLObject provider initializer for module "xacml-impl".
 */
public class XMLObjectProviderInitializerTest extends XMLObjectProviderInitializerBaseTestCase {

    /** {@inheritDoc} */
    protected Initializer getTestedInitializer() {
        return new XMLObjectProviderInitializer();
    }

    /** {@inheritDoc} */
    protected QName[] getTestedProviders() {
        return new QName[] {
                EnvironmentType.DEFAULT_ELEMENT_NAME,
                PolicySetType.DEFAULT_ELEMENT_NAME,
        };
    }
    
}
