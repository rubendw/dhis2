package org.hisp.dhis;

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

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Trygve Laugst&oslash;l
 * @version $Id: DhisSpringTest.java 5797 2008-10-02 15:40:29Z larshelg $
 */
public class DhisSpringTest
    extends DhisTest
    implements BeanFactory
{
    private AbstractApplicationContext beanFactory;

    // -------------------------------------------------------------------------
    // Abstract methods
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
    }

    public void tearDownTest()
        throws Exception
    {
    }

    // -------------------------------------------------------------------------
    // JUnit TestCase fixture methods
    // -------------------------------------------------------------------------

    @Override
    public final void setUp()
        throws Exception
    {
        super.setUp();

        setUpSpring();

        executeStartupRoutines();

        setUpTest();
    }

    @Override
    public final void tearDown()
        throws Exception
    {
        tearDownTest();

        closeSession();

        tearDownSpring();

        super.tearDown();
    }

    // -------------------------------------------------------------------------
    // Spring support methods
    // -------------------------------------------------------------------------

    private final void setUpSpring()
    {
        String location = "classpath*:/META-INF/dhis/beans.xml";

        beanFactory = new ClassPathXmlApplicationContext( location );
    }

    private final void tearDownSpring()
    {
        beanFactory.close();
    }

    // -------------------------------------------------------------------------
    // Other support methods
    // -------------------------------------------------------------------------

    private void closeSession()
        throws Exception
    {
        String id = "org.hisp.dhis.hibernate.HibernateSessionManager";

        if ( beanFactory.containsBean( id ) )
        {
            Object object = beanFactory.getBean( id );

            Method method = object.getClass().getMethod( "closeCurrentSession", new Class[0] );

            method.invoke( object, new Object[0] );
        }
    }

    private void executeStartupRoutines()
        throws Exception
    {
        String id = "org.hisp.dhis.system.startup.StartupRoutineExecutor";

        if ( beanFactory.containsBean( id ) )
        {
            Object object = beanFactory.getBean( id );

            Method method = object.getClass().getMethod( "execute", new Class[0] );

            method.invoke( object, new Object[0] );
        }
    }

    /**
     * Sets a dependency on the target service. This method can be used to set
     * mock implementations of dependencies on services for testing purposes. The
     * advantage of using this method over setting the services directly is that
     * the test can still be executed against the interface type of the service;
     * making the test unaware of the implementation and thus re-usable. A weakness
     * is that the field name of the dependency must be assumed.
     * 
     * @param targetService the target service.
     * @param fieldName the name of the dependency field in the target service.
     * @param dependency the dependency.
     */
    protected void setDependency( Object targetService, String fieldName, Object dependency )
    {
        Class<?> clazz = dependency.getClass().getInterfaces()[0];
        
        setDependency( targetService, fieldName, dependency, clazz );
    }
    
    /**
     * Sets a dependency on the target service. This method can be used to set
     * mock implementations of dependencies on services for testing purposes. The
     * advantage of using this method over setting the services directly is that
     * the test can still be executed against the interface type of the service;
     * making the test unaware of the implementation and thus re-usable. A weakness
     * is that the field name of the dependency must be assumed.
     * 
     * @param targetService the target service.
     * @param fieldName the name of the dependency field in the target service.
     * @param dependency the dependency.
     * @param clazz the class type of the dependency.
     */
    protected void setDependency( Object targetService, String fieldName, Object dependency, Class<?> clazz )
    {
        try
        {
            String setMethodName = "set" + fieldName.substring( 0, 1 ).toUpperCase() + 
                fieldName.substring( 1, fieldName.length() );
            
            Class<?>[] argumentClass = new Class<?>[] { clazz };
            
            Method method = targetService.getClass().getMethod( setMethodName, argumentClass );
            
            method.invoke( targetService, dependency );
        }
        catch ( Exception ex )
        {
            throw new RuntimeException( "Failed to set dependency on service: " + fieldName, ex );
        }
    }
    
    // -------------------------------------------------------------------------
    // BeanFactory Implementation
    // -------------------------------------------------------------------------

    public Object getBean( String id )
    {
        return beanFactory.getBean( id );
    }

    public Object getBean( String id, Class clazz )
    {
        return beanFactory.getBean( id, clazz );
    }

    public boolean containsBean( String s )
    {
        return beanFactory.containsBean( s );
    }

    public boolean isSingleton( String s )
        throws NoSuchBeanDefinitionException
    {
        return beanFactory.isSingleton( s );
    }

    public boolean isPrototype( String s )
        throws NoSuchBeanDefinitionException
    {
        return beanFactory.isPrototype( s );
    }

    public Class getType( String s )
        throws NoSuchBeanDefinitionException
    {
        return beanFactory.getType( s );
    }

    public String[] getAliases( String s )
        throws NoSuchBeanDefinitionException
    {
        return beanFactory.getAliases( s );
    }

    public boolean isTypeMatch( String s, Class clazz )
        throws NoSuchBeanDefinitionException
    {
        return beanFactory.isTypeMatch( s, clazz );
    }

    public Object getBean( String s, Object[] arg )
        throws BeansException
    {
        return beanFactory.getBean( s, arg );
    }
}
