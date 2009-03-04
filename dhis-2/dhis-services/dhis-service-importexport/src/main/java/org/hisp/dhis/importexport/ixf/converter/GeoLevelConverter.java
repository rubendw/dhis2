package org.hisp.dhis.importexport.ixf.converter;

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

import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.ixf.config.IXFConfiguration;
import org.hisp.dhis.importexport.ixf.config.IXFConfigurationManager;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: GeoLevelConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class GeoLevelConverter
    implements XMLConverter
{
    public static final String COLLECTION_NAME = "geoLevels";
    public static final String ELEMENT_NAME = "geoLevel";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------
    
    private IXFConfigurationManager configurationManager;

    private OrganisationUnitService organisationUnitService;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    /**
     * Constructor for write operations.
     * 
     * @param configurationManager the configurationManager to use.
     * @param organisationUnitService the organisationUnitService to use.
     */
    public GeoLevelConverter( IXFConfigurationManager configurationManager, OrganisationUnitService organisationUnitService )
    {
        this.configurationManager = configurationManager;
        this.organisationUnitService = organisationUnitService;
    }
    
    // -------------------------------------------------------------------------
    // IXFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {        
        IXFConfiguration config = configurationManager.getConfiguration();
        
        List<String> geoLevels = config.getLevelNames();
        
        geoLevels = verifyGeoLevels( geoLevels );
        
        writer.openElement( COLLECTION_NAME );
        
        int level = 0;
        
        for ( String geoLevel : geoLevels )
        {
            writer.openElement( ELEMENT_NAME, "key", String.valueOf( level++ ),
                "country-key", config.getCountry().getKey(), "geoType-key", "1" );

            writer.writeElement( "name", geoLevel, "lang", "en" );
            
            writer.closeElement( ELEMENT_NAME );
        }
        
        writer.closeElement( COLLECTION_NAME );
    }

    public void read( XMLReader reader, ImportParams params )
    {
        // Not in use
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private List<String> verifyGeoLevels( List<String> geoLevels )
    {
        int databaseLevels = organisationUnitService.getNumberOfOrganisationalLevels();
        
        int definedLevels = geoLevels.size();
        
        int excess = databaseLevels - definedLevels;
        
        for ( int i = 0; i < excess; i++ )
        {
            geoLevels.add( "Undefined" );
        }
        
        return geoLevels;
    }
}
