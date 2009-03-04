package org.hisp.dhis.aggregation.batch.statement;

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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class StatementHolder
{
    private Connection connection;
    
    private boolean pooled;

    private Statement statement;
    
    public StatementHolder( Connection connection, boolean pooled )
    {
        this.connection = connection;
        this.pooled = pooled;
        
        try
        {
            this.statement = connection.createStatement();
        }
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Failed to create statement", ex );
        }
    }
    
    public Statement getStatement()
    {
        return statement;
    }
    
    public Connection getConnection()
    {
        return connection;
    }
    
    public boolean isPooled()
    {
        return pooled;
    }

    public void close()
    {
        if ( !pooled )
        {
            if ( statement != null )
            {
                try
                {
                    statement.close();
                }
                catch ( SQLException ex )
                {   
                }
            }
            
            if ( connection != null )
            {
                try
                {
                    connection.close();
                }
                catch ( SQLException ex )
                {   
                }
            }
        }
    }
}
