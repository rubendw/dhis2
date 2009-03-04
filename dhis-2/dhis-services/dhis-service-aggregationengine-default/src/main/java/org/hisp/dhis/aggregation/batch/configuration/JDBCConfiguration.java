package org.hisp.dhis.aggregation.batch.configuration;

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

import org.hisp.dhis.aggregation.batch.factory.StatementDialect;

/**
 * @author Lars Helge Overland
 * @version $Id: JDBCConfiguration.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class JDBCConfiguration
{
    private StatementDialect dialect;
    
    private String driverClass;
    
    private String connectionUrl;
    
    private String username;
    
    private String password;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public JDBCConfiguration()
    {   
    }
    
    public JDBCConfiguration( StatementDialect dialect, String driverClass, String connectionUrl,
        String username, String password )
    {
        this.dialect = dialect;
        this.driverClass = driverClass;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public StatementDialect getDialect()
    {
        return dialect;
    }

    public void setDialect( StatementDialect dialect )
    {
        this.dialect = dialect;
    }
    
    public String getDriverClass()
    {
        return driverClass;
    }

    public void setDriverClass( String driverClass )
    {
        this.driverClass = driverClass;
    }

    public String getConnectionUrl()
    {
        return connectionUrl;
    }

    public void setConnectionUrl( String connectionUrl )
    {
        this.connectionUrl = connectionUrl;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }
}
