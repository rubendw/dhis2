package org.hisp.dhis.system.stream.test;

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

import java.util.Random;

import org.hisp.dhis.system.process.InternalProcess;
import org.hisp.dhis.system.stream.BlockingPipedOutputStream;

/**
 * @author Torgeir Lorange Ostby
 * @version $Id: ProducingInternalProcess.java 4597 2008-02-16 14:11:24Z torgeilo $
 */
public class ProducingInternalProcess
    extends InternalProcess
{
    public static final int LENGTH = 65536 * 8;

    public static final int BUF_SIZE = 4096;

    private BlockingPipedOutputStream outputStream;

    @Override
    protected String execute()
        throws Exception
    {
        outputStream = new BlockingPipedOutputStream();

        Random random = new Random();
        byte[] buf = new byte[BUF_SIZE];
        int l;

        for ( l = 0; l < LENGTH && !outputStream.isClosed(); l += BUF_SIZE )
        {
            random.nextBytes( buf );
            outputStream.write( buf );
        }

        outputStream.close();

        return State.SUCCESS;
    }

    public BlockingPipedOutputStream getOutputStream()
    {
        return outputStream;
    }
}
