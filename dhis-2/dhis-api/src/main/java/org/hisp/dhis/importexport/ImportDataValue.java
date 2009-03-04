package org.hisp.dhis.importexport;

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

import java.io.Serializable;
import java.util.Date;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ImportDataValue
    implements Serializable
{
    private int dataElementId;
    
    private int categoryOptionComboId;
    
    private int periodId;
    
    private int sourceId;
    
    private String value;
    
    private String storedBy;
    
    private Date timestamp;
    
    private String comment;
    
    private String status;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ImportDataValue()
    {   
    }
    
    public ImportDataValue( DataValue dataValue, ImportObjectStatus status )
    {
        setDataValue( dataValue, status );
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void setDataValue( DataValue dataValue, ImportObjectStatus status )
    {   
        this.dataElementId = dataValue.getDataElement().getId();
        this.categoryOptionComboId = dataValue.getOptionCombo().getId();
        this.periodId = dataValue.getPeriod().getId();
        this.sourceId = dataValue.getSource().getId();
        this.value = dataValue.getValue();
        this.storedBy = dataValue.getStoredBy();
        this.timestamp = dataValue.getTimestamp();
        this.comment = dataValue.getComment();
        this.status = status.name();
    }
    
    public DataValue getDataValue()
    {
        DataValue value = new DataValue();        
        
        DataElement element = new DataElement();
        value.setDataElement( element );
        
        DataElementCategoryOptionCombo optionCombo = new DataElementCategoryOptionCombo();
        value.setOptionCombo( optionCombo );
        
        Period period = new Period();
        value.setPeriod( period );
        
        Source source = new OrganisationUnit();
        value.setSource( source );
        
        value.getDataElement().setId( this.dataElementId );
        value.getOptionCombo().setId( this.categoryOptionComboId );
        value.getPeriod().setId( this.periodId );
        value.getSource().setId( this.sourceId );
        value.setValue( this.value );
        value.setStoredBy( this.storedBy );
        value.setTimestamp( this.timestamp );
        value.setComment( this.comment );
        
        return value;
    }
    
    // -------------------------------------------------------------------------
    // Equals & hashCode
    // -------------------------------------------------------------------------

    public int hashCode()
    {
        final int PRIME = 31;
        
        int result = 1;
        
        result = PRIME * result + dataElementId;
        
        result = PRIME * result + periodId;
        
        result = PRIME * result + sourceId;
        
        return result;
    }

    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }
        
        if ( object == null )
        {
            return false;
        }
        
        if ( !( object instanceof ImportDataValue ) )
        {
            return false;
        }
        
        final ImportDataValue other = (ImportDataValue) object;

        return dataElementId == other.getDataElementId() && 
            periodId == other.getPeriodId() && sourceId == other.getSourceId();
    }

    // -------------------------------------------------------------------------
    // Getters & setters
    // -------------------------------------------------------------------------
    
    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getCategoryOptionComboId()
    {
        return categoryOptionComboId;
    }

    public void setCategoryOptionComboId( int categoryOptionComboId )
    {
        this.categoryOptionComboId = categoryOptionComboId;
    }

    public int getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId( int periodId )
    {
        this.periodId = periodId;
    }

    public int getSourceId()
    {
        return sourceId;
    }

    public void setSourceId( int sourceId )
    {
        this.sourceId = sourceId;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }
    
    public String getStoredBy()
    {
        return storedBy;
    }

    public void setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( Date timeStamp )
    {
        this.timestamp = timeStamp;
    }
    
    public String getComment()
    {
        return comment;
    }

    public void setComment( String comment )
    {
        this.comment = comment;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus( String status )
    {
        this.status = status;
    }
}
