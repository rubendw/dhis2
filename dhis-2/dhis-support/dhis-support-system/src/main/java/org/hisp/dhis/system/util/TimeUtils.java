package org.hisp.dhis.system.util;

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

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lars Helge Overland
 * @version $Id: TimeUtil.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class TimeUtils
{
    private static final Log log = LogFactory.getLog( TimeUtils.class );
    
    private static StopWatch watch;
    
    static
    {
        watch = new StopWatch();
    }
    
    public static void start()
    {
        watch.reset();
        watch.start();
    }
    
    public static long getMillis()
    {
        watch.split();
        
        return watch.getSplitTime();
    }

    public static String getHMS()
    {
        watch.split();
        
        return DurationFormatUtils.formatDurationHMS( watch.getSplitTime() );
    }
    
    public static void markMillis( String message )
    {
        watch.split();
        
        log.info( message + ": " + watch.getSplitTime() );
    }
    
    public static void markHMS( String message )
    {
        watch.split();
        
        String time = DurationFormatUtils.formatDurationHMS( watch.getSplitTime() );
        
        log.info( message + ": " + time + " ms" );
    }
    
    public static void stop()
    {
        watch.stop();
        watch.reset();
    }
}
