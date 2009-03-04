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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataEntryForm;
import org.hisp.dhis.dataset.DataEntryFormService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;

public class ValidateDataEntryFormAction
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

    // -------------------------------------------------------------------------
    // I18n
    // -------------------------------------------------------------------------

    private I18n i18n;

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------
    
    private String name;

    public void setName( String name )
    {
        this.name = name;
    }

    private Integer dataEntryFormId;

    public void setDataEntryFormId( Integer dataEntryFormId )
    {
        this.dataEntryFormId = dataEntryFormId;
    }

    private Integer dataSetId;

    public void setDataSetId( Integer dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    private String designCode;

    public void setDesignCode( String designCode )
    {
        this.designCode = designCode;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }

    // -------------------------------------------------------------------------
    // Execution
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        // System.out.println( "HTML Code : " );
        // System.out.println( designCode );
        // System.out.println( "--------------------------------------------" );
        if ( name == null )
        {
            message = i18n.getString( "specify_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "specify_name" );

                return INPUT;
            }

            DataEntryForm match = dataEntryFormService.getDataEntryFormByName( name );

            if ( match != null && (dataEntryFormId == null || match.getId() != dataEntryFormId) )
            {
                message = i18n.getString( "duplicate_names" );

                return INPUT;
            }
        }

        /*
         * if ( !checkDataElementCount() ) { message = i18n.getString(
         * "dataelement_count_mismatch" ); return "mismatch"; }
         */
        return SUCCESS;
    }

    private boolean checkDataElementCount()
    {
        DataSet dataSet = dataSetService.getDataSet( dataSetId );
        int dataSetMembersCount = dataSet.getDataElements().size();

        int cdeFormMembersCount = checkDataElementCountInputs();
        // cdeFormMembersCount += checkDataElementCountCobmos();

        if ( cdeFormMembersCount == dataSetMembersCount )
            return true;

        return false;
    }

    private int checkDataElementCountInputs()
    {
        // Buffer to contain the final result.
        StringBuffer sb = new StringBuffer();

        List<DataElement> cdeFormMembers = new ArrayList<DataElement>();

        // Pattern to match data elements in the HTML code.
        Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>" );
        Matcher matDataElement = patDataElement.matcher( designCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields.
        // ---------------------------------------------------------------------
        boolean result = matDataElement.find();
        while ( result )
        {
            // Get input HTML code (HTML input field code).
            String dataElementCode = matDataElement.group( 1 );

            // Pattern to extract data element ID from data element field
            Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].value" );
            Matcher matDataElementId = patDataElementId.matcher( dataElementCode );
            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {
                // ---------------------------------------------------------------------
                // Get data element ID of data element.
                // ---------------------------------------------------------------------
                String strDataElementId = matDataElementId.group( 1 ).replace( "].value", "" );

                int dataElementId = Integer.parseInt( strDataElementId );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );
                if ( cdeFormMembers.contains( dataElement ) )
                {

                }
                else
                {
                    cdeFormMembers.add( dataElement );
                }
                // ---------------------------------------------------------------------
                // Appends dataElementCode
                // ---------------------------------------------------------------------
                String appendCode = dataElementCode;
                appendCode += "/>";
                matDataElement.appendReplacement( sb, appendCode );
            }
            else
            {
                // Pattern to extract data element ID from data element field
                Pattern patDataElementId1 = Pattern.compile( "value\\[(.*)\\].boolean" );
                Matcher matDataElementId1 = patDataElementId1.matcher( dataElementCode );
                if ( matDataElementId1.find() && matDataElementId1.groupCount() > 0 )
                {
                    // ---------------------------------------------------------------------
                    // Get data element ID of data element.
                    // ---------------------------------------------------------------------
                    String strDataElementId = matDataElementId1.group( 1 ).replace( "].boolean", "" );

                    int dataElementId = Integer.parseInt( strDataElementId );
                    DataElement dataElement = dataElementService.getDataElement( dataElementId );
                    if ( cdeFormMembers.contains( dataElement ) )
                    {

                    }
                    else
                    {
                        cdeFormMembers.add( dataElement );
                    }
                    // ---------------------------------------------------------------------
                    // Appends dataElementCode
                    // ---------------------------------------------------------------------
                    String appendCode = dataElementCode;
                    appendCode += "/>";
                    matDataElement.appendReplacement( sb, appendCode );
                }
            }

            // Go to next data entry field
            result = matDataElement.find();
        }
        //System.out.println( "DataElementCount- TextBox : " + cdeFormMembers.size() );
        return cdeFormMembers.size();
    }

    private int checkDataElementCountCobmos()
    {
        // Buffer to contain the final result.
        StringBuffer sb = new StringBuffer();

        List<DataElement> cdeFormMembers = new ArrayList<DataElement>();

        // Pattern to match data elements in the HTML code.
        Pattern patDataElement = Pattern.compile( "(<select.*?)>.*</select>" );
        Matcher matDataElement = patDataElement.matcher( designCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields.
        // ---------------------------------------------------------------------
        boolean result = matDataElement.find();
        while ( result )
        {
            // Get input HTML code (HTML input field code).
            String dataElementCode = matDataElement.group( 1 );

            // Pattern to extract data element ID from data element field
            Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].boolean" );
            Matcher matDataElementId = patDataElementId.matcher( dataElementCode );
            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {
                // ---------------------------------------------------------------------
                // Get data element ID of data element.
                // ---------------------------------------------------------------------
                String strDataElementId = matDataElementId.group( 1 ).replace( "].boolean", "" );

                int dataElementId = Integer.parseInt( strDataElementId );
                DataElement dataElement = dataElementService.getDataElement( dataElementId );
                if ( cdeFormMembers.contains( dataElement ) )
                {

                }
                else
                {
                    cdeFormMembers.add( dataElement );
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

        //System.out.println( "DataElementCount- Combo : " + cdeFormMembers.size() );
        return cdeFormMembers.size();

    }
}
