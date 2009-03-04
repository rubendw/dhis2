package org.hisp.dhis.dd.action.extendedindicator;

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

import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;

import com.opensymphony.xwork.ActionSupport;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ValidateExtendedIndicatorAction
    extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }
    
    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------

    private Integer id;

    public void setId( Integer id )
    {
        this.id = id;
    }

    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private String shortName;

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    private String alternativeName;

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }
    
    private String code;

    public void setCode( String code )
    {
        this.code = code;
    }
    
    private String numeratorDescription;

    public void setNumeratorDescription( String numeratorDescription )
    {
        this.numeratorDescription = numeratorDescription;
    }
    
    private String denominatorDescription;

    public void setDenominatorDescription( String denominatorDescription )
    {
        this.denominatorDescription = denominatorDescription;
    }

    private String mnemonic;

    public void setMnemonic( String mnemonic )
    {
        this.mnemonic = mnemonic;
    }    
    
    private String version;

    public void setVersion( String version )
    {
        this.version = version;
    }
    
    private String description;

    public void setDescription( String description )
    {
        this.description = description;
    }

    private String keywords;

    public void setKeywords( String keywords )
    {
        this.keywords = keywords;
    }
    
    private String minimumSize;

    public void setMinimumSize( String minimumSize )
    {
        this.minimumSize = minimumSize;
    }

    private String maximumSize;

    public void setMaximumSize( String maximumSize )
    {
        this.maximumSize = maximumSize;
    }
    
    private String responsibleAuthority;

    public void setResponsibleAuthority( String responsibleAuthority )
    {
        this.responsibleAuthority = responsibleAuthority;
    }

    private String location;

    public void setLocation( String location )
    {
        this.location = location;
    }

    private String reportingMethods;

    public void setReportingMethods( String reportingMethods )
    {
        this.reportingMethods = reportingMethods;
    }

    private String versionStatus;

    public void setVersionStatus( String versionStatus )
    {
        this.versionStatus = versionStatus;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        // -------------------------------------------------------------------------
        // name: required / unique
        // -------------------------------------------------------------------------

        if ( name == null )
        {
            message = i18n.getString( "specify_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "specify_name" );

                return INPUT;
            }

            Indicator match = indicatorService.getIndicatorByName( name );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "name_in_use" );

                return INPUT;
            }
        }
        
        // -------------------------------------------------------------------------
        // shortName: required / unique
        // -------------------------------------------------------------------------

        if ( shortName == null )
        {
            message = i18n.getString( "specify_short_name" );

            return INPUT;
        }
        else
        {
            shortName = shortName.trim();

            if ( shortName.length() == 0 )
            {
                message = i18n.getString( "specify_short_name" );

                return INPUT;
            }

            Indicator match = indicatorService.getIndicatorByShortName( shortName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "short_name_in_use" );

                return INPUT;
            }
        }
        
        // -------------------------------------------------------------------------
        // alternativeName: unique
        // -------------------------------------------------------------------------

        if ( alternativeName != null && alternativeName.trim().length() != 0 )
        {
            Indicator match = indicatorService.getIndicatorByAlternativeName( alternativeName );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "alternative_name_in_use" );

                return INPUT;
            }
        }
        
        // -------------------------------------------------------------------------
        // code: unique
        // -------------------------------------------------------------------------

        if ( code != null && code.trim().length() != 0 )
        {
            Indicator match = indicatorService.getIndicatorByCode( code );

            if ( match != null && (id == null || match.getId() != id) )
            {
                message = i18n.getString( "code_in_use" );

                return INPUT;
            }
        }

        // -------------------------------------------------------------------------
        // description: required
        // -------------------------------------------------------------------------

        if ( description == null || description.trim().length() == 0 )
        {
            message = i18n.getString( "specify_description" );
            
            return INPUT;
        }

        // -------------------------------------------------------------------------
        // numeratorDescription: required
        // -------------------------------------------------------------------------

        if ( numeratorDescription == null )
        {
            message = i18n.getString( "specify_numerator_description" );
        }

        // -------------------------------------------------------------------------
        // denominatorDescription: required
        // -------------------------------------------------------------------------

        if ( denominatorDescription == null )
        {
            message = i18n.getString( "specify_denominator_description" );
        }

        // -------------------------------------------------------------------------
        // mnemonic: required
        // -------------------------------------------------------------------------
        
        if ( mnemonic == null || mnemonic.trim().length() == 0 )
        {
            message = i18n.getString( "specify_mnemonic" );
            
            return INPUT;
        }

        // -------------------------------------------------------------------------
        // version: required
        // -------------------------------------------------------------------------

        if ( version == null || version.trim().length() == 0 )
        {
            message = i18n.getString( "specify_version" );
            
            return INPUT;
        }

        // -------------------------------------------------------------------------
        // keywords: required
        // -------------------------------------------------------------------------

        if ( keywords == null || keywords.trim().length() == 0 )
        {
            message = i18n.getString( "specify_keywords" );
            
            return INPUT;
        }
        
        // -------------------------------------------------------------------------
        // minimumSize: distinct number
        // -------------------------------------------------------------------------

        if ( minimumSize != null && minimumSize.trim().length() > 0 )
        {
            try
            {
                Integer.parseInt( minimumSize );
            }
            catch ( NumberFormatException ex )
            {
                message = i18n.getString( "specify_integer_minimum_size" );
                
                return INPUT;
            }
        }

        // -------------------------------------------------------------------------
        // maximumSize: distinct number
        // -------------------------------------------------------------------------

        if ( maximumSize != null && maximumSize.trim().length() > 0  )
        {
            try
            {
                Integer.parseInt( maximumSize );
            }
            catch ( NumberFormatException ex )
            {
                message = i18n.getString( "specify_integer_maximum_size" );
                
                return INPUT;
            }
        }
        
        // -------------------------------------------------------------------------
        // responsibleAuthority: required
        // -------------------------------------------------------------------------

        if ( responsibleAuthority == null || responsibleAuthority.trim().length() == 0 )
        {
            message = i18n.getString( "specify_responsible_authority" );
            
            return INPUT;
        }
        
        // -------------------------------------------------------------------------
        // location: required
        // -------------------------------------------------------------------------

        if ( location == null || location.trim().length() == 0 )
        {
            message = i18n.getString( "specify_location" );
            
            return INPUT;
        }

        // -------------------------------------------------------------------------
        // reportingMethods: required
        // -------------------------------------------------------------------------

        if ( reportingMethods == null || reportingMethods.trim().length() == 0 )
        {
            message = i18n.getString( "specify_reporting_methods" );
            
            return INPUT;
        }

        // -------------------------------------------------------------------------
        // versionStatus: required
        // -------------------------------------------------------------------------

        if ( versionStatus == null || versionStatus.trim().length() == 0 )
        {
            message = i18n.getString( "specify_version_status" );
            
            return INPUT;
        }

        message = i18n.getString( "everything_is_ok" );

        return SUCCESS;
    }
}
