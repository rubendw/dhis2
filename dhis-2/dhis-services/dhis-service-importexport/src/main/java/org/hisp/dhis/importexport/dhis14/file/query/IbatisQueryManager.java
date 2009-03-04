package org.hisp.dhis.importexport.dhis14.file.query;

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

import java.sql.SQLException;
import java.util.List;

import org.hisp.dhis.importexport.dhis14.file.sqlmap.SqlMapClientProvider;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: IbatisQueryManager.java 5517 2008-08-04 14:59:27Z larshelg $
 */
public class IbatisQueryManager
    implements QueryManager
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private SqlMapClientProvider sqlMapClientProvider;

    public void setSqlMapClientProvider( SqlMapClientProvider sqlMapClientProvider )
    {
        this.sqlMapClientProvider = sqlMapClientProvider;
    }

    // -------------------------------------------------------------------------
    // QueryManager implementation
    // -------------------------------------------------------------------------
    
    public Object queryForObject( String query, Object parameter )
    {
        SqlMapClient sqlMapClient = sqlMapClientProvider.getSqlMapClient();
        
        try
        {
            sqlMapClient.startTransaction();
            
            Object object = sqlMapClient.queryForObject( query, parameter );
            
            sqlMapClient.commitTransaction();
            
            return object;
        }        
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Query for Object failed", ex );
        }
        finally
        {
            try
            {
                sqlMapClient.endTransaction();
            }
            catch ( SQLException ex )
            {
                throw new RuntimeException( "Failed to end transaction", ex );
            }
        }
    }
    
    @SuppressWarnings( "unchecked" )
    public List<? extends Object> queryForList( String query, Object parameter )
    {
        SqlMapClient sqlMapClient = sqlMapClientProvider.getSqlMapClient();
        
        try
        {
            sqlMapClient.startTransaction();
            
            List<? extends Object> list = sqlMapClient.queryForList( query, parameter );
            
            sqlMapClient.commitTransaction();
            
            return list;
        }        
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Query for List failed", ex );
        }
        finally
        {
            try
            {
                sqlMapClient.endTransaction();
            }
            catch ( SQLException ex )
            {
                throw new RuntimeException( "Failed to end transaction", ex );
            }
        }
    }

    public void queryWithRowhandler( String query, RowHandler rowHandler )
    {
        queryWithRowhandler( query, rowHandler, null );
    }
    
    public void queryWithRowhandler( String query, RowHandler rowHandler, Object parameter )
    {
        SqlMapClient sqlMapClient = sqlMapClientProvider.getSqlMapClient();
                
        try
        {
            sqlMapClient.startTransaction();
            
            sqlMapClient.queryWithRowHandler( query, parameter, rowHandler );
            
            sqlMapClient.commitTransaction();
        }        
        catch ( SQLException ex )
        {
            throw new RuntimeException( "Query with RowHandler failed", ex );
        }
        finally
        {
            try
            {
                sqlMapClient.endTransaction();
            }
            catch ( SQLException ex )
            {
                throw new RuntimeException( "Failed to end transaction", ex );
            }
        }
    }
}
