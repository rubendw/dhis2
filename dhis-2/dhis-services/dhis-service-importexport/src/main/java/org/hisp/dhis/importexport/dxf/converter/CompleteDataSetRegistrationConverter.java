package org.hisp.dhis.importexport.dxf.converter;

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
import java.util.Map;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractCompleteDataSetRegistrationConverter;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.system.util.DateUtils;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class CompleteDataSetRegistrationConverter
    extends AbstractCompleteDataSetRegistrationConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "completeDataSetRegistrations";
    public static final String ELEMENT_NAME = "registration";

    private static final String FIELD_DATASET = "dataSet";
    private static final String FIELD_PERIOD = "period";
    private static final String FIELD_SOURCE = "source";
    private static final String FIELD_DATE = "date";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private Map<Object, Integer> dataSetMapping;    
    private Map<Object, Integer> periodMapping;    
    private Map<Object, Integer> sourceMapping;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public CompleteDataSetRegistrationConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler the BatchHandler to use.
     * @param importObjectService the ImportObjectService to use.
     * @param params the ImportParams to use.
     * @param dataSetMapping the DataSet mapping to use.
     * @param periodMapping the Period mapping to use.
     * @param sourceMapping the Source mapping to use.
     */
    public CompleteDataSetRegistrationConverter( BatchHandler batchHandler,
        ImportObjectService importObjectService,
        ImportParams params,
        Map<Object, Integer> dataSetMapping,
        Map<Object, Integer> periodMapping,
        Map<Object, Integer> sourceMapping )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.params = params;
        this.dataSetMapping = dataSetMapping;
        this.periodMapping = periodMapping;
        this.sourceMapping = sourceMapping;
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<CompleteDataSetRegistration> registrations = params.getCompleteDataSetRegistrations();
        
        if ( registrations != null && registrations.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( CompleteDataSetRegistration registration : registrations )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_DATASET, String.valueOf( registration.getDataSet().getId() ) );
                writer.writeElement( FIELD_PERIOD, String.valueOf( registration.getPeriod().getId() ) );
                writer.writeElement( FIELD_SOURCE, String.valueOf( registration.getSource().getId() ) );
                writer.writeElement( FIELD_DATE, DateUtils.getMediumDateString( registration.getDate() ) );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            CompleteDataSetRegistration registration = new CompleteDataSetRegistration();
            
            DataSet dataSet = new DataSet();
            registration.setDataSet( dataSet );
            
            Period period = new Period();
            registration.setPeriod( period );
            
            Source source = new OrganisationUnit();
            registration.setSource( source );
            
            reader.moveToStartElement( FIELD_DATASET );
            registration.getDataSet().setId( dataSetMapping.get( Integer.parseInt( reader.getElementValue() ) ) );
            
            reader.moveToStartElement( FIELD_PERIOD );
            registration.getPeriod().setId( periodMapping.get( Integer.parseInt( reader.getElementValue() ) ) );
            
            reader.moveToStartElement( FIELD_SOURCE );
            registration.getSource().setId( sourceMapping.get( Integer.parseInt( reader.getElementValue() ) ) );
            
            reader.moveToStartElement( FIELD_DATE );
            registration.setDate( DateUtils.getMediumDate( reader.getElementValue() ) );
            
            read( registration, CompleteDataSetRegistration.class, GroupMemberType.NONE, params );
        }
    }    
}
