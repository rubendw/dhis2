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

import static org.hisp.dhis.system.util.MathUtils.expressionIsTrue;

/**
 * @author Lars Helge Overland
 * @version $Id: MathUtil.java 4712 2008-03-12 10:32:45Z larshelg $
 */
public class MathUtilsTest
    extends TestCase
{
    private static final String OPERATOR_EQUAL = "==";
    private static final String OPERATOR_NOT_EQUAL = "!=";
    private static final String OPERATOR_GREATER = ">";
    private static final String OPERATOR_GREATER_EQUAL = ">=";
    private static final String OPERATOR_LESSER = "<";
    private static final String OPERATOR_LESSER_EQUAL = "<=";
    
    public void testExpressionIsTrue()
    {
        assertFalse( expressionIsTrue( 20.0, OPERATOR_EQUAL, 10.0 ) );
        assertTrue( expressionIsTrue( 20.0, OPERATOR_NOT_EQUAL, 10.0 ) );
        assertTrue( expressionIsTrue( 20.0, OPERATOR_GREATER, 10.0 ) );
        assertTrue( expressionIsTrue( 20.0, OPERATOR_GREATER_EQUAL, 20.0 ) );
        assertFalse( expressionIsTrue( 30.0, OPERATOR_LESSER, 15.0 ) );
        assertTrue( expressionIsTrue( 40.0, OPERATOR_LESSER_EQUAL, 50.0 ) );
        assertFalse( expressionIsTrue( 0.0, OPERATOR_GREATER_EQUAL, 20.0 ) );
    }
}
