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

package org.opensaml.xmlsec.impl;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;

import net.shibboleth.utilities.java.support.logic.ConstraintViolationException;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.crypto.JCAConstants;
import org.opensaml.security.crypto.KeySupport;
import org.opensaml.xmlsec.EncryptionParameters;
import org.opensaml.xmlsec.KeyTransportAlgorithmPredicate;
import org.opensaml.xmlsec.criterion.EncryptionConfigurationCriterion;
import org.opensaml.xmlsec.criterion.KeyInfoGenerationProfileCriterion;
import org.opensaml.xmlsec.encryption.support.EncryptionConstants;
import org.opensaml.xmlsec.encryption.support.RSAOAEPParameters;
import org.opensaml.xmlsec.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xmlsec.keyinfo.impl.BasicKeyInfoGeneratorFactory;
import org.opensaml.xmlsec.keyinfo.impl.X509KeyInfoGeneratorFactory;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 */
public class BasicEncryptionParametersResolverTest extends XMLObjectBaseTestCase {
    
    private BasicEncryptionParametersResolver resolver;
    
    private CriteriaSet criteriaSet;
    
    private EncryptionConfigurationCriterion criterion;
    
    private BasicEncryptionConfiguration config1, config2, config3;
    
    private Credential rsaCred1, aes128Cred1, aes192Cred1, aes256Cred1;
    
    private String rsaCred1KeyName = "RSACred1";
    private String aes128Cred1KeyName = "AES128Cred1";
    private String aes192Cred1KeyName = "AES192Cred1";
    private String aes256Cred1KeyName = "AES256Cred1";
    
    private String defaultRSAKeyTransportAlgo = EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP;
    private String defaultAES128DataAlgo = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128;
    private String defaultAES192DataAlgo = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES192;
    private String defaultAES256DataAlgo = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256;
    
    private NamedKeyInfoGeneratorManager defaultKeyTransportKeyInfoGeneratorManager = new NamedKeyInfoGeneratorManager();
    private NamedKeyInfoGeneratorManager defaultDataEncryptionKeyInfoGeneratorManager = new NamedKeyInfoGeneratorManager();
    
    @BeforeClass
    public void buildCredentials() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair rsaKeyPair = KeySupport.generateKeyPair(JCAConstants.KEY_ALGO_RSA, 2048, null);
        rsaCred1 = CredentialSupport.getSimpleCredential(rsaKeyPair.getPublic(), rsaKeyPair.getPrivate());
        rsaCred1.getKeyNames().add(rsaCred1KeyName);
        
        SecretKey aes128Key = KeySupport.generateKey(JCAConstants.KEY_ALGO_AES, 128, null);
        aes128Cred1 = CredentialSupport.getSimpleCredential(aes128Key);
        aes128Cred1.getKeyNames().add(aes128Cred1KeyName);
        
        SecretKey aes192Key = KeySupport.generateKey(JCAConstants.KEY_ALGO_AES, 192, null);
        aes192Cred1 = CredentialSupport.getSimpleCredential(aes192Key);
        aes192Cred1.getKeyNames().add(aes192Cred1KeyName);
        
        SecretKey aes256Key = KeySupport.generateKey(JCAConstants.KEY_ALGO_AES, 256, null);
        aes256Cred1 = CredentialSupport.getSimpleCredential(aes256Key);
        aes256Cred1.getKeyNames().add(aes256Cred1KeyName);
    }
    
    @BeforeMethod
    public void setUp() {
        resolver = new BasicEncryptionParametersResolver();
        
        config1 = new BasicEncryptionConfiguration();
        config2 = new BasicEncryptionConfiguration();
        config3 = new BasicEncryptionConfiguration();
        
        // Set these as defaults on the last config in the chain, just so don't have to set in every test.
        config3.setDataEncryptionAlgorithms(Arrays.asList(
                defaultAES128DataAlgo,
                defaultAES192DataAlgo,
                defaultAES256DataAlgo,
                EncryptionConstants.ALGO_ID_BLOCKCIPHER_TRIPLEDES,
                EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128_GCM,
                EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES192_GCM,
                EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256_GCM
                ));
        config3.setKeyTransportEncryptionAlgorithms(Arrays.asList(
                defaultRSAKeyTransportAlgo, 
                EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15,
                EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11,
                EncryptionConstants.ALGO_ID_KEYWRAP_AES128,
                EncryptionConstants.ALGO_ID_KEYWRAP_AES192,
                EncryptionConstants.ALGO_ID_KEYWRAP_AES256,
                EncryptionConstants.ALGO_ID_KEYWRAP_TRIPLEDES
                ));
        
        BasicKeyInfoGeneratorFactory basicFactory1 = new BasicKeyInfoGeneratorFactory();
        X509KeyInfoGeneratorFactory x509Factory1 = new X509KeyInfoGeneratorFactory();
        defaultKeyTransportKeyInfoGeneratorManager = new NamedKeyInfoGeneratorManager();
        defaultKeyTransportKeyInfoGeneratorManager.registerDefaultFactory(basicFactory1);
        defaultKeyTransportKeyInfoGeneratorManager.registerDefaultFactory(x509Factory1);
        config3.setKeyTransportKeyInfoGeneratorManager(defaultKeyTransportKeyInfoGeneratorManager);
        
        BasicKeyInfoGeneratorFactory basicFactory2 = new BasicKeyInfoGeneratorFactory();
        X509KeyInfoGeneratorFactory x509Factory2 = new X509KeyInfoGeneratorFactory();
        defaultDataEncryptionKeyInfoGeneratorManager = new NamedKeyInfoGeneratorManager();
        defaultDataEncryptionKeyInfoGeneratorManager.registerDefaultFactory(basicFactory2);
        defaultDataEncryptionKeyInfoGeneratorManager.registerDefaultFactory(x509Factory2);
        config3.setDataKeyInfoGeneratorManager(defaultDataEncryptionKeyInfoGeneratorManager);
        
        criterion = new EncryptionConfigurationCriterion(config1, config2, config3);
        
        criteriaSet = new CriteriaSet(criterion);
    }
    
    @Test
    public void testBasicRSA() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), rsaCred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), defaultRSAKeyTransportAlgo);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES128DataAlgo);
        Assert.assertNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testRSAWithAlgorithmOverrides() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        
        config2.setDataEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256));
        config2.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), rsaCred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256);
        Assert.assertNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testRSAWithBlacklist() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config1.setBlacklistedAlgorithms(Arrays.asList(defaultRSAKeyTransportAlgo, defaultAES128DataAlgo, defaultAES192DataAlgo));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), rsaCred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES256DataAlgo);
        Assert.assertNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testRSAWithWhitelist() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config1.setWhitelistedAlgorithms(Arrays.asList(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256, EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), rsaCred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256);
        Assert.assertNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testRSAWithGeneratedDataCredential() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        
        resolver.setAutoGenerateDataEncryptionCredential(true);
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), rsaCred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), defaultRSAKeyTransportAlgo);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNotNull(params.getDataEncryptionCredential());
        Assert.assertNotNull(params.getDataEncryptionCredential().getSecretKey());
        Assert.assertEquals(KeySupport.getKeyLength(params.getDataEncryptionCredential().getSecretKey()), Integer.valueOf(128));
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES128DataAlgo);
        Assert.assertNotNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testRSAOAEPParameters() throws ResolverException {
        EncryptionParameters params;
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        
        // Shouldn't resolve since not RSA OAEP
        config1.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15));
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertNull(params.getRSAOAEPParameters());
        
        // Should resolve an empty instance
        config1.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP));
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertNotNull(params.getRSAOAEPParameters());
        Assert.assertTrue(params.getRSAOAEPParameters().isEmpty());
        
        // Should resolve full set of values from config3
        config1.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP));
        config3.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA1, EncryptionConstants.ALGO_ID_MGF1_SHA1, "dummy-oaep-params-3"));
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertNotNull(params.getRSAOAEPParameters());
        Assert.assertEquals(params.getRSAOAEPParameters().getDigestMethod(), SignatureConstants.ALGO_ID_DIGEST_SHA1);
        Assert.assertEquals(params.getRSAOAEPParameters().getMaskGenerationFunction(), EncryptionConstants.ALGO_ID_MGF1_SHA1);
        Assert.assertEquals(params.getRSAOAEPParameters().getOAEPParams(), "dummy-oaep-params-3");
        
        // Should resolve digest and mgf from config2, OAEPParams from config3 (merged)
        config1.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11));
        config2.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA256, EncryptionConstants.ALGO_ID_MGF1_SHA256, null));
        config3.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA1, EncryptionConstants.ALGO_ID_MGF1_SHA1, "dummy-oaep-params-3"));
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertNotNull(params.getRSAOAEPParameters());
        Assert.assertEquals(params.getRSAOAEPParameters().getDigestMethod(), SignatureConstants.ALGO_ID_DIGEST_SHA256);
        Assert.assertEquals(params.getRSAOAEPParameters().getMaskGenerationFunction(), EncryptionConstants.ALGO_ID_MGF1_SHA256);
        Assert.assertEquals(params.getRSAOAEPParameters().getOAEPParams(), "dummy-oaep-params-3");
        
        // Should resolve digest from config1, and mgf from config2 (merged), but with no merging from config3 
        config1.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11));
        config1.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA512, null, null));
        config2.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA256, EncryptionConstants.ALGO_ID_MGF1_SHA256, null));
        config2.setRSAOAEPParametersMerge(false);
        config3.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA1, EncryptionConstants.ALGO_ID_MGF1_SHA1, "dummy-oaep-params-3"));
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertNotNull(params.getRSAOAEPParameters());
        Assert.assertEquals(params.getRSAOAEPParameters().getDigestMethod(), SignatureConstants.ALGO_ID_DIGEST_SHA512);
        Assert.assertEquals(params.getRSAOAEPParameters().getMaskGenerationFunction(), EncryptionConstants.ALGO_ID_MGF1_SHA256);
        Assert.assertNull(params.getRSAOAEPParameters().getOAEPParams());
        
        // Should resolve empty instance based on config1 only, with no merging
        config1.setKeyTransportEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11));
        config1.setRSAOAEPParameters(null);
        config1.setRSAOAEPParametersMerge(false);
        config2.setRSAOAEPParameters(new RSAOAEPParameters(SignatureConstants.ALGO_ID_DIGEST_SHA256, EncryptionConstants.ALGO_ID_MGF1_SHA256, "dummy-oaep-params2"));
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertNotNull(params.getRSAOAEPParameters());
        Assert.assertTrue(params.getRSAOAEPParameters().isEmpty());
    }
    
    @Test
    public void testAES128KeyWrap() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(aes128Cred1));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), aes128Cred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYWRAP_AES128);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES128DataAlgo);
        Assert.assertNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testDataCredOnly() throws ResolverException {
        config1.setDataEncryptionCredentials(Collections.singletonList(aes256Cred1));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertNull(params.getKeyTransportEncryptionCredential());
        Assert.assertNull(params.getKeyTransportEncryptionAlgorithm());
        Assert.assertNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertEquals(params.getDataEncryptionCredential(), aes256Cred1);
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES256DataAlgo);
        Assert.assertNotNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testKeyTransportCredWithBlacklistAndFallthrough() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Arrays.asList(rsaCred1, aes256Cred1));
        
        // Blacklist all RSA algos so rsaCred1 is skipped in favor of aes256Cred1
        config1.setBlacklistedAlgorithms(Arrays.asList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15, EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP, EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11));
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), aes256Cred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYWRAP_AES256);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES128DataAlgo);
        Assert.assertNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testDataCredOnlyWithBlacklistAndFallthrough() throws ResolverException {
        config1.setDataEncryptionCredentials(Arrays.asList(aes128Cred1, aes256Cred1));
        
        // Blacklist both AES-128 variants so aes128Cred1 is skipped in favor of aes256Cred1
        config1.setBlacklistedAlgorithms(Arrays.asList(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128, EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128_GCM));
        
        final EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params);
        Assert.assertNull(params.getKeyTransportEncryptionCredential());
        Assert.assertNull(params.getKeyTransportEncryptionAlgorithm());
        Assert.assertNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertEquals(params.getDataEncryptionCredential(), aes256Cred1);
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES256DataAlgo);
        Assert.assertNotNull(params.getDataKeyInfoGenerator());
    }
    
    @Test
    public void testKeyTransportAlgorithmPredicate() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config1.setKeyTransportEncryptionAlgorithms(Arrays.asList(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15, EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP));
        config1.setDataEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128));
        
        // Data algorithm -> key transport algorithm preferences mappings
        final HashMap<String,String> algoMap = new HashMap<>();
        algoMap.put(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128, EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);
        algoMap.put(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256, EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
        final KeyTransportAlgorithmPredicate predicate = new MapBasedKeyTransportAlgorithmPredicate(algoMap);
        
        // Without the predicate, for control
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
        
        config1.setKeyTransportAlgorithmPredicate(predicate);
        
        // Explicit preference with predicate, mapping # 1
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);
        
        config1.setDataEncryptionAlgorithms(Collections.singletonList(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256));
        
        // Explicit preference with predicate, mapping # 2
        params = resolver.resolveSingle(criteriaSet);
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES256);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
    }
    
    @Test
    public void testKeyInfoGenerationProfile() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config1.setDataEncryptionCredentials(Collections.singletonList(aes128Cred1));
        
        criteriaSet.add(new KeyInfoGenerationProfileCriterion("testKeyInfoProfile"));
        
        defaultDataEncryptionKeyInfoGeneratorManager.setUseDefaultManager(true);
        defaultKeyTransportKeyInfoGeneratorManager.setUseDefaultManager(true);
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params.getDataKeyInfoGenerator());
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        defaultDataEncryptionKeyInfoGeneratorManager.setUseDefaultManager(false);
        defaultKeyTransportKeyInfoGeneratorManager.setUseDefaultManager(false);
        
        params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNull(params.getDataKeyInfoGenerator());
        Assert.assertNull(params.getKeyTransportKeyInfoGenerator());
        
        defaultDataEncryptionKeyInfoGeneratorManager.setUseDefaultManager(false);
        defaultKeyTransportKeyInfoGeneratorManager.setUseDefaultManager(false);
        defaultDataEncryptionKeyInfoGeneratorManager.registerFactory("testKeyInfoProfile", new BasicKeyInfoGeneratorFactory());
        defaultKeyTransportKeyInfoGeneratorManager.registerFactory("testKeyInfoProfile", new BasicKeyInfoGeneratorFactory());
        
        params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNotNull(params.getDataKeyInfoGenerator());
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
    }
    
    @Test
    public void testResolve() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        
        Iterable<EncryptionParameters> paramsIter = resolver.resolve(criteriaSet);
        Assert.assertNotNull(paramsIter);
        
        Iterator<EncryptionParameters> iterator = paramsIter.iterator();
        Assert.assertNotNull(iterator);
        
        Assert.assertTrue(iterator.hasNext());
        
        EncryptionParameters params = iterator.next();
        
        Assert.assertNotNull(params);
        Assert.assertEquals(params.getKeyTransportEncryptionCredential(), rsaCred1);
        Assert.assertEquals(params.getKeyTransportEncryptionAlgorithm(), defaultRSAKeyTransportAlgo);
        Assert.assertNotNull(params.getKeyTransportKeyInfoGenerator());
        
        Assert.assertNull(params.getDataEncryptionCredential());
        Assert.assertEquals(params.getDataEncryptionAlgorithm(), defaultAES128DataAlgo);
        Assert.assertNull(params.getDataKeyInfoGenerator());
        
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testNoCredentials() throws ResolverException {
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNull(params);
    }
    
    @Test
    public void testNoKeyTransportAlgorithms() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config3.setKeyTransportEncryptionAlgorithms(new ArrayList<String>());
        
        EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNull(params);
    }
    
    @Test
    public void testNoDataEncryptionAlgorithmForResolvedDataCredential() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config1.setDataEncryptionCredentials(Collections.singletonList(aes128Cred1));
        config3.setDataEncryptionAlgorithms(new ArrayList<String>());
        
        final EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNull(params);
    }
    
    @Test
    public void testNoDataEncryptionAlgorithmForEncrypterAutoGen() throws ResolverException {
        config1.setKeyTransportEncryptionCredentials(Collections.singletonList(rsaCred1));
        config3.setDataEncryptionAlgorithms(new ArrayList<String>());
        
        final EncryptionParameters params = resolver.resolveSingle(criteriaSet);
        
        Assert.assertNull(params);
    }
    
    @Test
    public void testResolveKeyTransportAlgorithmPredicate() {
        Assert.assertNull(resolver.resolveKeyTransportAlgorithmPredicate(criteriaSet));
        
        final KeyTransportAlgorithmPredicate predicate = new KeyTransportAlgorithmPredicate() {
            public boolean apply(@Nullable SelectionInput input) {
                return true;
            }
        };
        
        config2.setKeyTransportAlgorithmPredicate(predicate);
        
        Assert.assertTrue(resolver.resolveKeyTransportAlgorithmPredicate(criteriaSet) == predicate);
    }
    
    @Test(expectedExceptions=ConstraintViolationException.class)
    public void testNullCriteriaSet() throws ResolverException {
        resolver.resolve(null);
    }

    @Test(expectedExceptions=ConstraintViolationException.class)
    public void testAbsentCriterion() throws ResolverException {
        resolver.resolve(new CriteriaSet());
    }
    
    // Test utility classes
    
    public class MapBasedKeyTransportAlgorithmPredicate implements KeyTransportAlgorithmPredicate {
        private Map<String,String> algoMap;
        
        public MapBasedKeyTransportAlgorithmPredicate(Map<String,String> algoMap) {
            this.algoMap = algoMap;
        }
        
        public boolean apply(@Nullable SelectionInput input) {
            return this.algoMap.get(input.getDataEncryptionAlgorithm()).equals(input.getKeyTransportAlgorithm());
        }
    }

}
