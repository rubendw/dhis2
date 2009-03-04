package org.hisp.dhis.importexport.dhis14.util;

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

import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.period.Period;

/**
 * @author Lars Helge Overland
 * @version $Id: OnChangePeriodUtil.java 4691 2008-03-10 17:52:00Z larshelg $
 */
public class OnChangePeriodUtil
{
    private static ThreadLocal<Set<Period>> periodSet = new ThreadLocal<Set<Period>>();
    
    private static ThreadLocal<Integer> periodIdentifier = new ThreadLocal<Integer>();

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    public static void addPeriod( Period period )
    {
        Set<Period> set = periodSet.get();
        
        if ( set == null )
        {
            set = new HashSet<Period>();
        }
        
        set.add( period );
        
        periodSet.set( set );
    }
    
    public static boolean periodIsUnique( Period period )
    {
        if ( periodSet.get() == null )
        {
            return true;
        }
        
        return !periodSet.get().contains( period );
    }

    // -------------------------------------------------------------------------
    // Identifier
    // -------------------------------------------------------------------------

    public static int getUniqueIdentifier()
    {
        Integer identifier = periodIdentifier.get();
        
        if ( identifier == null )
        {
            identifier = 1000000;
        }
        
        identifier++;
        
        periodIdentifier.set( identifier );
        
        return identifier;
    }

    // -------------------------------------------------------------------------
    // Control
    // -------------------------------------------------------------------------

    public static void clear()
    {
        periodSet.remove();
        periodIdentifier.remove();
    }
}
