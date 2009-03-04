package org.hisp.dhis.dataset.action;

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

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;

/**
 * @author Kristian
 * @version $Id: ValidateDataSetAction.java 5607 2008-08-28 11:09:22Z larshelg $
 */
public class ValidateDataSetAction
    implements Action
{
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
    
    private Integer dataSetId;

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    // -------------------------------------------------------------------------
    // I18n
    // -------------------------------------------------------------------------

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
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
    // Execution
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // ---------------------------------------------------------------------
        // Name
        // ---------------------------------------------------------------------

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

            DataSet match = dataSetService.getDataSetByName( name );
    
            if ( match != null && ( dataSetId == null || match.getId() != dataSetId ) )
            {
                message = i18n.getString( "duplicate_names" );
    
                return INPUT;
            }
        }
        
        // ---------------------------------------------------------------------
        // Short name
        // ---------------------------------------------------------------------

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
                message = i18n.getString( "specify_name" );

                return INPUT;
            }

            DataSet match = dataSetService.getDataSetByShortName( shortName );
    
            if ( match != null && ( dataSetId == null || match.getId() != dataSetId ) )
            {
                message = i18n.getString( "duplicate_short_names" );
    
                return INPUT;
            }
        }
        
        return SUCCESS;
    }
}
