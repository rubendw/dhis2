package org.hisp.dhis.settings.action.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.hisp.dhis.i18n.I18nService;
import org.hisp.dhis.i18n.locale.LocaleManager;

import com.opensymphony.xwork.ActionSupport;

/**
 * Created by IntelliJ IDEA.
 * User: yvindbrucker
 * Date: Mar 12, 2007
 * Time: 8:33:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetAvailableLocalesDbAction 
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private I18nService i18nService;

    public void setI18nService( I18nService i18nService )
    {
        this.i18nService = i18nService;
    }

    private LocaleManager localeManager;

    public void setLocaleManager( LocaleManager localeManager )
    {
        this.localeManager = localeManager;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<Locale> availableLocales;

    public List<Locale> getAvailableLocalesDb()
    {
        return availableLocales;
    }

    private Locale currentLocaleDb;

    public Locale getCurrentLocaleDb()
    {
        return currentLocaleDb;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        availableLocales = new ArrayList<Locale>( i18nService.getAvailableLocales() );

        if ( !availableLocales.contains( localeManager.getFallbackLocale() ) )
        {
            availableLocales.add( localeManager.getFallbackLocale() );
        }

        {
            Collections.sort( availableLocales, new Comparator<Locale>()
            {
                public int compare( Locale locale0, Locale locale1 )
                {
                    return locale0.getDisplayName().compareTo( locale1.getDisplayName() );
                }
            } );
        }

        currentLocaleDb = localeManager.getCurrentLocale();

        return SUCCESS;
    }
}
