package org.hisp.dhis.period;

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

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.source.Source;

/**
 * Defines the functionality for persisting Periods and PeriodTypes.
 * 
 * @author Torgeir Lorange Ostby
 * @version $Id: PeriodStore.java 5472 2008-07-01 14:35:35Z larshelg $
 */
public interface PeriodStore
{
    String ID = PeriodStore.class.getName();

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    /**
     * Adds a Period.
     * 
     * @param period the Period to add.
     * @return a generated unique id of the added Period.
     */
    int addPeriod( Period period );

    /**
     * Deletes a Period.
     * 
     * @param period the Period to delete.
     */
    void deletePeriod( Period period );

    /**
     * Returns a Period.
     * 
     * @param id the id of the Period to return.
     * @return the Period with the given id, or null if no match.
     */
    Period getPeriod( int id );

    /**
     * Returns a Period.
     * 
     * @param startDate the start date of the Period.
     * @param endDate the end date of the Period.
     * @param periodType the PeriodType of the Period
     * @return the Period matching the dates and periodtype, or null if no match.
     */
    Period getPeriod( Date startDate, Date endDate, PeriodType periodType );

    /**
     * Returns all persisted Periods.
     * 
     * @return all persisted Periods.
     */
    Collection<Period> getAllPeriods();

    /**
     * Returns all Periods with start date after or equal the specified start
     * date and end date before or equal the specified end date.
     * 
     * @param startDate the ultimate start date.
     * @param endDate the ultimate end date.
     * @return a collection of all Periods with start date after or equal the
     *         specified start date and end date before or equal the specified
     *         end date, or an empty collection if no Periods match.
     */
    Collection<Period> getPeriodsBetweenDates( Date startDate, Date endDate );

    /**
     * Returns all intersecting Periods between the startDate and endDate based on PeriodType
     * For example if the startDate is 2007-05-01 and endDate is 2007-08-01 and periodType is Quartely
     * then it retuns the periods for Q2,Q3
     *  
     * @param periodType is the ultimate period type
     * @param startDate is intercepting startDate
     * @param endDate is intercepting endDate
     * @return
     */
    Collection<Period> getIntersectingPeriodsByPeriodType(PeriodType periodType, Date startDate, Date endDate);
    
    /**
     * Returns Periods where at least one its days is between the given start date and end date.
     * 
     * @param startDate the start date.
     * @param endDate the end date.
     * @return Periods where at least one its days is between the given start date and end date.
     */
    Collection<Period> getIntersectingPeriods( Date startDate, Date endDate );
    
    /**
     * Returns all Periods with a given PeriodType.
     * 
     * @param periodType the PeriodType of the Periods to return.
     * @return all Periods with the given PeriodType, or an empty collection if
     *         no Periods match.
     */
    Collection<Period> getPeriodsByPeriodType( PeriodType periodType );
    
    /**
     * Returns all intersecting Periods for the given Period which have assosiated DataValues for
     * the given collection of DataElements and Sources.
     * 
     * @param period the Period.
     * @param dataElements the collection of DataElements.
     * @param sources the collection of Sources.
     * @return all intersecting Periods for the given Period which have assosiated DataValues for
     *         the given collection of DataElements and Sources.
     */
    Collection<Period> getPeriods( Period period, Collection<DataElement> dataElements, Collection<? extends Source> sources );
    
    /**
     * Deletes all Periods which do not have any associated DataValues.
     * 
     * @return number of deleted Periods.
     */
    int prunePeriods();

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    /**
     * Adds a PeriodType.
     * 
     * @param periodType the PeriodType to add.
     * @return a generated unique id of the added PeriodType.
     */
    int addPeriodType( PeriodType periodType );

    /**
     * Deletes a PeriodType.
     * 
     * @param periodType the PeriodType to delete.
     */
    void deletePeriodType( PeriodType periodType );

    /**
     * Returns a PeriodType.
     * 
     * @param id the id of the PeriodType to return.
     * @return the PeriodType with the given id, or null if no match.
     */
    PeriodType getPeriodType( int id );

    /**
     * Returns the persisted instance of a given PeriodType.
     * 
     * @param periodType the PeriodType class of the instance to return.
     * @return
     */
    PeriodType getPeriodType( Class<? extends PeriodType> periodType );

    /**
     * Returns all PeriodTypes.
     * 
     * @return a collection of all PeriodTypes, or an empty collection if there
     *         are no PeriodTypes.
     */
    Collection<PeriodType> getAllPeriodTypes();
}
