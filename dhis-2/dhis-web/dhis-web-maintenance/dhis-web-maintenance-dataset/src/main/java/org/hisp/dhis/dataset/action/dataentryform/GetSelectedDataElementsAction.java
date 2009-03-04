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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.options.displayproperty.DisplayPropertyHandler;

import com.opensymphony.xwork.Action;

/**
 * @author Bharath
 * @version $Id$
 */
public class GetSelectedDataElementsAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

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

    private Comparator<DataElement> dataElementComparator;

    public void setDataElementComparator( Comparator<DataElement> dataElementComparator )
    {
        this.dataElementComparator = dataElementComparator;
    }

    private DisplayPropertyHandler displayPropertyHandler;

    public void setDisplayPropertyHandler( DisplayPropertyHandler displayPropertyHandler )
    {
        this.displayPropertyHandler = displayPropertyHandler;
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    private int dataSetId;

    public void setDataSetId( int dataSetId )
    {
        this.dataSetId = dataSetId;
    }

    private String designCode;

    public void setDesignCode( String designCode )
    {
        this.designCode = designCode;
    }

    private List<DataElement> dataElementList;

    public List<DataElement> getDataElementList()
    {
        return dataElementList;
    }

    private List<DataElement> dataElements;

    // -------------------------------------------------------------------------
    // Execute
    // -------------------------------------------------------------------------
    
    public String execute()
        throws Exception
    {
        //System.out.println( "HTML CODE in GetSelectedDataElementsAction\n-----------------------\n" + designCode );
        /* Get data elements belonging to dataset */
        DataSet dataSet = dataSetService.getDataSet( dataSetId );

        dataElements = new ArrayList<DataElement>( dataSet.getDataElements() );

        Collections.sort( dataElements, dataElementComparator );

        // List<DataElement> selectedDataElementList =
        // getSelectedDataElementList();
        dataElementList = new ArrayList<DataElement>();

        /*
         * Additional code for Multi-Dimensional DataElement....need to be
         * remove when using for normal dataelements
         */
        dataElementList = dataElements;

        /*
         * Code commented for Multidimentsional DataElement...need to be remove
         * comments for normal ones
         */
        /*
         * Iterator<DataElement> iterator = dataElements.iterator(); while (
         * iterator.hasNext() ) { DataElement de = (DataElement)
         * iterator.next(); if ( selectedDataElementList.contains( de ) ) { }
         * else { dataElementList.add( de ); } }
         */
        Collections.sort( dataElementList, dataElementComparator );
        dataElementList = displayPropertyHandler.handleDataElements( dataElementList );

        return SUCCESS;
    }

    private List<DataElement> getSelectedDataElementList()
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
        return cdeFormMembers;

    }
}
