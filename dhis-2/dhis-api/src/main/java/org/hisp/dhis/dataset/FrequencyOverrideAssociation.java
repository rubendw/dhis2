package org.hisp.dhis.dataset;

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

import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: FrequencyOverrideAssociation.java 3331 2007-06-01 07:29:02Z torgeilo $
 */
public class FrequencyOverrideAssociation
    implements Serializable
{
    /**
     * Part of the association's composite ID.
     */
    private DataSet dataSet;

    /**
     * Part of the association's composite ID.
     */
    private Source source;

    /**
     * The override
     */
    private PeriodType periodType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public FrequencyOverrideAssociation()
    {
    }

    public FrequencyOverrideAssociation( DataSet dataSet, Source source, PeriodType periodType )
    {
        this.dataSet = dataSet;
        this.source = source;
        this.periodType = periodType;
    }

    // -------------------------------------------------------------------------
    // HashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + dataSet.hashCode();
        result = prime * result + source.hashCode();

        return result;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof FrequencyOverrideAssociation) )
        {
            return false;
        }

        final FrequencyOverrideAssociation other = (FrequencyOverrideAssociation) o;

        return dataSet.equals( other.getDataSet() ) && source.equals( other.getSource() );
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void setDataSet( DataSet dataSet )
    {
        this.dataSet = dataSet;
    }

    public Source getSource()
    {
        return source;
    }

    public void setSource( Source source )
    {
        this.source = source;
    }

    public PeriodType getPeriodType()
    {
        return periodType;
    }

    public void setPeriodType( PeriodType periodType )
    {
        this.periodType = periodType;
    }
}
