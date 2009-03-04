package org.hisp.dhis.gis.action.management;

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
 *//*
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

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.gis.GISConfiguration;
import org.hisp.dhis.gis.GISConfigurationService;
import org.hisp.dhis.gis.action.configuration.GISConfigurationManagerService;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: ConfigurationGISDirectoryAction.java 25-08-2008 16:06:00 $
 */
public class ChangeGISConfigurationAction
    implements Action
{

    private static final Log log = LogFactory.getLog( ChangeGISConfigurationAction.class );

    public static final int LOAD_CONFIGURATION = 1;

    public static final int UPDATE_CONFIGURATION = 2;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private GISConfigurationManagerService gisConfigurationManagerService;

    private GISConfigurationService gisConfigurationService;

    public void setGisConfigurationService( GISConfigurationService gisConfigurationService )
    {
        this.gisConfigurationService = gisConfigurationService;
    }

    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }

    // -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------

    private String directory;

    private Integer action;

    private String getIndicatorFrom;

    private Set<String> getIndicatorFroms;

    // -------------------------------------------------------------------------
    // Getter & Setter
    // -------------------------------------------------------------------------

    public void setDirectory( String directory )
    {
        this.directory = directory;
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setAction( Integer action )
    {
        this.action = action;
    }

    public Set<String> getGetIndicatorFroms()
    {
        return getIndicatorFroms;
    }

    public void setGetIndicatorFrom( String getIndicatorFrom )
    {
        this.getIndicatorFrom = getIndicatorFrom;
    }

    public String getGetIndicatorFrom()
    {
        return getIndicatorFrom;
    }

    // -------------------------------------------------------------------------
    // Action
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {

        if ( action != null )
        {
            if ( action.intValue() == LOAD_CONFIGURATION )
            {
                getIndicatorFroms = new HashSet<String>();

                getIndicatorFroms.add( GISConfiguration.AggregationService );

                getIndicatorFroms.add( GISConfiguration.AggregatedIndicatorValue );

                getIndicatorFrom = gisConfigurationManagerService.getIndicatorFrom();
                
                File file = gisConfigurationManagerService.getGISDirectory();
                
                if ( file != null )
                {
                    directory = file.getAbsolutePath();
                }

                return INPUT;
            }

            if ( action.intValue() == UPDATE_CONFIGURATION )
            {
                if ( directory != null )
                {
                    if ( directory.trim().length() != 0 )
                    {
                        
                        directory = directory.trim();
                                               
                        if ( gisConfigurationManagerService.isNULL( GISConfiguration.KEY_DIRECTORY )
                            || gisConfigurationManagerService.isNULL( GISConfiguration.KEY_GETINDICATOR ) )
                        {
                           
                            gisConfigurationService.add( GISConfiguration.KEY_DIRECTORY, directory );
                            
                            gisConfigurationService.add( GISConfiguration.KEY_GETINDICATOR, getIndicatorFrom );
                           
                        }else{
                            
                            gisConfigurationService.update( GISConfiguration.KEY_DIRECTORY, directory );

                            gisConfigurationService.update( GISConfiguration.KEY_GETINDICATOR, getIndicatorFrom );
                        }
                       
                    }

                }
                return SUCCESS;
            }
        }

        /*
         * StatementHolder holder = statementManager.getHolder();
         * 
         * Statement statement = holder.getStatement(); if ( action != null ) {
         * if ( action.intValue() == LOAD_CONFIGURATION ) { getIndicatorFroms =
         * new HashSet<String>();
         * 
         * getIndicatorFroms.add( GISConfiguration.AggregationService );
         * 
         * getIndicatorFroms.add( GISConfiguration.AggregatedIndicatorValue );
         * 
         * try { ResultSet resultSet = statement .executeQuery( "SELECT
         * directory,getindicatorfrom FROM gisconfiguration WHERE id = 1" );
         * 
         * resultSet.next();
         * 
         * directory = resultSet.getString( "directory" );
         * 
         * getIndicatorFrom = resultSet.getString( "getindicatorfrom" );
         *  } catch ( SQLException e ) { log.warn( "Can not read directory", e );
         *  }
         * 
         * return INPUT; } if ( action.intValue() == UPDATE_CONFIGURATION ) { if (
         * directory != null ) { if ( directory.trim().length() != 0 ) {
         * statement.executeUpdate( "UPDATE gisconfiguration SET directory='" +
         * directory + "' , getindicatorfrom='" + getIndicatorFrom + "' WHERE
         * id=1" ); }
         *  } return SUCCESS; } }
         * 
         */

        return NONE;

    }

}
