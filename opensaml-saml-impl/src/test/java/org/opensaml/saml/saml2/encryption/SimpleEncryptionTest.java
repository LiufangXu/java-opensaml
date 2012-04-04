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

package org.opensaml.saml.saml2.encryption;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import org.testng.AssertJUnit;
import java.util.ArrayList;
import java.util.List;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.EncryptedAssertion;
import org.opensaml.saml.saml2.core.EncryptedAttribute;
import org.opensaml.saml.saml2.core.EncryptedID;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.NewEncryptedID;
import org.opensaml.saml.saml2.core.NewID;
import org.opensaml.saml.saml2.encryption.Encrypter;
import org.opensaml.xmlsec.XMLSecurityHelper;
import org.opensaml.xmlsec.encryption.support.EncryptionConstants;
import org.opensaml.xmlsec.encryption.support.EncryptionException;
import org.opensaml.xmlsec.encryption.support.EncryptionParameters;
import org.opensaml.xmlsec.encryption.support.KeyEncryptionParameters;
import org.opensaml.xmlsec.keyinfo.impl.StaticKeyInfoGenerator;
import org.opensaml.xmlsec.signature.KeyInfo;
import org.opensaml.xmlsec.signature.KeyName;

import com.google.common.base.Strings;

/**
 * Simple tests for encryption.
 */
public class SimpleEncryptionTest extends XMLObjectBaseTestCase {
    
    private Encrypter encrypter;
    private EncryptionParameters encParams;
    private KeyEncryptionParameters kekParamsRSA;
    private List<KeyEncryptionParameters> kekParamsList;
    
    private KeyInfo keyInfo;
    
    private String algoURI;
    private String expectedKeyName;
    
    private String kekURIRSA;
    /**
     * Constructor.
     *
     */
    public SimpleEncryptionTest() {
        super();
        
        expectedKeyName = "SuperSecretKey";
        algoURI = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128;
        
        kekURIRSA = EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15;
    }
    
    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        encParams = new EncryptionParameters();
        encParams.setAlgorithm(algoURI);
        encParams.setEncryptionCredential(XMLSecurityHelper.generateKeyAndCredential(algoURI));
        
        kekParamsRSA = new KeyEncryptionParameters();
        kekParamsRSA.setAlgorithm(kekURIRSA);
        kekParamsRSA.setEncryptionCredential(XMLSecurityHelper.generateKeyPairAndCredential(kekURIRSA, 1024, false));
        
        kekParamsList = new ArrayList<KeyEncryptionParameters>();
        
        keyInfo = (KeyInfo) buildXMLObject(KeyInfo.DEFAULT_ELEMENT_NAME);
    }

    /**
     *  Test basic encryption with symmetric key, no key wrap,
     *  set key name in passed KeyInfo object.
     */
    @Test
    public void testAssertion() {
        Assertion target = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        
        KeyName keyName = (KeyName) buildXMLObject(org.opensaml.xmlsec.signature.KeyName.DEFAULT_ELEMENT_NAME);
        keyName.setValue(expectedKeyName);
        keyInfo.getKeyNames().add(keyName);
        encParams.setKeyInfoGenerator(new StaticKeyInfoGenerator(keyInfo));
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        EncryptedAssertion encTarget = null;
        XMLObject encObject = null;
        try {
            encObject = encrypter.encrypt(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof EncryptedAssertion);
        encTarget = (EncryptedAssertion) encObject;
        
        AssertJUnit.assertEquals("Type attribute", EncryptionConstants.TYPE_ELEMENT, encTarget.getEncryptedData().getType());
        AssertJUnit.assertEquals("Algorithm attribute", algoURI, 
                encTarget.getEncryptedData().getEncryptionMethod().getAlgorithm());
        AssertJUnit.assertNotNull("KeyInfo", encTarget.getEncryptedData().getKeyInfo());
        AssertJUnit.assertEquals("KeyName", expectedKeyName, 
                encTarget.getEncryptedData().getKeyInfo().getKeyNames().get(0).getValue());
        
        AssertJUnit.assertEquals("Number of EncryptedKeys", 0, 
                encTarget.getEncryptedData().getKeyInfo().getEncryptedKeys().size());
        
        AssertJUnit.assertFalse("EncryptedData ID attribute was empty",
                Strings.isNullOrEmpty(encTarget.getEncryptedData().getID()));
    }
    
    /**
     *  Test basic encryption with symmetric key, no key wrap,
     *  set key name in passed KeyInfo object.
     */
    @Test
    public void testAssertionAsID() {
        Assertion target = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        
        KeyName keyName = (KeyName) buildXMLObject(org.opensaml.xmlsec.signature.KeyName.DEFAULT_ELEMENT_NAME);
        keyName.setValue(expectedKeyName);
        keyInfo.getKeyNames().add(keyName);
        encParams.setKeyInfoGenerator(new StaticKeyInfoGenerator(keyInfo));
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        EncryptedID encTarget = null;
        XMLObject encObject = null;
        try {
            encObject = encrypter.encryptAsID(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof EncryptedID);
        encTarget = (EncryptedID) encObject;
        
        AssertJUnit.assertEquals("Type attribute", EncryptionConstants.TYPE_ELEMENT, encTarget.getEncryptedData().getType());
        AssertJUnit.assertEquals("Algorithm attribute", algoURI, 
                encTarget.getEncryptedData().getEncryptionMethod().getAlgorithm());
        AssertJUnit.assertNotNull("KeyInfo", encTarget.getEncryptedData().getKeyInfo());
        AssertJUnit.assertEquals("KeyName", expectedKeyName, 
                encTarget.getEncryptedData().getKeyInfo().getKeyNames().get(0).getValue());
        
        AssertJUnit.assertEquals("Number of EncryptedKeys", 0, 
                encTarget.getEncryptedData().getKeyInfo().getEncryptedKeys().size());
        
        AssertJUnit.assertFalse("EncryptedData ID attribute was empty",
                Strings.isNullOrEmpty(encTarget.getEncryptedData().getID()));
    }
    
    /**
     *  Test basic encryption with symmetric key, no key wrap,
     *  set key name in passed KeyInfo object.
     */
    @Test
    public void testNameID() {
        Assertion assertion = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        NameID target = assertion.getSubject().getNameID();
        
        KeyName keyName = (KeyName) buildXMLObject(org.opensaml.xmlsec.signature.KeyName.DEFAULT_ELEMENT_NAME);
        keyName.setValue(expectedKeyName);
        keyInfo.getKeyNames().add(keyName);
        encParams.setKeyInfoGenerator(new StaticKeyInfoGenerator(keyInfo));
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        EncryptedID encTarget = null;
        XMLObject encObject = null;
        try {
            encObject = encrypter.encrypt(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof EncryptedID);
        encTarget = (EncryptedID) encObject;
        
        AssertJUnit.assertEquals("Type attribute", EncryptionConstants.TYPE_ELEMENT, encTarget.getEncryptedData().getType());
        AssertJUnit.assertEquals("Algorithm attribute", algoURI, 
                encTarget.getEncryptedData().getEncryptionMethod().getAlgorithm());
        AssertJUnit.assertNotNull("KeyInfo", encTarget.getEncryptedData().getKeyInfo());
        AssertJUnit.assertEquals("KeyName", expectedKeyName, 
                encTarget.getEncryptedData().getKeyInfo().getKeyNames().get(0).getValue());
        
        AssertJUnit.assertEquals("Number of EncryptedKeys", 0, 
                encTarget.getEncryptedData().getKeyInfo().getEncryptedKeys().size());
        
        AssertJUnit.assertFalse("EncryptedData ID attribute was empty",
                Strings.isNullOrEmpty(encTarget.getEncryptedData().getID()));
    }
    
    /**
     *  Test basic encryption with symmetric key, no key wrap,
     *  set key name in passed KeyInfo object.
     */
    @Test
    public void testAttribute() {
        Assertion assertion = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        Attribute target = assertion.getAttributeStatements().get(0).getAttributes().get(0);
        
        
        KeyName keyName = (KeyName) buildXMLObject(org.opensaml.xmlsec.signature.KeyName.DEFAULT_ELEMENT_NAME);
        keyName.setValue(expectedKeyName);
        keyInfo.getKeyNames().add(keyName);
        encParams.setKeyInfoGenerator(new StaticKeyInfoGenerator(keyInfo));
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        EncryptedAttribute encTarget = null;
        XMLObject encObject = null;
        try {
            encObject = encrypter.encrypt(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof EncryptedAttribute);
        encTarget = (EncryptedAttribute) encObject;
        
        AssertJUnit.assertEquals("Type attribute", EncryptionConstants.TYPE_ELEMENT, encTarget.getEncryptedData().getType());
        AssertJUnit.assertEquals("Algorithm attribute", algoURI, 
                encTarget.getEncryptedData().getEncryptionMethod().getAlgorithm());
        AssertJUnit.assertNotNull("KeyInfo", encTarget.getEncryptedData().getKeyInfo());
        AssertJUnit.assertEquals("KeyName", expectedKeyName, 
                encTarget.getEncryptedData().getKeyInfo().getKeyNames().get(0).getValue());
        
        AssertJUnit.assertEquals("Number of EncryptedKeys", 0, 
                encTarget.getEncryptedData().getKeyInfo().getEncryptedKeys().size());
        
        AssertJUnit.assertFalse("EncryptedData ID attribute was empty",
                Strings.isNullOrEmpty(encTarget.getEncryptedData().getID()));
    }
    
    /**
     *  Test basic encryption with symmetric key, no key wrap,
     *  set key name in passed KeyInfo object.
     */
    @Test
    public void testNewID() {
        NewID target = (NewID) buildXMLObject(NewID.DEFAULT_ELEMENT_NAME);
        target.setNewID("SomeNewID");
        
        KeyName keyName = (KeyName) buildXMLObject(org.opensaml.xmlsec.signature.KeyName.DEFAULT_ELEMENT_NAME);
        keyName.setValue(expectedKeyName);
        keyInfo.getKeyNames().add(keyName);
        encParams.setKeyInfoGenerator(new StaticKeyInfoGenerator(keyInfo));
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        NewEncryptedID encTarget = null;
        XMLObject encObject = null;
        try {
            encObject = encrypter.encrypt(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof NewEncryptedID);
        encTarget = (NewEncryptedID) encObject;
        
        AssertJUnit.assertEquals("Type attribute", EncryptionConstants.TYPE_ELEMENT, encTarget.getEncryptedData().getType());
        AssertJUnit.assertEquals("Algorithm attribute", algoURI, 
                encTarget.getEncryptedData().getEncryptionMethod().getAlgorithm());
        AssertJUnit.assertNotNull("KeyInfo", encTarget.getEncryptedData().getKeyInfo());
        AssertJUnit.assertEquals("KeyName", expectedKeyName, 
                encTarget.getEncryptedData().getKeyInfo().getKeyNames().get(0).getValue());
        
        AssertJUnit.assertEquals("Number of EncryptedKeys", 0, 
                encTarget.getEncryptedData().getKeyInfo().getEncryptedKeys().size());
        
        AssertJUnit.assertFalse("EncryptedData ID attribute was empty",
                Strings.isNullOrEmpty(encTarget.getEncryptedData().getID()));
        
    }
    
    /** Test that reuse of the encrypter with the same encryption and key encryption parameters is allowed. */
    @Test
    public void testReuse() {
        Assertion assertion = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        
        Attribute target = assertion.getAttributeStatements().get(0).getAttributes().get(0);
        Attribute target2 = assertion.getAttributeStatements().get(0).getAttributes().get(1);
        
        KeyName keyName = (KeyName) buildXMLObject(org.opensaml.xmlsec.signature.KeyName.DEFAULT_ELEMENT_NAME);
        keyName.setValue(expectedKeyName);
        keyInfo.getKeyNames().add(keyName);
        encParams.setKeyInfoGenerator(new StaticKeyInfoGenerator(keyInfo));
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        XMLObject encObject = null;
        try {
            encObject = encrypter.encrypt(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof EncryptedAttribute);
        
        XMLObject encObject2 = null;
        try {
            encObject2 = encrypter.encrypt(target2);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject2);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject2 instanceof EncryptedAttribute);
    }
    
    /** Test that a data encryption key is auto-generated if it is not supplied. */
    @Test
    public void testAutoKeyGen() {
        Assertion target = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        
        encParams.setEncryptionCredential(null);
        
        kekParamsList.add(kekParamsRSA);
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        XMLObject encObject = null;
        try {
            encObject = encrypter.encrypt(target);
        } catch (EncryptionException e) {
            Assert.fail("Object encryption failed: " + e);
        }
        
        AssertJUnit.assertNotNull("Encrypted object was null", encObject);
        AssertJUnit.assertTrue("Encrypted object was not an instance of the expected type", 
                encObject instanceof EncryptedAssertion);
    }
    
    /** Test that an error is thrown if the no data encryption credential is supplied and no KEK is specified. */
    @Test
    public void testAutoKeyGenNoKEK() {
        Assertion target = (Assertion) unmarshallElement("/data/org/opensaml/saml/saml2/encryption/Assertion.xml");
        
        encParams.setEncryptionCredential(null);
        
        kekParamsList.clear();
        
        encrypter = new Encrypter(encParams, kekParamsList);
        
        try {
            encrypter.encrypt(target);
            Assert.fail("Object encryption should have failed: no KEK supplied with auto key generation for data encryption");
        } catch (EncryptionException e) {
            //do nothing, should fail
        }
    }
    
}