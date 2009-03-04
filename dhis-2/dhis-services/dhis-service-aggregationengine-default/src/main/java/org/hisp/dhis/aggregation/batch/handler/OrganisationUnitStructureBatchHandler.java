package org.hisp.dhis.aggregation.batch.handler;

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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.aggregation.OrganisationUnitStructure;
import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
/**
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitStructureBatchHandler.java 5359 2008-06-06 10:36:39Z larshelg $
 */
public class OrganisationUnitStructureBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
 
    public OrganisationUnitStructureBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
    }

    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "orgunitstructure";
    }
    
    protected void openSqlStatement()
    {
        statementBuilder.setAutoIncrementColumnIndex( 0 );
        statementBuilder.setAutoIncrementColumnName( "orgunitstructureid" );
        
        addColumns();
        
        sqlBuffer.append( statementBuilder.getInsertStatementOpening( tableName ) );
    }
    
    protected String getUpdateSqlStatement( Object object )
    {
        addColumns();
        
        addValues( object );
        
        return statementBuilder.getUpdateStatement( tableName );
    }
    
    protected String getIdentifierStatement( Object objectName )
    {
        return statementBuilder.getValueStatement( tableName, "orgunitstructureid", "organisationunitid", String.valueOf( objectName ) );
    }
    
    protected String getUniquenessStatement( Object object )
    {
        OrganisationUnitStructure structure = (OrganisationUnitStructure) object;
        
        Map<String, String> fieldMap = new HashMap<String, String>();
        
        fieldMap.put( "organisationUnitId", String.valueOf( structure.getOrganisationUnitId() ) );
        
        return statementBuilder.getValueStatement( tableName, "orgunitstructureid", fieldMap, false );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "organisationunitid" );
        statementBuilder.setColumn( "level" );
        
        statementBuilder.setColumn( "idlevel1" );
        statementBuilder.setColumn( "idlevel2" );
        statementBuilder.setColumn( "idlevel3" );
        statementBuilder.setColumn( "idlevel4" );
        statementBuilder.setColumn( "idlevel5" );
        statementBuilder.setColumn( "idlevel6" );
        statementBuilder.setColumn( "idlevel7" );
        statementBuilder.setColumn( "idlevel8" );
        
        statementBuilder.setColumn( "geolevel1" );
        statementBuilder.setColumn( "geolevel2" );
        statementBuilder.setColumn( "geolevel3" );
        statementBuilder.setColumn( "geolevel4" );
        statementBuilder.setColumn( "geolevel5" );
        statementBuilder.setColumn( "geolevel6" );
        statementBuilder.setColumn( "geolevel7" );
        statementBuilder.setColumn( "geolevel8" );
    }
    
    protected void addValues( Object object )
    {
        OrganisationUnitStructure structure = (OrganisationUnitStructure) object;
        
        statementBuilder.setInt( structure.getOrganisationUnitId() );
        statementBuilder.setInt( structure.getLevel() );
        
        statementBuilder.setInt( structure.getIdLevel1() );
        statementBuilder.setInt( structure.getIdLevel2() );
        statementBuilder.setInt( structure.getIdLevel3() );
        statementBuilder.setInt( structure.getIdLevel4() );
        statementBuilder.setInt( structure.getIdLevel5() );
        statementBuilder.setInt( structure.getIdLevel6() );
        statementBuilder.setInt( structure.getIdLevel7() );
        statementBuilder.setInt( structure.getIdLevel8() );

        statementBuilder.setString( structure.getGeoCodeLevel1() );
        statementBuilder.setString( structure.getGeoCodeLevel2() );
        statementBuilder.setString( structure.getGeoCodeLevel3() );
        statementBuilder.setString( structure.getGeoCodeLevel4() );
        statementBuilder.setString( structure.getGeoCodeLevel5() );
        statementBuilder.setString( structure.getGeoCodeLevel6() );
        statementBuilder.setString( structure.getGeoCodeLevel7() );
        statementBuilder.setString( structure.getGeoCodeLevel8() );
    }
}
