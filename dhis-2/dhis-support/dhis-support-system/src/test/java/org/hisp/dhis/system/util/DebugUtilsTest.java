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

import junit.framework.TestCase;

import static org.hisp.dhis.system.util.DebugUtils.logDuplicates;
import static org.hisp.dhis.system.util.DebugUtils.resetDuplicates;
import static org.hisp.dhis.system.util.DebugUtils.SEPARATOR;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class DebugUtilsTest
    extends TestCase
{
    public void testLogDuplicate()
    {
        String keyA = "name";
        String keyB = "code";
        String keyC = "identifier";
        
        assertNull( logDuplicates( keyA, "john" ) );
        assertNull( logDuplicates( keyA, "tom" ) );
        assertNull( logDuplicates( keyA, "george" ) );
        assertEquals( "tom", logDuplicates( keyA, "tom" ) );
        assertNull( logDuplicates( keyA, "will" ) );
        assertEquals( "john", logDuplicates( keyA, "john" ) );
                
        assertNull( logDuplicates( keyB, "john" ) );
        assertNull( logDuplicates( keyB, "A2", "A3" ) );
        assertNull( logDuplicates( keyB, "B1" ) );
        assertEquals( "A2" + SEPARATOR + "A3", logDuplicates( keyB, "A2", "A3" ) );
        assertEquals( "A2" + SEPARATOR + "A3", logDuplicates( keyB, "A2", "A3" ) );
        assertNull( logDuplicates( keyB, "C1" ) );
        
        assertNull( logDuplicates( keyC, 1, 2, 3 ) );
        assertNull( logDuplicates( keyC, 1, 2, 4 ) );
        assertEquals( 1 + SEPARATOR + 2 + SEPARATOR + 3, logDuplicates( keyC, 1, 2, 3 ) );        
        
        assertTrue( resetDuplicates( keyA ) );
        assertNull( logDuplicates( keyA, "tom" ) );
    }
}
