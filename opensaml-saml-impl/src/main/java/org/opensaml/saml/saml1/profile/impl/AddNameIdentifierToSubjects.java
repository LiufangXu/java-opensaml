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

package org.opensaml.saml.saml1.profile.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.profile.action.AbstractProfileAction;
import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.action.EventIds;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.profile.context.navigate.OutboundMessageContextLookup;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullAfterInit;
import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;

import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.messaging.context.navigate.MessageLookup;
import org.opensaml.saml.common.SAMLException;
import org.opensaml.saml.common.SAMLObjectBuilder;
import org.opensaml.saml.common.profile.logic.MetadataNameIdentifierFormatStrategy;
import org.opensaml.saml.saml1.core.Assertion;
import org.opensaml.saml.saml1.core.NameIdentifier;
import org.opensaml.saml.saml1.core.Response;
import org.opensaml.saml.saml1.core.Statement;
import org.opensaml.saml.saml1.core.Subject;
import org.opensaml.saml.saml1.core.SubjectStatement;
import org.opensaml.saml.saml1.profile.SAML1NameIdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Functions;

/**
 * Action that builds a {@link NameIdentifier} and adds it to the {@link Subject} of all the statements
 * in all the assertions found in a {@link Response}. The message to update is returned by a lookup
 * strategy, by default the message returned by {@link ProfileRequestContext#getOutboundMessageContext()}.
 * 
 * <p>No assertions or statements will be created by this action, but if no {@link Subject} exists in
 * the statements found, it will be created.</p>
 * 
 * <p>The source of the {@link NameIdentifier} is one of a set of candidate {@link SAML1NameIdentifierGenerator}
 * plugins injected into the action. The plugin(s) to attempt to use are derived from the Format value,
 * which is established by a lookup strategy.</p>
 * 
 * @event {@link EventIds#PROCEED_EVENT_ID}
 * @event {@link EventIds#INVALID_MSG_CTX}
 */
public class AddNameIdentifierToSubjects extends AbstractProfileAction {

    /** Class logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(AddNameIdentifierToSubjects.class);
    
    /** Builder for Subject objects. */
    @Nonnull private final SAMLObjectBuilder<Subject> subjectBuilder;

    /** Builder for NameIdentifier objects. */
    @Nonnull private final SAMLObjectBuilder<NameIdentifier> nameIdentifierBuilder;
    
    /** Flag controlling whether to overwrite an existing NameIdentifier. */
    private boolean overwriteExisting;
    
    /** Strategy used to locate the {@link Response} to operate on. */
    @Nonnull private Function<ProfileRequestContext,Response> responseLookupStrategy;

    /** Strategy used to determine the formats to try. */
    @Nonnull private Function<ProfileRequestContext,List<String>> formatLookupStrategy;

    /** Generator to use. */
    @NonnullAfterInit private SAML1NameIdentifierGenerator generator;
    
    /** Formats to try. */
    @Nonnull @NonnullElements private List<String> formats;
    
    /** Response to modify. */
    @Nullable private Response response;
    
    /** Constructor. */
    public AddNameIdentifierToSubjects() {
        subjectBuilder = (SAMLObjectBuilder<Subject>)
                XMLObjectProviderRegistrySupport.getBuilderFactory().<Subject>getBuilderOrThrow(
                        Subject.DEFAULT_ELEMENT_NAME);
        nameIdentifierBuilder = (SAMLObjectBuilder<NameIdentifier>)
                XMLObjectProviderRegistrySupport.getBuilderFactory().<NameIdentifier>getBuilderOrThrow(
                        NameIdentifier.DEFAULT_ELEMENT_NAME);
        
        overwriteExisting = true;
        
        responseLookupStrategy =
                Functions.compose(new MessageLookup<>(Response.class), new OutboundMessageContextLookup());
        formatLookupStrategy = new MetadataNameIdentifierFormatStrategy();
        formats = Collections.emptyList();
    }
    
    /**
     * Set whether to overwrite any existing {@link NameIdentifier} objects found.
     * 
     * @param flag  true iff the action should overwrite any existing objects
     */
    public void setOverwriteExisting(final boolean flag) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        overwriteExisting = flag;
    }
    
    /**
     * Set the strategy used to locate the {@link Response} to operate on.
     * 
     * @param strategy strategy used to locate the {@link Response} to operate on
     */
    public void setResponseLookupStrategy(@Nonnull final Function<ProfileRequestContext,Response> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);

        responseLookupStrategy = Constraint.isNotNull(strategy, "Response lookup strategy cannot be null");
    }

    /**
     * Set the strategy function to use to obtain the formats to try.
     * 
     * @param strategy  format lookup strategy
     */
    public void setFormatLookupStrategy(@Nonnull final Function<ProfileRequestContext,List<String>> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        formatLookupStrategy = Constraint.isNotNull(strategy, "Format lookup strategy cannot be null");
    }

    /**
     * Set the generator to use.
     * 
     * @param theGenerator the generator to use
     */
    public void setNameIdentifierGenerator(@Nonnull final SAML1NameIdentifierGenerator theGenerator) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        generator = Constraint.isNotNull(theGenerator, "SAML1NameIdentifierGenerator cannot be null");
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doInitialize() throws ComponentInitializationException {
        super.doInitialize();
        
        if (generator == null) {
            throw new ComponentInitializationException("SAML1NameIdentifierGenerator cannot be null");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected boolean doPreExecute(@Nonnull final ProfileRequestContext profileRequestContext) {
        log.debug("{} Attempting to add NameIdentifier to statements in outgoing Response", getLogPrefix());

        response = responseLookupStrategy.apply(profileRequestContext);
        if (response == null) {
            log.debug("{} No SAML response located in current profile request context", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_MSG_CTX);
            return false;
        } else if (response.getAssertions().isEmpty()) {
            log.debug("{} No assertions in response message, nothing to do", getLogPrefix());
            return false;
        }
        
        formats = formatLookupStrategy.apply(profileRequestContext);
        if (formats == null || formats.isEmpty()) {
            log.debug("{} No candidate NameIdentifier formats, nothing to do", getLogPrefix());
            return false;
        } else {
            log.debug("{} Candidate NameIdentifier formats: {}", getLogPrefix(), formats);
        }
        
        return super.doPreExecute(profileRequestContext);
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) {

        final NameIdentifier nameIdentifier = generateNameIdentifier(profileRequestContext);
        if (nameIdentifier == null) {
            log.debug("{} Unable to generate a NameIdentifier, leaving empty", getLogPrefix());
            return;
        }
        
        int count = 0;
        
        for (final Assertion assertion : response.getAssertions()) {
            for (final Statement statement : assertion.getStatements()) {
                if (statement instanceof SubjectStatement) {
                    final Subject subject = getStatementSubject((SubjectStatement) statement);
                    final NameIdentifier existing = subject.getNameIdentifier();
                    if (existing == null || overwriteExisting) {
                        subject.setNameIdentifier(count > 0 ? cloneNameIdentifier(nameIdentifier) : nameIdentifier);
                        count ++;
                    }
                }
            }
        }
        
        if (count > 0) {
            log.debug("{} Added NameIdentifier to {} statement subject(s)", getLogPrefix(), count);
        }
    }

    /**
     * Attempt to generate a {@link NameIdentifier} using each of the candidate Formats and plugins.
     * 
     * @param profileRequestContext current profile request context
     * 
     * @return a generated {@link NameIdentifier} or null
     */
    @Nullable private NameIdentifier generateNameIdentifier(
            @Nonnull final ProfileRequestContext profileRequestContext) {
        
        // See if we can generate one.
        for (final String format : formats) {
            log.debug("{} Trying to generate NameIdentifier with Format {}", getLogPrefix(), format);
            try {
                final NameIdentifier nameIdentifier = generator.generate(profileRequestContext, format);
                if (nameIdentifier != null) {
                    log.debug("{} Successfully generated NameIdentifier with Format {}", getLogPrefix(),
                            format);
                    return nameIdentifier;
                }
            } catch (final SAMLException e) {
                log.error("{} Error while generating NameIdentifier", getLogPrefix(), e);
            }
        }
        
        return null;
    }
    
    /**
     * Get the subject to which the name identifier will be added.
     * 
     * @param statement the statement being modified
     * 
     * @return the subject to which the name identifier will be added
     */
    @Nonnull private Subject getStatementSubject(@Nonnull final SubjectStatement statement) {
        if (statement.getSubject() != null) {
            return statement.getSubject();
        }
        
        final Subject subject = subjectBuilder.buildObject();
        statement.setSubject(subject);
        return subject;
    }
    
    /**
     * Create an efficient field-wise copy of a {@link NameIdentifier}.
     * 
     * @param nameIdentifier    the object to clone
     * 
     * @return the copy
     */
    @Nonnull private NameIdentifier cloneNameIdentifier(@Nonnull final NameIdentifier nameIdentifier) {
        final NameIdentifier clone = nameIdentifierBuilder.buildObject();
        
        clone.setFormat(nameIdentifier.getFormat());
        clone.setNameQualifier(nameIdentifier.getNameQualifier());
        clone.setValue(nameIdentifier.getValue());
        
        return clone;
    }
    
}