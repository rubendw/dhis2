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

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractPeriodConverter;
import org.hisp.dhis.importexport.ixf.util.IXFMappingUtil;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.system.util.DateUtils;

/**
 * @author Lars Helge Overland
 * @version $Id: TimePeriodConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class TimePeriodConverter
    extends AbstractPeriodConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "timePeriods";
    public static final String ELEMENT_NAME = "timePeriod";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------
    
    private Map<String, Integer> periodTypeMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    /**
     * Constructor for write operations.
     */
    public TimePeriodConverter()
    {
    }

    /**
     * Constructor for read operations.
     * 
     * @param periodService the periodService to use.
     * @param importObjectService the importObjectService to use.
     */
    public TimePeriodConverter( BatchHandler batchHandler, 
        PeriodService periodService, 
        ImportObjectService importObjectService,
        Map<String, Integer> periodTypeMapping )
    {
        this.batchHandler = batchHandler;
        this.periodService = periodService;
        this.importObjectService = importObjectService;
        this.periodTypeMapping = periodTypeMapping;
    }
    
    // -------------------------------------------------------------------------
    // IXFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {        
        Collection<Period> periods = params.getPeriods();
        
        if ( periods != null && periods.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
                        
            for ( Period period : periods )
            {
                String periodicity = IXFMappingUtil.getPeriodTypeToIXF( period.getPeriodType().getName() );
                
                String dateString = DateUtils.getMediumDateString( period.getStartDate() );
                
                String key = String.valueOf( period.getId() );
                
                writer.writeElement( ELEMENT_NAME, dateString, "key", key, "periodicity", periodicity );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }        
    }

    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            String periodKey = reader.getAttributeValue( "key" );
            
            String periodicity = reader.getAttributeValue( "periodicity" );
            
            Date startDate = DateUtils.getMediumDate( reader.getElementValue() );
            
            Period period = IXFMappingUtil.getPeriodFromIXF( periodicity, startDate );
            
            String periodTypeName = IXFMappingUtil.getPeriodTypeFromIXF( periodicity );
            
            period.getPeriodType().setId( periodTypeMapping.get( periodTypeName ) );
            
            NameMappingUtil.addPeriodMapping( periodKey, period );
            
            read( period, Period.class, GroupMemberType.NONE, params );
        }
    }
}
