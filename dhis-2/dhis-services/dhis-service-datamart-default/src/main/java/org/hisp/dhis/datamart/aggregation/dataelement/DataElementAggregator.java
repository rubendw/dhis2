package org.hisp.dhis.datamart.aggregation.dataelement;

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

import org.hisp.dhis.dataelement.Operand;
import org.hisp.dhis.datamart.CrossTabDataValue;
import org.hisp.dhis.datamart.DataMartStore;
import org.hisp.dhis.datamart.aggregation.cache.AggregationCache;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitHierarchy;
import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: DataElementAggregator.java 5510 2008-07-30 16:30:27Z larshelg $
 */
public abstract class DataElementAggregator
{
    protected final String TRUE = "true";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    protected DataMartStore dataMartStore;
    
    public void setDataMartStore( DataMartStore dataMartStore )
    {
        this.dataMartStore = dataMartStore;
    }
    
    protected AggregationCache aggregationCache;
        
    public void setAggregationCache( AggregationCache aggregationCache )
    {
        this.aggregationCache = aggregationCache;
    }
    
    // -------------------------------------------------------------------------
    // DataElementAggregator
    // -------------------------------------------------------------------------

    public abstract Map<Operand, Double> getAggregatedValues( 
        Collection<Operand> operands, Period period, OrganisationUnit unit );
    
    protected abstract Collection<CrossTabDataValue> getCrossTabDataValues( 
        Collection<Operand> operands, Date startDate, Date endDate, int parentId, OrganisationUnitHierarchy hierarchy );
    
    protected abstract Map<Operand, Double[]> getAggregate( 
        Collection<CrossTabDataValue> crossTabValues, Date startDate, Date endDate, Date aggregationStartDate, Date aggregationEndDate );
}
