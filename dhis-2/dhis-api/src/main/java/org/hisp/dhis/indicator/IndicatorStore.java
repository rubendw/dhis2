package org.hisp.dhis.indicator;

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

/**
 * @author Lars Helge Overland
 * @version $Id: IndicatorStore.java 3305 2007-05-14 18:55:52Z larshelg $
 */
public interface IndicatorStore
{
    String ID = IndicatorStore.class.getName();

    // -------------------------------------------------------------------------
    // Indicator
    // -------------------------------------------------------------------------

    int addIndicator( Indicator indicator );

    void updateIndicator( Indicator indicator );

    void deleteIndicator( Indicator indicator );

    Indicator getIndicator( int id );
    
    Indicator getIndicator( String uuid );
    
    Collection<Indicator> getAllIndicators();
    
    Indicator getIndicatorByName( String name );

    Indicator getIndicatorByShortName( String shortName );

    Indicator getIndicatorByAlternativeName( String alternativeName );

    Indicator getIndicatorByCode( String code );

    // -------------------------------------------------------------------------
    // IndicatorType
    // -------------------------------------------------------------------------

    int addIndicatorType( IndicatorType indicatorType );

    void updateIndicatorType( IndicatorType indicatorType );

    void deleteIndicatorType( IndicatorType indicatorType );

    IndicatorType getIndicatorType( int id );

    Collection<IndicatorType> getAllIndicatorTypes();

    IndicatorType getIndicatorTypeByName( String name );

    // -------------------------------------------------------------------------
    // IndicatorGroup
    // -------------------------------------------------------------------------

    int addIndicatorGroup( IndicatorGroup indicatorGroup );

    void updateIndicatorGroup( IndicatorGroup indicatorGroup );

    void deleteIndicatorGroup( IndicatorGroup indicatorGroup );

    IndicatorGroup getIndicatorGroup( int id );
    
    IndicatorGroup getIndicatorGroup( String uuid );

    Collection<IndicatorGroup> getAllIndicatorGroups();

    IndicatorGroup getIndicatorGroupByName( String name );
}
