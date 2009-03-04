package org.hisp.dhis.system.process;

/*
 * Copyright (c) 2004-2007, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the HISP project nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * {@link InternalProcessCoordinator} which loads process objects from the
 * Spring BeanFactory. The {@link #newProcess(String)} type argument is combined
 * with a prefix and a postfix to form a bean ID. According to the default
 * values, a type "a" is transformed into the bean ID "internal-process-a".
 * Exceptions are thrown if there is trouble loading the bean. Process beans
 * must be prototypes.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: SpringInternalProcessCoordinator.java 4395 2008-01-11 17:27:58Z torgeilo $
 */
public class SpringInternalProcessCoordinator
    extends AbstractInternalProcessCoordinator
    implements BeanFactoryAware
{
    private final Log log = LogFactory.getLog( SpringInternalProcessCoordinator.class );

    /**
     * Value: {@value}.
     */
    public static final String DEFAULT_INTERNAL_PROCESS_ID_PREFIX = "internal-process-";

    /**
     * Value: {@value}.
     */
    public static final String DEFAULT_INTERNAL_PROCESS_ID_POSTFIX = "";

    // -------------------------------------------------------------------------
    // BeanFactoryAware
    // -------------------------------------------------------------------------

    private BeanFactory beanFactory;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public final void setBeanFactory( BeanFactory beanFactory )
    {
        this.beanFactory = beanFactory;
    }

    // -------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------

    private String internalProcessIdPrefix = DEFAULT_INTERNAL_PROCESS_ID_PREFIX;

    /**
     * Sets the bean ID prefix.
     */
    public final void setInternalProcessIdPrefix( String internalProcessIdPrefix )
    {
        this.internalProcessIdPrefix = internalProcessIdPrefix;
    }

    private String internalProcessIdPostfix = DEFAULT_INTERNAL_PROCESS_ID_POSTFIX;

    /**
     * Sets the bean ID postfix.
     */
    public final void setInternalProcessIdPostfix( String internalProcessIdPostfix )
    {
        this.internalProcessIdPostfix = internalProcessIdPostfix;
    }

    // -------------------------------------------------------------------------
    // InternalProcessCoordinator
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.hisp.dhis.system.process.InternalProcessCoordinator#newProcess(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings( "unchecked" )
    public final <T extends InternalProcess> T newProcess( final String type, final String owner )
    {
        // ---------------------------------------------------------------------
        // Check argument and corresponding bean
        // ---------------------------------------------------------------------

        if ( type == null )
        {
            throw new IllegalArgumentException( "Argument is null" );
        }

        final String beanId = internalProcessIdPrefix + type + internalProcessIdPostfix;

        Object bean = beanFactory.getBean( beanId );

        if ( bean == null )
        {
            throw new IllegalArgumentException( "Internal process type not found: " + type );
        }

        if ( !InternalProcess.class.isAssignableFrom( bean.getClass() ) )
        {
            throw new IllegalArgumentException( "Argument does not reference an internal process type: " + type );
        }

        if ( !beanFactory.isPrototype( beanId ) )
        {
            throw new IllegalStateException( "Internal process bean \"" + beanId + "\" is not a prototype" );
        }

        T internalProcess = (T) bean; // Safe cast after above if-tests.

        internalProcess.setType( type );
        internalProcess.setOwner( owner );

        // ---------------------------------------------------------------------
        // Additional configuration
        // ---------------------------------------------------------------------

        postLoadConfiguration( internalProcess );

        log.debug( "Created new internal process, type: " + type );

        return internalProcess;
    }

    /**
     * Overridable method if additional common configuration is desired before
     * the process object is returned to the client.
     */
    @SuppressWarnings( "unused" )
    protected void postLoadConfiguration( InternalProcess internalProcess )
    {
    }
}
