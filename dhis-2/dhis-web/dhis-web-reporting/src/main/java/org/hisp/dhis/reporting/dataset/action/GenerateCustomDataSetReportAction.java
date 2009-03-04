package org.hisp.dhis.reporting.dataset.action;

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
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.aggregation.batch.statement.StatementManager;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataset.DataEntryForm;
import org.hisp.dhis.dataset.DataEntryFormService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.order.manager.DataElementOrderManager;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.oust.manager.SelectionTreeManager;
import org.hisp.dhis.reporting.dataset.generators.CustomDataSetReportGenerator;
import org.hisp.dhis.reporting.dataset.utils.NumberUtils;
import org.hisp.dhis.system.filter.AggregateableDataElementPredicate;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class GenerateCustomDataSetReportAction
    extends AbstractAction
{  
    // -----------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------
    
    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }   
        
    private DataElementOrderManager dataElementOrderManager;

    public void setDataElementOrderManager( DataElementOrderManager dataElementOrderManager )
    {
        this.dataElementOrderManager = dataElementOrderManager;
    }

    private StatementManager statementManager;

    public void setStatementManager( StatementManager statementManager )
    {
        this.statementManager = statementManager;
    }
    
    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }
    
    private SelectionTreeManager selectionTreeManager;

    public void setSelectionTreeManager( SelectionTreeManager selectionTreeManager )
    {
        this.selectionTreeManager = selectionTreeManager;
    }
    
    // -------------------------------------------------------------------------
    // Parameters
    // -------------------------------------------------------------------------      
    
    private String customDataEntryFormCode;

    public String getCustomDataEntryFormCode()
    {
        return this.customDataEntryFormCode;
    }
    
    private String reportingUnit;
    
    public String getReportingUnit()
    {
    	return this.reportingUnit;
    }
    
    private String reportingPeriod;
    
    public String getReportingPeriod()
    {
    	return this.reportingPeriod;
    }  
   
    // -----------------------------------------------------------------------
    // Action implementation
    // -----------------------------------------------------------------------
    
    public String execute()
        throws Exception
    {        
        OrganisationUnit orgUnit = selectionTreeManager.getSelectedOrganisationUnit();        
        
        if ( dataSetId() && orgUnit != null && startDate() && endDate() && isAfter()  )
        {
        	organisationUnitId = orgUnit.getId();
        	
            DataSet dataSet = dataSetService.getDataSet( dataSetId );
            
            Date sDate = format.parseDate( startDate );            
            Date eDate = format.parseDate( endDate );      

            Collection<DataElement> dataElements = dataElementOrderManager.getOrderedDataElements( dataSet );

            CollectionUtils.filter( dataElements, new AggregateableDataElementPredicate() );
            
            Map<String, String> aggregatedDataValueMap = new TreeMap<String,String>();                      
            
            statementManager.initialise();
            
            try
            {
            	for ( DataElement dataElement : dataElements )
            	{
                    DataElementCategoryCombo catCombo = dataElement.getCategoryCombo();                                        
                    
                    for( DataElementCategoryOptionCombo optionComb : catCombo.getOptionCombos() )
                    {
                        String value = new String();                       
                        
                        if( dataElement.getType().equals( DataElement.TYPE_INT ) )
                        {
                            double aggregatedValue = reportDataAccess.getAggregatedDataValue( dataElement.getId(), optionComb.getId(), sDate, eDate, String.valueOf( organisationUnitId ) );                        
                  
                            value = ( aggregatedValue != AggregationService.NO_VALUES_REGISTERED ) ?
                                NumberUtils.formatDataValue( aggregatedValue ) : "";                            
                                     
                            aggregatedDataValueMap.put(dataElement.getId() + ":" + optionComb.getId(), value);
                    	}
                        else
                        {                        	
                            aggregatedDataValueMap.put(dataElement.getId() + ":" + optionComb.getId(), " ");
                        }
                    }
                }
            }
            finally
            {
                statementManager.destroy();
            }
            
            // -----------------------------------------------------------------
            // Get the custom data entry form if any
            // -----------------------------------------------------------------

            DataEntryForm dataEntryForm = dataEntryFormService.getDataEntryFormByDataSet( dataSet );
            
            customDataEntryFormCode = CustomDataSetReportGenerator.prepareReportContent( dataEntryForm.getHtmlCode(), aggregatedDataValueMap );
            
            reportingUnit = reportDataAccess.getOrganisationUnitName( organisationUnitId );
            
            reportingPeriod = format.formatDate( sDate ) + " - " + format.formatDate( eDate );
           
            return SUCCESS;
        }
        
        return ERROR;
    }
}
