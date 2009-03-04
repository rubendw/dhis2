package org.hisp.dhis.importexport.dhis14.file.rowhandler;

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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.converter.AbstractIndicatorConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.system.util.UUIdUtils;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorRowHandler.java 5578 2008-08-22 07:17:34Z larshelg $
 */
public class IndicatorRowHandler
    extends AbstractIndicatorConverter implements RowHandler
{
    private static final Log log = LogFactory.getLog( IndicatorRowHandler.class );
    
    private Map<Object, Integer> indicatorTypeMap;
    
    private Map<Object, Integer> dataElementMap;
    
    private ImportParams params;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public IndicatorRowHandler( BatchHandler batchHandler, 
        ImportObjectService importObjectService,
        IndicatorService indicatorService,
        Map<Object, Integer> indicatorTypeMap, 
        Map<Object, Integer> dataElementMap,
        ImportParams params )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.indicatorService = indicatorService;
        this.indicatorTypeMap = indicatorTypeMap;
        this.dataElementMap = dataElementMap;
        this.params = params;
    }
    
    // -------------------------------------------------------------------------
    // BatchRowHandler implementation
    // -------------------------------------------------------------------------

    public void handleRow( Object object )
    {
        Indicator indicator = (Indicator) object;
        
        NameMappingUtil.addIndicatorMapping( indicator.getId(), indicator.getName() );
        
        indicator.setUuid( UUIdUtils.getUUId() );

        if ( indicator.getAlternativeName() != null && indicator.getAlternativeName().trim().length() == 0 )
        {
            indicator.setAlternativeName( null );
        }
        
        if ( indicator.getCode() != null && indicator.getCode().trim().length() == 0 )
        {
            indicator.setCode( null );
        }
        
        indicator.getIndicatorType().setId( indicatorTypeMap.get( indicator.getIndicatorType().getId() ) );            
        
        indicator.setNumeratorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        indicator.setDenominatorAggregationOperator( DataElement.AGGREGATION_OPERATOR_SUM );
        
        indicator.setNumerator( convertFormula( indicator.getNumerator(), indicator ) );
        indicator.setDenominator( convertFormula( indicator.getDenominator(), indicator ) );
            
        read( indicator, Indicator.class, GroupMemberType.NONE, params );
    }
    
    /**
     * Converts an Indicator formula with DHIS 1.4 specific identifiers to
     * a formula with DHIS 2 specific identifiers.
     * 
     * @param formula a DHIS 1.4 Indicator formula.
     * @return a DHIS 2 Indicator formula.
     * @throws IllegalArgumentException if DataElement identifier in formula 
     *         is not of type int.
     */
    private String convertFormula( String formula, Indicator indicator )
        throws IllegalArgumentException
    {
        StringBuffer convertedFormula = new StringBuffer();
        
        if ( formula != null )
        {
            // -------------------------------------------------------------------------
            // Replace DHIS 1.4 aggregation type annotation like "SUM" from formula  
            // -------------------------------------------------------------------------
    
            formula = formula.replaceAll( "[A-Z,a-z]", "" );
    
            // -------------------------------------------------------------------------
            // Extract DataElement identifiers on the form "[123]" from the formula
            // -------------------------------------------------------------------------
    
            Pattern pattern = Pattern.compile( "\\[\\d+\\]" );
            Matcher matcher = pattern.matcher( formula );
            
            while ( matcher.find() )
            {
                String replaceString = matcher.group();
    
                // ---------------------------------------------------------------------
                // Remove brackets to get identifier
                // ---------------------------------------------------------------------
    
                String id = replaceString.replaceAll( "[\\[\\]]", "" );
    
                // ---------------------------------------------------------------------
                // Parse identifier to int
                // ---------------------------------------------------------------------
    
                int dataElementId = -1;
                
                try
                {
                    dataElementId = Integer.parseInt( id );
                }
                catch ( NumberFormatException ex )
                {
                    throw new IllegalArgumentException( "Illegal identifier in formula - " + replaceString, ex );
                }
    
                // ---------------------------------------------------------------------
                // Get identifier from the DataElement in the database
                // ---------------------------------------------------------------------
    
                Integer convertedDataElementId = dataElementMap.get( dataElementId );
                
                if ( convertedDataElementId == null )
                {
                    log.warn( "The formula for indicator '" + indicator.getName() + 
                        "' contains a non-existing data element identifier: '" + dataElementId + "'" );
                    
                    convertedDataElementId = -1;
                }
                
                // ---------------------------------------------------------------------
                // Put brackets back on
                // ---------------------------------------------------------------------
    
                replaceString = "[" + convertedDataElementId + "]";
                
                matcher.appendReplacement( convertedFormula, replaceString );
            }
            
            matcher.appendTail( convertedFormula );
        }
        
        return convertedFormula.toString();
    }
}
