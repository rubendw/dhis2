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

import org.hisp.dhis.aggregation.batch.configuration.JDBCConfiguration;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;

/**
 * @author Lars Helge Overland
 * @version $Id: ExtendedDataElementBatchHandler.java 5805 2008-10-03 13:16:15Z larshelg $
 */
public class ExtendedDataElementBatchHandler
    extends AbstractBatchHandler
{
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public ExtendedDataElementBatchHandler( JDBCConfiguration configuration )
    {
        super( configuration );
        
        hasSinglePrimaryKey = false;
    }    

    // -------------------------------------------------------------------------
    // AbstractBatchHandler implementation
    // -------------------------------------------------------------------------

    protected void setTableName()
    {
        this.tableName = "extendeddataelement";
    }
    
    protected void openSqlStatement()
    {
        statementBuilder.setAutoIncrementColumnIndex( 0 );
        statementBuilder.setAutoIncrementColumnName( "extendeddataelementid" );
        
        addColumns();
        
        sqlBuffer.append( statementBuilder.getInsertStatementOpening( tableName ) );
    }
    
    protected String getUpdateSqlStatement( Object object )
    {
        ExtendedDataElement dataElement = (ExtendedDataElement) object;
        
        statementBuilder.setIdentifierColumnName( "extendeddataelementid" );
        statementBuilder.setIdentifierColumnValue( dataElement.getId() );
        
        addColumns();
        
        addValues( object );
        
        return statementBuilder.getUpdateStatement( tableName );
    }

    protected String getIdentifierStatement( Object objectName )
    {
        return statementBuilder.getValueStatement( "dataelement", "dataelementid", "name", String.valueOf( objectName ) );
    }
    
    protected String getUniquenessStatement( Object object )
    {
        DataElement dataElement = (DataElement) object;
        
        Map<String, String> map = new HashMap<String, String>();
        
        map.put( "name", dataElement.getName() );
        map.put( "shortname", dataElement.getShortName() );
        map.put( "code", dataElement.getCode() );
        map.put( "alternativename", dataElement.getAlternativeName() );
        
        return statementBuilder.getValueStatement( "dataelement", "dataelementid", map, false );
    }
    
    protected void addColumns()
    {
        statementBuilder.setColumn( "mnemonic" );
        statementBuilder.setColumn( "version" );
        statementBuilder.setColumn( "context" );
        statementBuilder.setColumn( "synonyms" );
        statementBuilder.setColumn( "hononyms" );
        statementBuilder.setColumn( "keywords" );
        statementBuilder.setColumn( "status" );
        statementBuilder.setColumn( "statusDate" );
        statementBuilder.setColumn( "dataElementType" );
        
        statementBuilder.setColumn( "dataType" );
        statementBuilder.setColumn( "representationalForm" );
        statementBuilder.setColumn( "representationalLayout" );
        statementBuilder.setColumn( "minimumSize" );
        statementBuilder.setColumn( "maximumSize" );
        statementBuilder.setColumn( "dataDomain" );
        statementBuilder.setColumn( "validationRules" );
        statementBuilder.setColumn( "relatedDataReferences" );
        statementBuilder.setColumn( "guideForUse" );        
        statementBuilder.setColumn( "collectionMethods" );
        
        statementBuilder.setColumn( "responsibleAuthority" );
        statementBuilder.setColumn( "updateRules" );
        statementBuilder.setColumn( "accessAuthority" );
        statementBuilder.setColumn( "updateFrequency" );
        statementBuilder.setColumn( "location" );
        statementBuilder.setColumn( "reportingMethods" );
        statementBuilder.setColumn( "versionStatus" );
        statementBuilder.setColumn( "previousVersionReferences" );
        statementBuilder.setColumn( "sourceDocument" );
        statementBuilder.setColumn( "sourceOrganisation" );
        statementBuilder.setColumn( "comment" );
        statementBuilder.setColumn( "saved" );
        statementBuilder.setColumn( "lastUpdated" );
    }
    
    protected void addValues( Object object )
    {
        ExtendedDataElement element = (ExtendedDataElement) object;
        
        statementBuilder.setString( element.getMnemonic() );
        statementBuilder.setString( element.getVersion() );
        statementBuilder.setString( element.getContext() );
        statementBuilder.setString( element.getSynonyms() );
        statementBuilder.setString( element.getHononyms() );
        statementBuilder.setString( element.getKeywords() );
        statementBuilder.setString( element.getStatus() );
        statementBuilder.setDate( element.getStatusDate() );
        statementBuilder.setString( element.getDataElementType() );
        
        statementBuilder.setString( element.getDataType() );
        statementBuilder.setString( element.getRepresentationalForm() );
        statementBuilder.setString( element.getRepresentationalLayout() );
        statementBuilder.setInt( element.getMinimumSize() );
        statementBuilder.setInt( element.getMaximumSize() );
        statementBuilder.setString( element.getDataDomain() );
        statementBuilder.setString( element.getValidationRules() );
        statementBuilder.setString( element.getRelatedDataReferences() );
        statementBuilder.setString( element.getGuideForUse() );
        statementBuilder.setString( element.getCollectionMethods() );
        
        statementBuilder.setString( element.getResponsibleAuthority() );
        statementBuilder.setString( element.getUpdateRules() );
        statementBuilder.setString( element.getAccessAuthority() );
        statementBuilder.setString( element.getUpdateFrequency() );
        statementBuilder.setString( element.getLocation() );
        statementBuilder.setString( element.getReportingMethods() );
        statementBuilder.setString( element.getVersionStatus() );
        statementBuilder.setString( element.getPreviousVersionReferences() );
        statementBuilder.setString( element.getSourceDocument() );
        statementBuilder.setString( element.getSourceOrganisation() );
        statementBuilder.setString( element.getComment() );
        statementBuilder.setDate( element.getSaved() );
        statementBuilder.setDate( element.getLastUpdated() );
    }        
}
