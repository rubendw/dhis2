package org.hisp.dhis.importexport.converter;

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

import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.reporttable.ReportTableStore;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class AbstractReportTableConverter
    extends AbstractConverter<ReportTable>
{
    protected ReportTableStore reportTableStore;

    // -------------------------------------------------------------------------
    // Overridden methods
    // -------------------------------------------------------------------------

    protected void importUnique( ReportTable object )
    {
        batchHandler.addObject( object );       
    }

    protected void importMatching( ReportTable object, ReportTable match )
    {
        match.setName( object.getName() );
        match.setTableName( object.getTableName() );
        match.setIndicators( match.getIndicators() );
        match.setPeriods( match.getPeriods() );
        match.setUnits( match.getUnits() );
        match.setDoIndicators( match.isDoIndicators() );
        match.setDoPeriods( match.isDoPeriods() );
        match.setDoUnits( match.isDoUnits() );
        match.getRelatives().setReportingMonth( object.getRelatives().isReportingMonth() );
        match.getRelatives().setLast3Months( object.getRelatives().isLast3Months() );
        match.getRelatives().setLast6Months( object.getRelatives().isLast6Months() );
        match.getRelatives().setLast9Months( object.getRelatives().isLast9Months() );
        match.getRelatives().setLast12Months( object.getRelatives().isLast12Months() );
        match.getRelatives().setSoFarThisYear( object.getRelatives().isSoFarThisYear() );
                            
        reportTableStore.saveReportTable( match );
    }
    
    protected ReportTable getMatching( ReportTable object )
    {
        return reportTableStore.getReportTableByName( object.getName() );
    }
    
    protected boolean isIdentical( ReportTable object, ReportTable existing )
    {
        if ( !object.getName().equals( existing.getName() ) )
        {
            return false;
        }
        if ( !object.getTableName().equals( existing.getTableName() ) )
        {
            return false;
        }
        if ( object.isDoIndicators() != existing.isDoIndicators() )
        {
            return false;
        }
        if ( object.isDoPeriods() != existing.isDoPeriods() )
        {
            return false;
        }
        if ( object.isDoUnits() != existing.isDoUnits() )
        {
            return false;
        }
        
        return true;
    }
}
