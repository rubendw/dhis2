package org.hisp.dhis.de.action.multidimensional;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrder;
import org.hisp.dhis.dataelement.DataElementDimensionColumnOrderService;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrder;
import org.hisp.dhis.dataelement.DataElementDimensionRowOrderService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataEntryForm;
import org.hisp.dhis.dataset.DataEntryFormService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.de.comments.StandardCommentsManager;
import org.hisp.dhis.order.manager.DataElementOrderManager;
import org.hisp.dhis.de.state.SelectedStateManager;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.minmax.MinMaxDataElement;
import org.hisp.dhis.minmax.MinMaxDataElementStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;

import com.opensymphony.xwork.Action;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class FormAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataEntryFormService dataEntryFormService;

    public void setDataEntryFormService( DataEntryFormService dataEntryFormService )
    {
        this.dataEntryFormService = dataEntryFormService;
    }

    private DataElementOrderManager dataElementOrderManager;

    public void setDataElementOrderManager( DataElementOrderManager dataElementOrderManager )
    {
        this.dataElementOrderManager = dataElementOrderManager;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private StandardCommentsManager standardCommentsManager;

    public void setStandardCommentsManager( StandardCommentsManager standardCommentsManager )
    {
        this.standardCommentsManager = standardCommentsManager;
    }

    private MinMaxDataElementStore minMaxDataElementStore;

    public void setMinMaxDataElementStore( MinMaxDataElementStore minMaxDataElementStore )
    {
        this.minMaxDataElementStore = minMaxDataElementStore;
    }

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }
    
    private DataElementDimensionRowOrderService dataElementDimensionRowOrderService;    

    public void setDataElementDimensionRowOrderService( DataElementDimensionRowOrderService dataElementDimensionRowOrderService )
    {
    	this.dataElementDimensionRowOrderService = dataElementDimensionRowOrderService;
    }  
    
    private DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService;    

    public void setDataElementDimensionColumnOrderService( DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService )
    {
    	this.dataElementDimensionColumnOrderService = dataElementDimensionColumnOrderService;
    }

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private List<DataElement> orderedDataElements = new ArrayList<DataElement>();

    public List<DataElement> getOrderedDataElements()
    {
        return orderedDataElements;
    }

    private Map<String, DataValue> dataValueMap;

    public Map<String, DataValue> getDataValueMap()
    {
        return dataValueMap;
    }

    private Map<CalculatedDataElement, Integer> calculatedValueMap;

    public Map<CalculatedDataElement, Integer> getCalculatedValueMap()
    {
        return calculatedValueMap;
    }

    private List<String> standardComments;

    public List<String> getStandardComments()
    {
        return standardComments;
    }

    private Map<String, String> dataElementTypeMap;

    public Map<String, String> getDataElementTypeMap()
    {
        return dataElementTypeMap;
    }

    private Map<Integer, MinMaxDataElement> minMaxMap;

    public Map<Integer, MinMaxDataElement> getMinMaxMap()
    {
        return minMaxMap;
    }

    private Integer integer = new Integer( 0 );

    public Integer getInteger()
    {
        return integer;
    }
   
    Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap = new HashMap<Integer, Collection<DataElementCategoryOption>>();

    public Map<Integer, Collection<DataElementCategoryOption>> getOrderedOptionsMap()
    {
        return orderedOptionsMap;
    }

    private Collection<DataElementCategory> orderedCategories;

    public Collection<DataElementCategory> getOrderedCategories()
    {
        return orderedCategories;
    }

    private Integer numberOfTotalColumns;

    public Integer getNumberOfTotalColumns()
    {
        return numberOfTotalColumns;
    }

    Map<Integer, Collection<Integer>> catColRepeat = new HashMap<Integer, Collection<Integer>>();

    public Map<Integer, Collection<Integer>> getCatColRepeat()
    {
        return catColRepeat;
    }
    
    Collection<DataElementCategoryOptionCombo> orderdCategoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();

    public Collection<DataElementCategoryOptionCombo> getOrderdCategoryOptionCombos()
    {
        return orderdCategoryOptionCombos;
    }    
    
    Map<Integer, String> optionComboNames = new HashMap<Integer, String>();

    public Map<Integer, String> getOptionComboNames()
    {
        return optionComboNames;
    }

    private Boolean cdeFormExists;

    public Boolean getCdeFormExists()
    {
        return cdeFormExists;
    }
    
    private DataEntryForm dataEntryForm;

    public DataEntryForm getDataEntryForm()
    {
        return this.dataEntryForm;
    }

    private String customDataEntryFormCode;

    public String getCustomDataEntryFormCode()
    {
        return this.customDataEntryFormCode;
    }

    // -------------------------------------------------------------------------
    // Input/output
    // -------------------------------------------------------------------------

    private Integer selectedDataSetId;

    public void setSelectedDataSetId( Integer selectedDataSetId )
    {
        this.selectedDataSetId = selectedDataSetId;
    }

    public Integer getSelectedDataSetId()
    {
        return selectedDataSetId;
    }

    private Integer selectedPeriodIndex;

    public void setSelectedPeriodIndex( Integer selectedPeriodIndex )
    {
        this.selectedPeriodIndex = selectedPeriodIndex;
    }

    public Integer getSelectedPeriodIndex()
    {
        return selectedPeriodIndex;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        DataSet dataSet = selectedStateManager.getSelectedDataSet();

        Period period = selectedStateManager.getSelectedPeriod();

        Collection<DataElement> dataElements = dataSet.getDataElements();

        if ( dataElements.size() == 0 )
        {
            return SUCCESS;
        }
        
        for( DataElement de : dataElements )
        {  
        	Collection<DataElementCategoryOptionCombo> optionCombos = dataElementCategoryOptionComboService.sortDataElementCategoryOptionCombos( de.getCategoryCombo() ) ;
        	
        	for( DataElementCategoryOptionCombo optionCombo : optionCombos )
        	{
        		if( !orderdCategoryOptionCombos.contains( optionCombo ) )
        		{
        			orderdCategoryOptionCombos.add(optionCombo);
        		}
        	}	
        }
        
        /*
         * Perform ordering of categories and their options so that they could
         * be displayed as in the paper form.        
         * 
         * Note that the total number of entry cells to be generated are the
         * multiple of options each category is going to provide!
         */
        DataElement sample = dataElements.iterator().next();
        DataElementCategoryCombo decbo = sample.getCategoryCombo();
        
        // Get the optioncombos for which the dataelments are going to collect
        // data.
        
        // OrderdCategoryOptionCombos = dataElementCategoryOptionComboService.sortDataElementCategoryOptionCombos( decbo );        

        List<DataElementCategory> categories = new ArrayList<DataElementCategory>( decbo.getCategories() );
        Map<Integer, DataElementCategory> categoryMap = new TreeMap<Integer, DataElementCategory>();

        // numberOfTotalColumns = 1;
        
        numberOfTotalColumns = orderdCategoryOptionCombos.size();

        // Get the order of categories
        for ( DataElementCategory category : categories )
        {
            // NumberOfTotalColumns = numberOfTotalColumns * category.getCategoryOptions().size();        
        	DataElementDimensionRowOrder rowOrder = dataElementDimensionRowOrderService.getDataElementDimensionRowOrder( decbo, category );
        	
        	if( rowOrder != null )
        	{
        		categoryMap.put( rowOrder.getDisplayOrder(), category );
        	}
        	else
        	{
        		categoryMap.put( category.getId(), category );
        	}
        }

        orderedCategories = categoryMap.values();
        
        //Get the order of options        
        for ( DataElementCategory dec : orderedCategories )
        {
            Map<Integer, DataElementCategoryOption> optionsMap = new TreeMap<Integer, DataElementCategoryOption>();

            for ( DataElementCategoryOption option : dec.getCategoryOptions() )      
            {
            	DataElementDimensionColumnOrder columnOrder = dataElementDimensionColumnOrderService.getDataElementDimensionColumnOrder( dec, option );
            	
            	if( columnOrder != null )
            	{
            		optionsMap.put( columnOrder.getDisplayOrder(), option );
            	}
            	else
            	{
            		optionsMap.put( option.getId(), option );
            	}
            	
            }            	

            orderedOptionsMap.put( dec.getId(), optionsMap.values() );
        }

        /*
         * Calculating the number of times each category is supposed to be
         * repeated in the dataentry form.
         */
        int catColSpan = numberOfTotalColumns;
        Map<Integer, Integer> catRepeat = new HashMap<Integer, Integer>();

        for ( DataElementCategory cat : orderedCategories )
        {
            catColSpan = catColSpan / cat.getCategoryOptions().size();
            int total = numberOfTotalColumns / (catColSpan * cat.getCategoryOptions().size());
            Collection<Integer> cols = new ArrayList<Integer>( total );

            for ( int i = 0; i < total; i++ )
            {
                cols.add( i );
            }

            // Cols is made to be a collection simply to facilitate a for loop
            // in the velocity - there should be a better way of "for" doing a
            // loop.
            catColRepeat.put( cat.getId(), cols );
            
            catRepeat.put( cat.getId(), catColSpan );
        }       
        
        for( DataElementCategoryOptionCombo deOptionCombo : orderdCategoryOptionCombos )
        {
        	optionComboNames.put( deOptionCombo.getId(), dataElementCategoryOptionComboService.getOptionNames( deOptionCombo ) );        	
        }
        
        // ---------------------------------------------------------------------
        // Get the min/max values
        // ---------------------------------------------------------------------
        
        Collection<MinMaxDataElement> minMaxDataElements = minMaxDataElementStore.getMinMaxDataElements(
            organisationUnit, dataElements );

        minMaxMap = new HashMap<Integer, MinMaxDataElement>( minMaxDataElements.size() );

        for ( MinMaxDataElement minMaxDataElement : minMaxDataElements )
        {
            minMaxMap.put( minMaxDataElement.getDataElement().getId(), minMaxDataElement );
        }

        // ---------------------------------------------------------------------
        // Order the DataElements
        // ---------------------------------------------------------------------

        orderedDataElements = dataElementOrderManager.getOrderedDataElements( dataSet );

        // ---------------------------------------------------------------------
        // Get the DataValues and create a map
        // ---------------------------------------------------------------------

        Collection<DataValue> dataValues = dataValueService.getDataValues( organisationUnit, period, dataElements,
        		orderdCategoryOptionCombos );

        dataValueMap = new HashMap<String, DataValue>( dataValues.size() );

        for ( DataValue dataValue : dataValues )
        {
            Integer deId = dataValue.getDataElement().getId();
            Integer ocId = dataValue.getOptionCombo().getId();

            dataValueMap.put( deId.toString() + ':' + ocId.toString(), dataValue );
        }

        // ---------------------------------------------------------------------
        // Prepare values for unsaved CalculatedDataElements
        // ---------------------------------------------------------------------

        CalculatedDataElement cde;

        calculatedValueMap = new HashMap<CalculatedDataElement, Integer>();

        Map<DataElement, Integer> factorMap;

        DataValue dataValue;
        int factor;
        int value = 0;

        for ( DataElement dataElement : dataElements )
        {
            if ( !(dataElement instanceof CalculatedDataElement) )
            {
                continue;
            }

            cde = (CalculatedDataElement) dataElement;

            if ( cde.isSaved() )
            {
                continue;
            }

            factorMap = dataElementService.getDataElementFactors( cde );

            for ( DataElement cdeElement : cde.getExpression().getDataElementsInExpression() )
            {
                factor = factorMap.get( cdeElement );
                dataValue = dataValueMap.get( cdeElement.getId() );

                if ( dataValue != null )
                {
                    value += Integer.parseInt( dataValue.getValue() ) * factor;
                }
            }

            calculatedValueMap.put( cde, value );

            value = 0;
        }

        // ---------------------------------------------------------------------
        // Make the standard comments available
        // ---------------------------------------------------------------------

        standardComments = standardCommentsManager.getStandardComments();

        // ---------------------------------------------------------------------
        // Make the DataElement types available
        // ---------------------------------------------------------------------

        dataElementTypeMap = new HashMap<String, String>();
        dataElementTypeMap.put( DataElement.TYPE_BOOL, i18n.getString( "yes_no" ) );
        dataElementTypeMap.put( DataElement.TYPE_INT, i18n.getString( "number" ) );
        dataElementTypeMap.put( DataElement.TYPE_STRING, i18n.getString( "text" ) );

        // ---------------------------------------------------------------------
        // Get the custom data entry form (if any)
        // ---------------------------------------------------------------------

        // Locate custom data entry form belonging to dataset, if any.
        dataEntryForm = dataEntryFormService.getDataEntryFormByDataSet( dataSet );
        cdeFormExists = (dataEntryForm != null);

        // Add JS and meta data to dynamically persist values of form.
        if ( cdeFormExists )
        {
            customDataEntryFormCode = prepareDataEntryFormCode( dataEntryForm.getHtmlCode(), dataValues );
        }

        return SUCCESS;
    }

    /**
     * Prepares the data entry form code by preparing the data element
     * place holders with code for displaying, manipulating and persisting data
     * elements.
     * 
     * The function in turn calls separate functions for preparing boolean and
     * non-boolean data elements.
     * 
     */
    private String prepareDataEntryFormCode( String dataEntryFormCode, Collection<DataValue> dataValues )
    {

        String preparedCode = dataEntryFormCode;
        
        preparedCode = prepareDataEntryFormInputsAndCombos( preparedCode, dataValues );

        return preparedCode;

    }

    private String prepareDataEntryFormInputsAndCombos( String dataEntryFormCode, Collection<DataValue> dataValues )
    {
        // ---------------------------------------------------------------------
        // Inline Javascript to add to HTML before outputting.
        // ---------------------------------------------------------------------
        
        final String jsCodeForInputs = " onchange=\"saveValue( $DATAELEMENTID, $OPTIONCOMBOID, '$DATAELEMENTNAME' )\" onkeypress=\"return keyPress(event, this)\" style=\"text-align:center\" ";
        final String jsCodeForCombos = " onchange=\"saveBoolean( $DATAELEMENTID, $OPTIONCOMBOID, this )\">";
        final String historyCode = " ondblclick='javascript:viewHistory( $DATAELEMENTID  )' ";
        final String calDataElementCode = " class=\"calculated\" disabled ";
        
        // ---------------------------------------------------------------------
        // Metadata code to add to HTML before outputting.
        // ---------------------------------------------------------------------
        
        final String metaDataCode = "<span id=\"value[$DATAELEMENTID].name\" style=\"display:none\">$DATAELEMENTNAME</span>"
            + "<span id=\"value[$DATAELEMENTID].type\" style=\"display:none\">$DATAELEMENTTYPE</span>"
            + "<div id=\"value[$DATAELEMENTID].min\" style=\"display:none\">$MIN</div>"
            + "<div id=\"value[$DATAELEMENTID].max\" style=\"display:none\">$MAX</div>";

        // Buffer to contain the final result.
        StringBuffer sb = new StringBuffer();

        // Pattern to match data elements in the HTML code.
        Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>", Pattern.DOTALL );
        Matcher matDataElement = patDataElement.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields.
        // ---------------------------------------------------------------------
        
        boolean result = matDataElement.find();
        
        while ( result )
        {        	
            // Get input HTML code (HTML input field code).
            String dataElementCode = matDataElement.group( 1 );

            // Pattern to extract data element ID from data element field            
            Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].value:value\\[(.*)\\].value" );
            
            Matcher matDataElementId = patDataElementId.matcher( dataElementCode );
            
            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {
                // -------------------------------------------------------------
                // Get data element ID of data element.
                // -------------------------------------------------------------
                
                int dataElementId = Integer.parseInt( matDataElementId.group( 1 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );                
                int optionComboId = Integer.parseInt( matDataElementId.group( 2 ) );               

                // -------------------------------------------------------------
                // Find type of data element
                // -------------------------------------------------------------
                
                String dataElementType = dataElement.getType();

                // -------------------------------------------------------------
                // Find existing value of data element in data set.
                // -------------------------------------------------------------
                
                String dataElementValue = "";
                
                if ( (dataElement instanceof CalculatedDataElement) )
                {
                    CalculatedDataElement cde = (CalculatedDataElement) dataElement;
                    if ( cde.isSaved() )
                    {
                        for ( DataValue dv : dataValues )
                        {
                            if ( dv.getDataElement().getId() == dataElementId && dv.getOptionCombo().getId() == optionComboId )
                            {
                                dataElementValue = dv.getValue();
                                break;
                            }
                        }
                    }
                    else
                    {
                        dataElementValue = String.valueOf( calculatedValueMap.get( cde ) );
                    }
                }
                else
                {
                    for ( DataValue dv : dataValues )
                    {
                        if ( dv.getDataElement().getId() == dataElementId && dv.getOptionCombo().getId() == optionComboId )
                        {                            
                            dataElementValue = dv.getValue();
                            break;
                        }
                    }
                }               

                // -------------------------------------------------------------
                // Insert value of data element in output code.
                // -------------------------------------------------------------
                
                if ( dataElement.getType().equals( "bool" ) )
                {                	
                    dataElementCode = dataElementCode.replace( "input", "select" );
                    dataElementCode = dataElementCode.replaceAll( "value=\".*?\"", "" );
                    dataElementCode = dataElementCode.replaceAll( "view=\".*?\"", "" );
                }
                else
                {
                    if ( dataElementCode.contains( "value=\"\"" ) )
                        dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"" + dataElementValue + "\"" );
                    else
                        dataElementCode += "value=\"" + dataElementValue + "\"";
                }

                // -------------------------------------------------------------
                // MIN-MAX Values
                // -------------------------------------------------------------
                
                MinMaxDataElement minMaxDataElement = minMaxMap.get( new Integer( dataElement.getId() ) );
                String minValue = "No Min";
                String maxValue = "No Max";
                
                if ( minMaxDataElement != null )
                {
                    minValue = String.valueOf( minMaxDataElement.getMin() );
                    maxValue = String.valueOf( minMaxDataElement.getMax() );
                }

                // -------------------------------------------------------------
                // Remove placeholder view attribute from input field.
                // -------------------------------------------------------------
                
                dataElementCode = dataElementCode.replaceAll( "view=\".*?\"", "" );

                // -------------------------------------------------------------
                // Insert Title Information - DataElement id,name,type,min,max
                // -------------------------------------------------------------
                
                if ( dataElementCode.contains( "title=\"\"" ) )
                {
                    dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- ID:" + dataElement.getId()
                        + " Name:" + dataElement.getShortName() + " Type:" + dataElement.getType() + " Min:" + minValue
                        + " Max:" + maxValue + " --\"" );
                }
                else
                {
                    dataElementCode += "title=\"-- ID:" + dataElement.getId() + " Name:" + dataElement.getShortName()
                        + " Type:" + dataElement.getType() + " Min:" + minValue + " Max:" + maxValue + " --\"";
                }

                // -------------------------------------------------------------
                // Append Javascript code and meta data (type/min/max) for
                // persisting to output code, and insert value and type for
                // fields.
                // -------------------------------------------------------------
                
                String appendCode = dataElementCode;

                if ( dataElement.getType().equalsIgnoreCase( "bool" ) )
                {                	
                    appendCode += jsCodeForCombos;
                    
                    appendCode += "<option value=\"\">" + i18n.getString( "no_value" ) + "</option>";
                    
                    if ( dataElementValue.equalsIgnoreCase( "true" ) )
                    {
                        appendCode += "<option value=\"true\" selected>" + i18n.getString( "yes" ) + "</option>";
                    }
                    else
                    {
                        appendCode += "<option value=\"true\">" + i18n.getString( "yes" ) + "</option>";
                    }

                    if ( dataElementValue.equalsIgnoreCase( "false" ) )
                    {
                        appendCode += "<option value=\"false\" selected>" + i18n.getString( "no" ) + "</option>";
                    }
                    else
                    {
                        appendCode += "<option value=\"false\">" + i18n.getString( "no" ) + "</option>";
                    }

                    appendCode += "</select>";
                }
                else
                {
                    appendCode += jsCodeForInputs;
                    
                    if ( dataElement.getType().equalsIgnoreCase( "int" ) )
                    {
                        appendCode += historyCode;
                    }

                    if ( (dataElement instanceof CalculatedDataElement) )
                    {
                        appendCode += calDataElementCode;
                    }

                    appendCode += " />";
                }

                appendCode += metaDataCode;
                appendCode = appendCode.replace( "$DATAELEMENTID", String.valueOf( dataElementId ) );
                appendCode = appendCode.replace( "$DATAELEMENTNAME", dataElement.getName() );
                appendCode = appendCode.replace( "$DATAELEMENTTYPE", dataElementType );
                appendCode = appendCode.replace( "$OPTIONCOMBOID", String.valueOf(optionComboId) );
                
                if ( minMaxDataElement == null )
                {
                    appendCode = appendCode.replace( "$MIN", minValue );
                    appendCode = appendCode.replace( "$MAX", maxValue );
                }
                else
                {
                    appendCode = appendCode.replace( "$MIN", String.valueOf( minMaxDataElement.getMin() ) );
                    appendCode = appendCode.replace( "$MAX", String.valueOf( minMaxDataElement.getMax() ) );
                }
                
                matDataElement.appendReplacement( sb, appendCode );
            }

            // Go to next data entry field
            result = matDataElement.find();
        }
        
        // Add remaining code (after the last match), and return formatted code.
        matDataElement.appendTail( sb );
        
        return sb.toString();
    }
}
