package org.hisp.dhis.datavalue;

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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Kristian Nordal
 * @version $Id: DefaultDataValueService.java 5715 2008-09-17 14:05:28Z larshelg $
 */
public class DefaultDataValueService
    implements DataValueService
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataValueStore dataValueStore;

    public void setDataValueStore( DataValueStore dataValueStore )
    {
        this.dataValueStore = dataValueStore;
    }

    // -------------------------------------------------------------------------
    // Basic DataValue
    // -------------------------------------------------------------------------

    public void addDataValue( DataValue dataValue )
    {
        if ( !( dataValue.getValue() == null && dataValue.getComment() == null ) )
        {
            dataValueStore.addDataValue( dataValue );
        }
    }

    public void updateDataValue( DataValue dataValue )
    {
        if ( dataValue.getValue() == null && dataValue.getComment() == null )
        {
            dataValueStore.deleteDataValue( dataValue );
        }
        else
        {
            dataValueStore.updateDataValue( dataValue );
        }
    }

    public void deleteDataValue( DataValue dataValue )
    {
        dataValueStore.deleteDataValue( dataValue );
    }
    
    public int deleteDataValuesBySource( Source source )
    {
        return dataValueStore.deleteDataValuesBySource( source );
    }
    
    public int deleteDataValuesByDataElement( DataElement dataElement )
    {
        return dataValueStore.deleteDataValuesByDataElement( dataElement );
    }

    public DataValue getDataValue( Source source, DataElement dataElement, Period period )
    {
        return dataValueStore.getDataValue( source, dataElement, period );
    }
    
    public DataValue getDataValue( Source source, DataElement dataElement, Period period, DataElementCategoryOptionCombo optionCombo )
    {
        return dataValueStore.getDataValue( source, dataElement, period, optionCombo );
    }

    // -------------------------------------------------------------------------
    // Collections of DataValues
    // -------------------------------------------------------------------------

    public Collection<DataValue> getAllDataValues()
    {
        return dataValueStore.getAllDataValues();
    }
    
    public Collection<DataValue> getDataValues( Source source, Period period )
    {
        return dataValueStore.getDataValues( source, period );
    }

    public Collection<DataValue> getDataValues( Source source, DataElement dataElement )
    {
        return dataValueStore.getDataValues( source, dataElement );
    }

    public Collection<DataValue> getDataValues( Collection<Source> sources, DataElement dataElement )
    {
        return dataValueStore.getDataValues( sources, dataElement );
    }

    public Collection<DataValue> getDataValues( Source source, Period period, Collection<DataElement> dataElements )
    {
        return dataValueStore.getDataValues( source, period, dataElements );
    }
    
    public Collection<DataValue> getDataValues( Source source, Period period, Collection<DataElement> dataElements, Collection<DataElementCategoryOptionCombo> optionCombos )
    {
        return dataValueStore.getDataValues( source, period, dataElements, optionCombos );
    }
    
    public Collection<DataValue> getDataValues( DataElement dataElement, Collection<Period> periods, Collection<? extends Source> sources )
    {
        return dataValueStore.getDataValues( dataElement, periods, sources );
    }
    
    public Collection<DataValue> getDataValues( DataElement dataElement, DataElementCategoryOptionCombo optionCombo, 
        Collection<Period> periods, Collection<? extends Source> sources )
    {
        return dataValueStore.getDataValues( dataElement, optionCombo, periods, sources );
    }
    
    public Collection<DataValue> getDataValues( Collection<DataElement> dataElements, Collection<Period> periods, 
        Collection<? extends Source> sources, int firstResult, int maxResults )
    {
        return dataValueStore.getDataValues( dataElements, periods, sources, firstResult, maxResults );
    }
    
    public Collection<DataValue> getDataValues( Collection<DataElementCategoryOptionCombo> optionCombos )
    {
    	return dataValueStore.getDataValues( optionCombos );    	
    }
    
    public Collection<DataValue> getDataValues( DataElement dataElement )
    {
    	return dataValueStore.getDataValues( dataElement );
    }
}
