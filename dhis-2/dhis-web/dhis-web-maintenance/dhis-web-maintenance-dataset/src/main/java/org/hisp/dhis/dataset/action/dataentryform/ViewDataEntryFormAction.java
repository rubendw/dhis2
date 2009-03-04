package org.hisp.dhis.dataset.action.dataentryform;

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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOption;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataEntryForm;
import org.hisp.dhis.dataset.DataEntryFormService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;

import com.opensymphony.xwork.Action;

public class ViewDataEntryFormAction
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

    private DataSetService dataSetService;

    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    
    private int dataSetId;

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    private DataEntryForm dataEntryForm;

    public DataEntryForm getDataEntryForm()
    {
        return dataEntryForm;
    }

    private DataSet dataSet;

    public DataSet getDataSet()
    {
        return dataSet;
    }

    private String status;

    public String getStatus()
    {
        return status;
    }

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------
    public String execute()
        throws Exception
    {
        dataSet = dataSetService.getDataSet( dataSetId );

        dataEntryForm = dataEntryFormService.getDataEntryFormByDataSet( dataSet );

        if ( dataEntryForm == null )
        {
            status = "ADD";
        }
        else
        {
            status = "EDIT";
            dataEntryForm.setHtmlCode( prepareDataEntryFormCode( dataEntryForm.getHtmlCode() ) );
        }

        return SUCCESS;
    }

    /**
     * Prepares the data entry form code by injecting the dataElement name for
     * each entry field
     * 
     * 
     * @param dataEntryFormCode HTML code of the data entry form (as persisted
     *        in the database)
     * @return HTML code for the data entry form injected with dataelement name
     */
    private String prepareDataEntryFormCode( String dataEntryFormCode )
    {
        String preparedCode = dataEntryFormCode;

        preparedCode = prepareDataEntryFormInputs( preparedCode );
        preparedCode = prepareDataEntryFormCombos( preparedCode );

        return preparedCode;
    }

    private String prepareDataEntryFormInputs( String preparedCode )
    {
        // Buffer to contain the final result.
        StringBuffer sb = new StringBuffer();

        // Pattern to match data elements in the HTML code.
        Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>" );
        Matcher matDataElement = patDataElement.matcher( preparedCode );

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

            Pattern patViewBy = Pattern.compile( "view=\"@@(.*)@@\"" );
            Matcher matViewBy = patViewBy.matcher( dataElementCode );

            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {
                // ---------------------------------------------------------------------
                // Get data element id,name, optionCombo id,name of data
                // element.
                // ---------------------------------------------------------------------
                int dataElementId = Integer.parseInt( matDataElementId.group( 1 ) );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                int optionComboId = Integer.parseInt( matDataElementId.group( 2 ) );
                DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService
                    .getDataElementCategoryOptionCombo( optionComboId );
                String optionComboName = "";

                if ( optionCombo != null )
                {
                    List<DataElementCategoryOption> categoryOptions = new ArrayList<DataElementCategoryOption>(
                        optionCombo.getCategoryOptions() );
                    Iterator<DataElementCategoryOption> categoryOptionsIterator = categoryOptions.iterator();
                    while ( categoryOptionsIterator.hasNext() )
                    {
                        DataElementCategoryOption categoryOption = categoryOptionsIterator.next();
                        
                        optionComboName += categoryOption.getName() + " ";
                    }
                }
                // ---------------------------------------------------------------------
                // Insert name of data element in output code.
                // ---------------------------------------------------------------------
                String dispVal = "No Such DataElement Exists";
                if ( dataElement != null )
                {
                    dispVal = dataElement.getShortName();

                    if ( matViewBy.find() && matViewBy.groupCount() > 0 )
                    {
                        String viewByVal = matViewBy.group( 1 );
                        if ( viewByVal.equalsIgnoreCase( "deid" ) )
                            dispVal = String.valueOf( dataElement.getId() );
                        else if ( viewByVal.equalsIgnoreCase( "dename" ) )
                            dispVal = dataElement.getName();
                    }
                    dispVal += " - " + optionComboName;

                    if ( dataElementCode.contains( "value=\"\"" ) )
                        dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"[ " + dispVal + " ]\"" );
                    else
                        dataElementCode += " value=\"[ " + dispVal + " ]\"";

                    if ( dataElementCode.contains( "title=\"\"" ) )
                        dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- " + dataElement.getId()
                            + ". " + dataElement.getName() + " " + optionComboId + ". " + optionComboName + " ("
                            + dataElement.getType() + ") --\"" );
                    else
                        dataElementCode += " title=\"-- " + dataElement.getId() + ". " + dataElement.getName() + " "
                            + optionComboId + ". " + optionComboName + " (" + dataElement.getType() + ") --\"";
                }
                else
                {
                    if ( dataElementCode.contains( "value=\"\"" ) )
                        dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"[ " + dispVal + " ]\"" );
                    else
                        dataElementCode += " value=\"[ " + dispVal + " ]\"";

                    if ( dataElementCode.contains( "title=\"\"" ) )
                        dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- " + dispVal + " --\"" );
                    else
                        dataElementCode += " title=\"-- " + dispVal + " --\"";
                }

                // ---------------------------------------------------------------------
                // Appends dataElementCode
                // ---------------------------------------------------------------------
                String appendCode = dataElementCode;
                appendCode += "/>";
                matDataElement.appendReplacement( sb, appendCode );
            }

            // Go to next data entry field
            result = matDataElement.find();
        }

        // Add remaining code (after the last match), and return formatted code.
        matDataElement.appendTail( sb );
        return sb.toString();
    }

    
    
    private String prepareDataEntryFormCombos( String preparedCode )
    {
            // Buffer to contain the final result.
            StringBuffer sb = new StringBuffer();

            // Pattern to match data elements in the HTML code.
            Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>" );
            Matcher matDataElement = patDataElement.matcher( preparedCode );

            // ---------------------------------------------------------------------
            // Iterate through all matching data element fields.
            // ---------------------------------------------------------------------
            boolean result = matDataElement.find();
            while ( result )
            {
                // Get input HTML code (HTML input field code).
                String dataElementCode = matDataElement.group( 1 );

                // Pattern to extract data element ID from data element field
                Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].value:value\\[(.*)\\].boolean" );
                Matcher matDataElementId = patDataElementId.matcher( dataElementCode );

                Pattern patViewBy = Pattern.compile( "view=\"@@(.*)@@\"" );
                Matcher matViewBy = patViewBy.matcher( dataElementCode );

                if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
                {
                    // ---------------------------------------------------------------------
                    // Get data element id,name, optionCombo id,name of data
                    // element.
                    // ---------------------------------------------------------------------
                    int dataElementId = Integer.parseInt( matDataElementId.group( 1 ) );
                    DataElement dataElement = dataElementService.getDataElement( dataElementId );

                    int optionComboId = Integer.parseInt( matDataElementId.group( 2 ) );
                    DataElementCategoryOptionCombo optionCombo = dataElementCategoryOptionComboService
                        .getDataElementCategoryOptionCombo( optionComboId );
                    String optionComboName = "";

                    if ( optionCombo != null )
                    {
                        List<DataElementCategoryOption> categoryOptions = new ArrayList<DataElementCategoryOption>(
                            optionCombo.getCategoryOptions() );
                        Iterator<DataElementCategoryOption> categoryOptionsIterator = categoryOptions.iterator();
                        while ( categoryOptionsIterator.hasNext() )
                        {
                            DataElementCategoryOption categoryOption = categoryOptionsIterator.next();
                            
                            optionComboName += categoryOption.getName() + " ";
                        }
                    }
                    // ---------------------------------------------------------------------
                    // Insert name of data element in output code.
                    // ---------------------------------------------------------------------
                    String dispVal = "No Such DataElement Exists";
                    if ( dataElement != null )
                    {
                        dispVal = dataElement.getShortName();

                        if ( matViewBy.find() && matViewBy.groupCount() > 0 )
                        {
                            String viewByVal = matViewBy.group( 1 );
                            if ( viewByVal.equalsIgnoreCase( "deid" ) )
                                dispVal = String.valueOf( dataElement.getId() );
                            else if ( viewByVal.equalsIgnoreCase( "dename" ) )
                                dispVal = dataElement.getName();
                        }
                        dispVal += " - " + optionComboName;

                        if ( dataElementCode.contains( "value=\"\"" ) )
                            dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"[ " + dispVal + " ]\"" );
                        else
                            dataElementCode += " value=\"[ " + dispVal + " ]\"";

                        if ( dataElementCode.contains( "title=\"\"" ) )
                            dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- " + dataElement.getId()
                                + ". " + dataElement.getName() + " " + optionComboId + ". " + optionComboName + " ("
                                + dataElement.getType() + ") --\"" );
                        else
                            dataElementCode += " title=\"-- " + dataElement.getId() + ". " + dataElement.getName() + " "
                                + optionComboId + ". " + optionComboName + " (" + dataElement.getType() + ") --\"";
                    }
                    else
                    {
                        if ( dataElementCode.contains( "value=\"\"" ) )
                            dataElementCode = dataElementCode.replace( "value=\"\"", "value=\"[ " + dispVal + " ]\"" );
                        else
                            dataElementCode += " value=\"[ " + dispVal + " ]\"";

                        if ( dataElementCode.contains( "title=\"\"" ) )
                            dataElementCode = dataElementCode.replace( "title=\"\"", "title=\"-- " + dispVal + " --\"" );
                        else
                            dataElementCode += " title=\"-- " + dispVal + " --\"";
                    }

                    // ---------------------------------------------------------------------
                    // Appends dataElementCode
                    // ---------------------------------------------------------------------
                    String appendCode = dataElementCode;
                    appendCode += "/>";
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
