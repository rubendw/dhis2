package org.hisp.dhis.dataelement;

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
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.hierarchy.HierarchyViolationException;
import org.hisp.dhis.system.util.UUIdUtils;

/**
 * @author Kristian Nordal
 * @version $Id: DefaultDataElementService.java 5243 2008-05-25 10:18:58Z
 *          larshelg $
 */
public class DefaultDataElementService
    implements DataElementService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementStore dataElementStore;

    public void setDataElementStore( DataElementStore dataElementStore )
    {
        this.dataElementStore = dataElementStore;
    }

    // -------------------------------------------------------------------------
    // DataElement
    // -------------------------------------------------------------------------

    public int addDataElement( DataElement dataElement )
    {
        if ( dataElement.getUuid() == null )
        {
            dataElement.setUuid( UUIdUtils.getUUId() );
        }

        return dataElementStore.addDataElement( dataElement );
    }

    public void updateDataElement( DataElement dataElement )
    {
        dataElementStore.updateDataElement( dataElement );
    }

    public void deleteDataElement( DataElement dataElement )
        throws HierarchyViolationException
    {
        dataElementStore.deleteDataElement( dataElement );
    }

    public DataElement getDataElement( int id )
    {
        return dataElementStore.getDataElement( id );
    }

    public DataElement getDataElement( String uuid )
    {
        return dataElementStore.getDataElement( uuid );
    }

    public Collection<DataElement> getAllDataElements()
    {
        return dataElementStore.getAllDataElements();
    }
    
    public Collection<DataElement> getDataElements( Collection<Integer> identifiers )
    {
        Collection<DataElement> objects = new ArrayList<DataElement>();
        
        for ( Integer id : identifiers )
        {
            objects.add( getDataElement( id ) );
        }
        
        return objects;
    }

    public Collection<DataElement> getAggregateableDataElements()
    {
        return dataElementStore.getAggregateableDataElements();
    }

    public Collection<DataElement> getAllActiveDataElements()
    {
        return dataElementStore.getAllActiveDataElements();
    }

    public DataElement getDataElementByName( String name )
    {
        return dataElementStore.getDataElementByName( name );
    }

    public DataElement getDataElementByAlternativeName( String alternativeName )
    {
        return dataElementStore.getDataElementByAlternativeName( alternativeName );
    }

    public DataElement getDataElementByShortName( String shortName )
    {
        return dataElementStore.getDataElementByShortName( shortName );
    }

    public DataElement getDataElementByCode( String code )
    {
        return dataElementStore.getDataElementByCode( code );
    }

    public Collection<DataElement> getDataElementsByAggregationOperator( String aggregationOperator )
    {
        return dataElementStore.getDataElementsByAggregationOperator( aggregationOperator );
    }

    public Collection<DataElement> getDataElementsByType( String type )
    {
        return dataElementStore.getDataElementsByType( type );
    }
    
    // -------------------------------------------------------------------------
    // CalculatedDataElement
    // -------------------------------------------------------------------------

    public Collection<CalculatedDataElement> getAllCalculatedDataElements()
    {
        return dataElementStore.getAllCalculatedDataElements();
    }

    public CalculatedDataElement getCalculatedDataElementByDataElement( DataElement dataElement )
    {
        return dataElementStore.getCalculatedDataElementByDataElement( dataElement );
    }

    public Collection<CalculatedDataElement> getCalculatedDataElementsByDataElements(
        Collection<DataElement> dataElements )
    {
        return dataElementStore.getCalculatedDataElementsByDataElements( dataElements );
    }

    public Map<DataElement, Integer> getDataElementFactors( CalculatedDataElement calculatedDataElement )
    {
        Map<DataElement, Integer> factorMap = new HashMap<DataElement, Integer>();

        Pattern pattern = Pattern.compile( "\\[(\\d+)\\]\\s*\\*\\s*(\\d+)" );

        // ---------------------------------------------------------------------
        // In readable form: \[(\d+)\]\s*\*\s*(\d+)
        // Meaning any expression on the form "[id] * factor"
        // ---------------------------------------------------------------------

        Matcher matcher = pattern.matcher( calculatedDataElement.getExpression().getExpression() );

        while ( matcher.find() )
        {
            // -----------------------------------------------------------------
            // Key: Datelementid
            // Value: Factor
            // -----------------------------------------------------------------

            factorMap.put( getDataElement( Integer.parseInt( matcher.group( 1 ) ) ), Integer.parseInt( matcher
                .group( 2 ) ) );
        }

        return factorMap;
    }

    public Map<String, Integer> getOperandFactors( CalculatedDataElement calculatedDataElement )
    {
        Map<String, Integer> factorMap = new HashMap<String, Integer>();

        Pattern pattern = Pattern.compile( "\\[(\\d+\\.\\d+)\\]\\s*\\*\\s*(-?\\d+)" );

        // ---------------------------------------------------------------------
        // In readable form: \[(\d+)\]\s*\*\s*(\d+)
        // Meaning any expression on the form "[id] * factor"
        // ---------------------------------------------------------------------

        Matcher matcher = pattern.matcher( calculatedDataElement.getExpression().getExpression() );

        while ( matcher.find() )
        {
            // -----------------------------------------------------------------
            // Key: Datelementid.optioncomboid
            // Value: Factor
            // -----------------------------------------------------------------

            factorMap.put( matcher.group( 1 ), Integer.parseInt( matcher.group( 2 ) ) );
        }

        return factorMap;
    }

    public Collection<String> getOperandIds( CalculatedDataElement calculatedDataElement )
    {
        Collection<String> operands = new ArrayList<String>();

        Pattern pattern = Pattern.compile( "\\[(\\d+\\.\\d+)\\]" );

        // ---------------------------------------------------------------------
        // In readable form: \[(\d+)\]\s*\*\s*(\d+)
        // Meaning any expression on the form "[id] * factor"
        // ---------------------------------------------------------------------

        Matcher matcher = pattern.matcher( calculatedDataElement.getExpression().getExpression() );

        while ( matcher.find() )
        {
            // -----------------------------------------------------------------
            // Datelementid.optioncomboid
            // -----------------------------------------------------------------

            operands.add( matcher.group( 1 ) );
        }

        return operands;
    }

    // -------------------------------------------------------------------------
    // DataElementGroup
    // -------------------------------------------------------------------------

    public int addDataElementGroup( DataElementGroup dataElementGroup )
    {
        if ( dataElementGroup.getUuid() == null )
        {
            dataElementGroup.setUuid( UUIdUtils.getUUId() );
        }

        return dataElementStore.addDataElementGroup( dataElementGroup );
    }

    public void updateDataElementGroup( DataElementGroup dataElementGroup )
    {
        dataElementStore.updateDataElementGroup( dataElementGroup );
    }

    public void deleteDataElementGroup( DataElementGroup dataElementGroup )
    {
        dataElementStore.deleteDataElementGroup( dataElementGroup );
    }

    public DataElementGroup getDataElementGroup( int id )
    {
        return dataElementStore.getDataElementGroup( id );
    }

    public DataElementGroup getDataElementGroup( String uuid )
    {
        return dataElementStore.getDataElementGroup( uuid );
    }

    public Collection<DataElementGroup> getAllDataElementGroups()
    {
        return dataElementStore.getAllDataElementGroups();
    }

    public DataElementGroup getDataElementGroupByName( String name )
    {
        return dataElementStore.getDataElementGroupByName( name );
    }

    public Collection<DataElementGroup> getGroupsContainingDataElement( DataElement dataElement )
    {
        Collection<DataElementGroup> groups = getAllDataElementGroups();

        Iterator<DataElementGroup> iterator = groups.iterator();

        while ( iterator.hasNext() )
        {
            if ( !iterator.next().getMembers().contains( dataElement ) )
            {
                iterator.remove();
            }
        }

        return groups;
    }
}
