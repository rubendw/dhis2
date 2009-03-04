package org.hisp.dhis.datamart.crosstab;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.aggregation.batch.factory.BatchHandlerFactory;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.aggregation.batch.handler.DataValueCrossTabBatchHandler;
import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.datamart.crosstab.jdbc.CrossTabStore;
import org.hisp.dhis.system.process.PercentageBoundedProgressState;
import org.hisp.dhis.system.process.InternalProcess.State;

/**
 * @author Lars Helge Overland
 * @version $Id: DefaultCrossTabService.java 5510 2008-07-30 16:30:27Z larshelg $
 */
public class DefaultCrossTabService
    implements CrossTabService
{
    private static final Log log = LogFactory.getLog( DefaultCrossTabService.class );
    
    private static final int MAX_LENGTH = 20;
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private BatchHandlerFactory batchHandlerFactory;
        
    public void setBatchHandlerFactory( BatchHandlerFactory batchHandlerFactory )
    {
        this.batchHandlerFactory = batchHandlerFactory;
    }
    
    private CrossTabStore crossTabStore;
        
    public void setCrossTabStore( CrossTabStore crossTabTableManager )
    {
        this.crossTabStore = crossTabTableManager;
    }
    
    private DataMartStore dataMartStore;
        
    public void setDataMartStore( DataMartStore dataMartStore )
    {
        this.dataMartStore = dataMartStore;
    }
    
    // -------------------------------------------------------------------------
    // CrossTabService implementation
    // -------------------------------------------------------------------------

    public void populateCrossTabTable( Collection<Operand> operands, Collection<Integer> periodIds, 
        Collection<Integer> organisationUnitIds, State state )
    {
        if ( validate( operands, periodIds, organisationUnitIds ) )
        {
            List<Operand> operandList = new ArrayList<Operand>( operands );
            
            Collections.sort( operandList );

            crossTabStore.dropCrossTabTable();
            
            log.info( "Dropped crosstab table" );
            
            crossTabStore.createCrossTabTable( operandList );
            
            log.info( "Created crosstab table" );
            
            BatchHandler batchHandler = batchHandlerFactory.createBatchHandler( DataValueCrossTabBatchHandler.class );
            
            batchHandler.init();
            
            int total = periodIds.size();            
            int count = 0;
            
            for ( Integer periodId : periodIds )
            {
                for ( Integer sourceId : organisationUnitIds )
                {
                    Map<Operand, String> map = dataMartStore.getDataValueMap( periodId, sourceId );
                    
                    List<String> valueList = new ArrayList<String>();
                    
                    valueList.add( String.valueOf( periodId ) );
                    valueList.add( String.valueOf( sourceId ) );
                    
                    for ( Operand operand : operandList )
                    {
                        String value = map.get( operand );
                        
                        if ( value != null && value.length() > MAX_LENGTH )
                        {
                            log.warn( "Value ignored, too long: '" + value + 
                                "', for dataelement id: '" + operand.getDataElementId() +
                                "', categoryoptioncombo id: '" + operand.getOptionComboId() +
                                "', period id: '" + periodId + 
                                "', source id: '" + sourceId + "'" );
                            
                            value = null;
                        }
                        
                        valueList.add( value );
                    }
                    
                    batchHandler.addObject( valueList );
                }
                
                setProgress( state, ++count, total );
                
                log.info( "Crosstabulated data for period " + periodId );
            }
            
            batchHandler.flush();
            
            log.info( "Populated crosstab table" );
        }
    }
    
    public void dropCrossTabTable()
    {
        crossTabStore.dropCrossTabTable();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    /**
     * Sets the current progress.
     */
    private void setProgress( State state, int count, int total )
    {
        PercentageBoundedProgressState progressState = (PercentageBoundedProgressState) state;
        
        if ( progressState != null )
        {
            double percentage = ( count / (double) total ) * 100;
            
            progressState.setPercentageCompleted( (int) Math.round( percentage ) );
        }
    }

    /**
     * Validates whether the given collections of identifiers are not null and of size greater than 0.
     */
    private boolean validate( Collection<Operand> operands, Collection<Integer> periodIds, Collection<Integer> unitIds )
    {
        if ( operands == null || periodIds == null || unitIds == null )
        {
            return false;
        }
        
        if ( operands.size() == 0 || periodIds.size() == 0 || unitIds.size() == 0 )
        {
            return false;
        }
        
        return true;
    }
}
