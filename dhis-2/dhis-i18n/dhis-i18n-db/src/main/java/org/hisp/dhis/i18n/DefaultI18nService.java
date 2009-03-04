package org.hisp.dhis.i18n;

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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.i18n.locale.LocaleManager;
import org.hisp.dhis.i18n.locale.LocaleManagerException;

/**
 * @author Oyvind Brucker
 */
public class DefaultI18nService
    implements I18nService
{
    private static final Log LOG = LogFactory.getLog( DefaultI18nService.class );

    Locale currentLocale = Locale.US;

    Locale systemLocale = Locale.UK;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    private TranslationStore translationStore;

    public void setTranslationStore( TranslationStore translationStore )
    {
        this.translationStore = translationStore;
    }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    private Collection<I18nObject> objects;

    public void setObjects( Collection<I18nObject> objects )
    {
        this.objects = objects;
    }

    // -------------------------------------------------------------------------
    // I18nService implementation
    // -------------------------------------------------------------------------

    private void internationalise( Object object, Locale locale )
    {
        for ( I18nObject i18nObject : objects )
        {
            if ( i18nObject.match( object ) )
            {
                Collection<Translation> translations = translationStore.getTranslations( getClassName( object ),
                    getId( object ), locale );

                Map<String, String> translationsCurrentLocale = convertTranslations( translations );

                // Dont initiate these unless needed..
                Collection<Translation> translationsFallback = null;
                Map<String, String> translationsFallbackLocale = null;

                List<String> propertyNames = i18nObject.getPropertyNames();

                for ( String property : propertyNames )
                {
                    String value = translationsCurrentLocale.get( property );

                    if ( value != null && !value.equals( "" ) )
                    {
                        setProperty( object, property, value );
                    }
                    else
                    {
                        if ( translationsFallback == null )
                        {
                            translationsFallback = translationStore.getTranslations( getClassName( object ),
                                getId( object ), localeManager.getFallbackLocale() );

                            translationsFallbackLocale = convertTranslations( translationsFallback );
                        }

                        value = translationsFallbackLocale.get( property );

                        if ( value != null && !value.equals( "" ) )
                        {
                            setProperty( object, property, value );
                        }
                    }
                }
            }
        }
    }

    public void internationalise( Object object )
    {
        if ( !isI18nObject( object ) | object == null )
        {
            return;
        }

        Locale locale = null;

        try
        {
            locale = localeManager.getCurrentLocale();
        }
        catch ( LocaleManagerException e )
        {

        }

        if ( locale == null )
        {
            LOG.error( "Unable to get current locale" );
        }
        else
        {
            internationalise( object, locale );
        }

    }

    public void internationaliseCollection( Collection intObjects )
    {
        Locale locale = null;

        try
        {
            locale = localeManager.getCurrentLocale();
        }
        catch ( LocaleManagerException e )
        {
            LOG.error( "Unable to get current locale: " + e );
            return;
        }

        if ( locale == null || intObjects == null )
        {
            return;
        }

        /**
         * Check if all objects are of the same type, in as good
         * as all cases this will be true so we use the optimized query for that.
         */
        
        boolean oneType = true;
        Class type = null;

        for ( Object object : intObjects )
        {
            if ( type == null )
            {
                type = object.getClass();
            }
            else
            {
                if ( !type.equals( object.getClass() ) )
                {
                    oneType = false;
                }
            }
        }

        if ( !oneType )
        {
            for ( Object object : intObjects )
            {
                internationalise( object );
            }
        }
        else if ( !intObjects.isEmpty() )
        {
            for ( I18nObject i18nObject : objects )
            {
                if ( i18nObject.match( intObjects.iterator().next() ) )
                {
                    Collection<Translation> allTranslations = translationStore.getTranslations( i18nObject
                        .getClassName(), locale );

                    // Don't initiate unless needed
                    
                    Collection<Translation> fallbackTranslations = null;
                    Map<String, String> fallbackTranslationsMap = null;

                    for ( Object object : intObjects )
                    {
                        Map<String, String> translations = getTranslationsForObject( allTranslations, getId( object ) );

                        for ( String property : translations.keySet() )
                        {
                            String value = translations.get( property );

                            if ( value != null && !value.equals( "" ) )
                            {
                                setProperty( object, property, value );
                            }
                            else
                            {
                                if ( fallbackTranslations == null )
                                {
                                    fallbackTranslations = translationStore.getTranslations( i18nObject.getClassName(),
                                        locale );

                                    fallbackTranslationsMap = getTranslationsForObject( fallbackTranslations,
                                        getId( object ) );
                                }

                                value = fallbackTranslationsMap.get( property );

                                if ( value != null && !value.equals( "" ) )
                                {
                                    setProperty( object, property, value );
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public List<String> getPropertyNames( String className )
    {
        for ( I18nObject i18nObject : objects )
        {
            if ( i18nObject.getClassName().equals( className ) )
            {
                return i18nObject.getPropertyNames();
            }
        }

        return null;
    }

    public Map<String, String> getPropertyNamesLabel( String className )
    {
        for ( I18nObject i18nObject : objects )
        {
            if ( i18nObject.getClassName().equals( className ) )
            {
                Map<String, String> propertyNamesLabel = new Hashtable<String, String>();

                for ( String property : i18nObject.getPropertyNames() )
                {
                    propertyNamesLabel.put( property, convertPropertyToKey( property ) );
                }

                return propertyNamesLabel;
            }
        }

        return null;
    }

    public Map<String, String> getTranslations( String className, int id, Locale locale )
    {
        Collection<Translation> translationsCol = translationStore.getTranslations( className, id, locale );

        return convertTranslations( translationsCol );
    }

    public void updateTranslation( String className, int id, Locale locale, Map<String, String> translations )
    {
        for ( String key : translations.keySet() )
        {
            String value = translations.get( key );

            if ( value != null && !value.equals( "" ) )
            {
                Translation translation = translationStore.getTranslation( className, id, locale, key );

                if ( translation != null )
                {
                    translation.setValue( value );
                    translationStore.updateTranslation( translation );
                }
                else
                {
                    translation = new Translation( className, id, locale.toString(), key, value );
                    translationStore.addTranslation( translation );
                }

            }
        }
    }

    public void verify( Object object )
    {
        if ( !isI18nObject( object ) | object == null )
        {
            return;
        }

        Locale locale = null;

        try
        {

            locale = localeManager.getCurrentLocale();
        }
        catch ( LocaleManagerException e )
        {
            LOG.error( "Unable to get current locale: " + e );
            return;
        }

        /**
         * Save translations
         */

        for ( I18nObject i18nObject : objects )
        {
            if ( i18nObject.match( object ) )
            {
                String className = getClassName( object );
                int id = getId( object );

                Map<String, String> translations = new Hashtable<String, String>();

                for ( String property : i18nObject.getPropertyNames() )
                {
                    String value = getProperty( object, property );

                    if ( value != null && !value.equals( "" ) )
                    {
                        translations.put( property, value );
                    }
                }

                updateTranslation( className, id, locale, translations );
            }
        }

        /**
         * Set properties to properties from the fallback locale
         */
        if ( !locale.equals( localeManager.getFallbackLocale() ) )
        {
            internationalise( object, localeManager.getFallbackLocale() );
        }

    }

    public void verifyCollection( Collection collection )
    {
        for ( Object object : collection )
        {
            verify( object );
        }
    }

    public void addObject( Object object )
    {
        if ( !isI18nObject( object ) )
        {
            return;
        }

        Locale locale = null;

        try
        {
            locale = localeManager.getCurrentLocale();
        }
        catch ( LocaleManagerException e )
        {
            // Handled in the next block
        }

        if ( locale == null )
        {
            LOG.warn( "Failed to get current locale while adding object" );

            return;
        }

        for ( I18nObject i18nObject : objects )
        {
            if ( i18nObject.match( object ) )
            {
                String className = getClassName( object );
                int id = getId( object );

                Map<String, String> translations = new Hashtable<String, String>();

                for ( String property : i18nObject.getPropertyNames() )
                {
                    String value = getProperty( object, property );

                    if ( value != null && !value.equals( "" ) )
                    {
                        translations.put( property, value );
                    }
                }

                if ( !translations.isEmpty() )
                {
                    updateTranslation( className, id, locale, translations );
                }
            }
        }
    }

    public void addTranslation( Object object, String property, String value, Locale locale )
    {
        if ( !isI18nObject( object ) | object == null )
        {
            return;
        }

        String className = getClassName( object );
        int id = getId( object );

        Map<String, String> translations = new Hashtable<String, String>();

        translations.put( property, value );

        updateTranslation( className, id, locale, translations );
    }

    public void removeObject( Object object )
    {
        if ( object != null )
        {
            translationStore.deleteTranslations( getClassName( object ), getId( object ) );
        }
    }

    public void setToFallback( Object object )
    {
        if ( !isI18nObject( object ) | object == null )
        {
            return;
        }

        internationalise( object, localeManager.getFallbackLocale() );
    }

    public Collection<Locale> getAvailableLocales()
    {
        List<Locale> locales;

        try
        {
            locales = localeManager.getLocalesOrderedByPriority();
        }
        catch ( LocaleManagerException e )
        {
            locales = new ArrayList<Locale>();
        }

        Collection<Locale> translationLocales = translationStore.getAvailableLocales();

        if ( translationLocales != null )
        {
            for ( Locale locale : translationLocales )
            {
                if ( !locales.contains( locale ) )
                {
                    locales.add( locale );
                }
            }
        }

        return locales;
    }

    // -------------------------------------------------------------------------
    // Support methods
    // -------------------------------------------------------------------------

    /**
     * Returns property/value pairs of translations for one object matching id.
     *
     * @param translations Collection to search
     * @param id           Object id
     * @return Map of property/value pairs
     */
    private Map<String, String> getTranslationsForObject( Collection<Translation> translations, int id )
    {
        Collection<Translation> objectTranslations = new ArrayList<Translation>();

        for ( Translation translation : translations )
        {
            if ( translation.getId() == id )
            {
                objectTranslations.add( translation );
            }
        }

        return convertTranslations( objectTranslations );
    }

    /**
     * Returns property/value pairs of a collection of translations as a map
     *
     * @param translations
     * @return Map containing translations
     */
    private Map<String, String> convertTranslations( Collection<Translation> translations )
    {
        Map<String, String> translationMap = new Hashtable<String, String>();

        for ( Translation translation : translations )
        {
            if ( translation.getProperty() != null && translation.getValue() != null )
            {
                translationMap.put( translation.getProperty(), translation.getValue() );
            }
        }

        return translationMap;
    }

    /**
     * Sets a property for the supplied object
     *
     * @param object Object to modify
     * @param name   Name of property to set
     * @param value  Value the property will be set to
     */
    private void setProperty( Object object, String name, String value )
    {
        Class c = object.getClass();

        Object[] arguments = new Object[] { value };

        Class[] parameterTypes = new Class[] { String.class };

        if ( name.length() > 0 )
        {
            name = "set" + name.substring( 0, 1 ).toUpperCase() + name.substring( 1, name.length() );
        }
        else
        {
            return;
        }

        try
        {
            Method concatMethod = c.getMethod( name, parameterTypes );

            concatMethod.invoke( object, arguments );
        }
        catch ( IllegalAccessException iae )
        {
            System.out.println( "Ex: " + iae );
        }
        catch ( NoSuchMethodException nsme )
        {
            System.out.println( "Ex: " + nsme );
        }
        catch ( InvocationTargetException ite )
        {
            System.out.println( "Ex: " + ite );
        }
    }

    /**
     * Fetch a property off the object using reflection
     *
     * @param object   Object to search
     * @param property Name of the property to get
     * @return the value of the property or null
     */
    private String getProperty( Object object, String property )
    {
        String value = null;

        property = property.substring( 0, 1 ).toUpperCase() + property.substring( 1, property.length() );

        Class c = object.getClass();

        Method method;

        try
        {
            method = c.getMethod( "get" + property );

            value = (String) method.invoke( object );
        }
        catch ( NoSuchMethodException e )
        {
            System.out.println( e );
        }
        catch ( IllegalAccessException e )
        {
            System.out.println( e );
        }
        catch ( InvocationTargetException e )
        {
            System.out.println( e );
        }

        return value;
    }

    /**
     * Returns the name of the class that the object is an instance of
     * org.hisp.dhis.indicator.Indicactor returns Indicator
     *
     * @param object Object to determine className of
     * @return String containing the class name
     */
    private String getClassName( Object object )
    {
        String pathToClassName = object.getClass().getName();

        int start = pathToClassName.lastIndexOf( "." );

        return pathToClassName.substring( start + 1, pathToClassName.length() );
    }

    /**
     * Calls the method getId for this object, throws exception if this fails.
     *
     * @param object object to call method on, needs to have the public method getId():int
     * @return The id
     */
    private int getId( Object object )
    {
        int result = -1;

        Class c = object.getClass();

        Method method;

        try
        {
            method = c.getMethod( "getId" );

            result = (Integer) method.invoke( object );
        }
        catch ( NoSuchMethodException e )
        {
            System.out.println( e );
        }
        catch ( IllegalAccessException e )
        {
            System.out.println( e );
        }
        catch ( InvocationTargetException e )
        {
            System.out.println( e );
        }

        return result;
    }

    /**
     * Converts the property to a i18n keystring
     * alternativeName produces alternative_name
     *
     * @param propName string to parse
     * @return Modified string
     */
    private String convertPropertyToKey( String propName )
    {
        String str = "";

        char[] chars = propName.toCharArray();

        for ( int i = 0; i < chars.length; i++ )
        {
            if ( Character.isUpperCase( chars[i] ) )
            {
                str += "_" + Character.toLowerCase( chars[i] );
            }
            else
            {
                str += chars[i];
            }
        }

        return str;
    }

    /**
     * Test if an object is enabled for i18n
     *
     * @param object Object to check
     * @return true if the object is enabled for i18n
     */
    private boolean isI18nObject( Object object )
    {
        for ( I18nObject i18nObject : objects )
        {
            if ( i18nObject.match( object ) )
            {
                return true;
            }
        }

        return false;
    }
}
