package org.hisp.dhis.options.displayproperty;

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

import java.util.List;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.organisationunit.OrganisationUnit;

/**
 * @author Lars Helge Overland
 * @version $Id: DisplayPropertyHandler.java 3796 2007-10-30 16:21:08Z larshelg $
 */
public class DisplayPropertyHandler
{
    private String displayProperty;

    public DisplayPropertyHandler( String displayProperty )
    {
        this.displayProperty = displayProperty;
    }
    
    public List<DataElement> handleDataElements( List<DataElement> list )
    {
        if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_SHORTNAME ) )
        {
            for ( DataElement element : list )
            {
                if ( element.getShortName() != null )
                {
                    element.setName( element.getShortName() );
                }
            }
        }
        
        else if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_ALTERNATIVENAME ) )
        {
            for ( DataElement element : list )
            {
                if ( element.getAlternativeName() != null )
                {
                    element.setName( element.getAlternativeName() );
                }
            }
        }
        
        else if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_CODE ) )
        {
            for ( DataElement element : list )
            {
                if ( element.getCode() != null )
                {
                    element.setName( element.getCode() );
                }
            }
        }
        
        return list;
    }

    public List<Indicator> handleIndicators( List<Indicator> list )
    {
        if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_SHORTNAME ) )
        {
            for ( Indicator indicator : list )
            {
                if ( indicator.getShortName() != null )
                {
                    indicator.setName( indicator.getShortName() );
                }
            }
        }
        
        else if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_ALTERNATIVENAME ) )
        {
            for ( Indicator indicator : list )
            {
                if ( indicator.getAlternativeName() != null )
                {
                    indicator.setName( indicator.getAlternativeName() );
                }
            }
        }
        
        else if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_CODE ) )
        {
            for ( Indicator indicator : list )
            {
                if ( indicator.getCode() != null )
                {    
                    indicator.setName( indicator.getCode() );
                }
            }
        }
        
        return list;
    }
    
    public List<OrganisationUnit> handleOrganisationUnits( List<OrganisationUnit> list )
    {
        if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_SHORTNAME ) )
        {
            for ( OrganisationUnit unit : list )
            {
                if ( unit.getShortName() != null )
                {
                    unit.setName( unit.getShortName() );
                }
            }
        }
        
        else if ( displayProperty.equals( DisplayPropertyManager.DISPLAY_PROPERTY_CODE ) )
        {
            for ( OrganisationUnit unit : list )
            {
                if ( unit.getOrganisationUnitCode() != null )
                {
                    unit.setName( unit.getOrganisationUnitCode() );
                }
            }
        }
        
        return list;
    }
}
