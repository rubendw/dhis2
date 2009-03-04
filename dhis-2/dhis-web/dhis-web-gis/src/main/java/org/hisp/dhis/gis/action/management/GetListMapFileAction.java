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
 */
import java.io.File;
import java.util.*;

import org.hisp.dhis.gis.GISConfiguration;
import org.hisp.dhis.gis.action.configuration.GISConfigurationManagerService;
import org.hisp.dhis.gis.util.FileUtils;

import com.opensymphony.xwork.Action;

/**
 * @author Tran Thanh Tri
 * @version $Id: GetListMapFileAction.java 28-04-2008 16:06:00 $
 */
public class GetListMapFileAction
    implements Action
{

    private GISConfigurationManagerService gisConfigurationManagerService;

    public void setGisConfigurationManagerService( GISConfigurationManagerService gisConfigurationManagerService )
    {
        this.gisConfigurationManagerService = gisConfigurationManagerService;
    }

    private List<String> listMap = new ArrayList<String>();

    private String mapFile;

    public String getMapFile()
    {
        return mapFile;
    }

    public void setMapFile( String mapFile )
    {
        this.mapFile = mapFile;
    }

    public List<String> getListMap()
    {
        return listMap;
    }

    public void setListMap( List<String> listMap )
    {
        this.listMap = listMap;
    }

    public String execute()
        throws Exception
    {

        if ( gisConfigurationManagerService.isNULL( GISConfiguration.KEY_DIRECTORY )
            || gisConfigurationManagerService.isNULL( GISConfiguration.KEY_GETINDICATOR ) )
        {
            return "configuration";
        }
        
        File directory = gisConfigurationManagerService.getGISMapDirectory();

        listMap = FileUtils.getListFile( directory );

        if ( mapFile == null )
        {
            mapFile = "example.svg";
        }
        else if ( mapFile.trim().length() == 0 )
        {
            mapFile = "example.svg";
        }

        return SUCCESS;
    }

}
