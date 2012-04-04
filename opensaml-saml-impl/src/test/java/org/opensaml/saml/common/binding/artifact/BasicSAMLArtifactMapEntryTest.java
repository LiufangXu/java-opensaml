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

package org.opensaml.saml.common.binding.artifact;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.joda.time.DateTime;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.binding.artifact.BasicSAMLArtifactMapEntry;
import org.w3c.dom.Document;

/**
 * Test the basic SAML artifact map entry.
 */
public class BasicSAMLArtifactMapEntryTest extends XMLObjectBaseTestCase {
    
    private SAMLObject samlObject;
    
    private String artifact = "the-artifact";
    private String issuerId = "urn:test:issuer";
    private String rpId = "urn:test:rp";
    private long lifetime = 60*60*1000;
    
    private Document origDocument;
    
    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        samlObject = (SAMLObject) unmarshallElement("/data/org/opensaml/saml/saml2/core/ResponseSuccessAuthnAttrib.xml");
        origDocument = samlObject.getDOM().getOwnerDocument();
        // Drop the DOM for a more realistic test, usuallly the artifact SAMLObject will be built, not unmarshalled
        samlObject.releaseChildrenDOM(true);
        samlObject.releaseDOM();
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException, MarshallingException {
        
        BasicSAMLArtifactMapEntry origEntry = 
            new BasicSAMLArtifactMapEntry(artifact, issuerId, rpId, samlObject, lifetime);
        DateTime expectedExpiration = origEntry.getExpirationTime();
        
        Object newObject = serializeAndDeserialize(origEntry);
        AssertJUnit.assertNotNull("Deserialized object was null", newObject);
        AssertJUnit.assertTrue("Object was not instance of expected class",
                newObject instanceof BasicSAMLArtifactMapEntry);
        
        BasicSAMLArtifactMapEntry newEntry = (BasicSAMLArtifactMapEntry) newObject;
        
        AssertJUnit.assertEquals("Invalid value for artifact", artifact, newEntry.getArtifact());
        AssertJUnit.assertEquals("Invalid value for issuer ID", issuerId, newEntry.getIssuerId());
        AssertJUnit.assertEquals("Invalid value for relying party ID", rpId, newEntry.getRelyingPartyId());
        AssertJUnit.assertEquals("Invalid value for expiration time", expectedExpiration, newEntry.getExpirationTime());
        
        // Test SAMLObject reconstitution
        // It will be unmarshalled and so should already have a DOM
        Document newDocument = newEntry.getSamlMessage().getDOM().getOwnerDocument();
        XMLAssert.assertXMLIdentical(new Diff(origDocument, newDocument), true);

    }
    
    @Test
    public void testMessageSerialization() {
        BasicSAMLArtifactMapEntry entry = 
            new BasicSAMLArtifactMapEntry(artifact, issuerId, rpId, samlObject, lifetime);
        
        AssertJUnit.assertNull(entry.getSerializedMessage());
        
        entry.serializeMessage();
        
        AssertJUnit.assertNotNull(entry.getSerializedMessage());
    }

    protected Object serializeAndDeserialize(Object origObject) throws IOException, ClassNotFoundException {
        File dataFile = new File("artifact-entry-serialization-test.ser");
        if (dataFile.exists()) {
            dataFile.delete();
        }

        FileOutputStream fos = new FileOutputStream(dataFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(origObject);
        oos.flush();
        oos.close();

        FileInputStream fis = new FileInputStream(dataFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object deserializedObject = ois.readObject();
        ois.close();

        dataFile.delete();

        return deserializedObject;
    }


}