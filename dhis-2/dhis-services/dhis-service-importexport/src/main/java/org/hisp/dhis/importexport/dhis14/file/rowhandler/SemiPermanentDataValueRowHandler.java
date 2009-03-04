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

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.converter.AbstractDataValueConverter;
import org.hisp.dhis.importexport.dhis14.object.Dhis14SemiPermanentDataValue;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.OnChangePeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: SemiPermanentDataValueRowHandler.java 5493 2008-07-23 21:57:40Z larshelg $
 */
public class SemiPermanentDataValueRowHandler
    extends AbstractDataValueConverter implements RowHandler
{
    private Map<Object, Integer> dataElementMapping;
    
    private Map<String, Integer> periodTypeMapping;
    
    private Map<Period, Integer> periodMapping;
    
    private Map<Object, Integer> organisationUnitMapping;

    private DataElementCategoryOptionCombo categoryOptionCombo;
    
    private DataElement element = new DataElement();

    private Period period = new Period();
    
    private PeriodType periodType = new OnChangePeriodType();
    
    private Source source = new OrganisationUnit();
    
    private DataValue value = new DataValue();    
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public SemiPermanentDataValueRowHandler( BatchHandler batchHandler,
        BatchHandler importDataValueBatchHandler,
        DataValueService dataValueService,
        Map<Object, Integer> dataElementMapping,
        Map<String, Integer> periodTypeMapping,
        Map<Period, Integer> periodMapping, 
        Map<Object, Integer> organisationUnitMapping,
        DataElementCategoryOptionCombo categoryOptionCombo,
        ImportParams params )
    {
        this.batchHandler = batchHandler;
        this.importDataValueBatchHandler = importDataValueBatchHandler;
        this.dataValueService = dataValueService;
        this.dataElementMapping = dataElementMapping;
        this.periodTypeMapping = periodTypeMapping;
        this.periodMapping = periodMapping;
        this.organisationUnitMapping = organisationUnitMapping;
        this.categoryOptionCombo = categoryOptionCombo;
        this.params = params;
    }

    // -------------------------------------------------------------------------
    // BatchRowHandler implementation
    // -------------------------------------------------------------------------

    public void handleRow( Object object )
    {
        Dhis14SemiPermanentDataValue dhis14Value = (Dhis14SemiPermanentDataValue) object;

        period.setPeriodType( periodType );
        
        period.setStartDate( dhis14Value.getStartDate() );
        period.setEndDate( dhis14Value.getEndDate() );
        period.getPeriodType().setId( periodTypeMapping.get( OnChangePeriodType.NAME ) );
        
        element.setId( dataElementMapping.get( dhis14Value.getDataElementId() ) );
        source.setId( organisationUnitMapping.get( dhis14Value.getOrganisationUnitId() ) );
        period.setId( periodMapping.get( period ) );
        
        value.setDataElement( element );
        value.setOptionCombo( categoryOptionCombo );
        value.setPeriod( period );
        value.setSource( source );
        value.setValue( String.valueOf( dhis14Value.getValue() ) );
        value.setComment( dhis14Value.getComment() );
        
        if ( value.getDataElement() != null && value.getPeriod() != null && value.getSource() != null )
        {
            read( value, DataValue.class, GroupMemberType.NONE, params );
        }
    }
}
