package org.hisp.dhis.source;

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

import org.hisp.dhis.DhisSpringTest;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: SourceStoreTest.java 3200 2007-03-29 11:51:17Z torgeilo $
 */
public class SourceStoreTest
    extends DhisSpringTest
{
    private SourceStore sourceStore;

    // -------------------------------------------------------------------------
    // Set up/tear down
    // -------------------------------------------------------------------------

    public void setUpTest()
        throws Exception
    {
        sourceStore = (SourceStore) getBean( SourceStore.ID );
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    public void testSource()
        throws Exception
    {
        String sourceAName = "DummySourceA";
        String sourceBName = "DummySourceB";

        Source sourceA = new DummySource( sourceAName );
        Source sourceB = new DummySource( sourceBName );

        assertEquals( 0, sourceStore.getAllSources().size() );
        assertEquals( 0, sourceStore.getAllSources( DummySource.class ).size() );

        int sourceAId = sourceStore.addSource( sourceA );
        DummySource sourceAA = sourceStore.getSource( sourceAId );
        assertEquals( sourceAId, sourceAA.getId() );
        assertEquals( sourceAName, sourceAA.getName() );

        assertEquals( 1, sourceStore.getAllSources().size() );
        assertEquals( 1, sourceStore.getAllSources( DummySource.class ).size() );

        int sourceBId = sourceStore.addSource( sourceB );
        DummySource sourceBB = sourceStore.getSource( sourceBId );
        assertEquals( sourceBId, sourceBB.getId() );
        assertEquals( sourceBName, sourceBB.getName() );

        assertEquals( 2, sourceStore.getAllSources().size() );
        assertEquals( 2, sourceStore.getAllSources( DummySource.class ).size() );

        try
        {
            sourceStore.addSource( new DummySource( sourceAName ) );
            fail();
        }
        catch ( Exception e )
        {
            // Expected
        }

        sourceAName = "DummySourceAA";
        sourceAA.setName( sourceAName );
        sourceStore.updateSource( sourceAA );
        sourceAA = sourceStore.getSource( sourceAId );
        assertEquals( sourceAId, sourceAA.getId() );
        assertEquals( sourceAName, sourceAA.getName() );

        assertEquals( 2, sourceStore.getAllSources().size() );

        sourceStore.deleteSource( sourceAA );
        assertNull( sourceStore.getSource( sourceAId ) );
        assertEquals( 1, sourceStore.getAllSources().size() );
        sourceBB = sourceStore.getSource( sourceBId);
        sourceStore.deleteSource( sourceBB );
        assertNull( sourceStore.getSource( sourceBId ) );
        assertEquals( 0, sourceStore.getAllSources().size() );
    }
}
