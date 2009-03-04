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

import java.util.List;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: QueryManager.java 5517 2008-08-04 14:59:27Z larshelg $
 */
public interface QueryManager
{
    /**
     * Performs a query for a single object.
     * 
     * @param query the name of the query.
     * @param parameter the parameter object.
     * @param source the Source.
     * @return the queried object.
     */
    Object queryForObject( String query, Object parameter );
    
    /**
     * Performs a query for a list.
     * 
     * @param query the name of the query.
     * @param parameter the parameter object.
     * @param source the Source.
     * @return the queried List.
     */
    List<? extends Object> queryForList( String query, Object parameter );
    
    /**
     * Performs a query with a RowHandler.
     * 
     * @param query the name of the query.
     * @param rowHandler the RowHandler.
     * @param source the Source.
     */
    void queryWithRowhandler( String query, RowHandler rowHandler );
    
    /**
     * Performs a query with a RowHandler.
     * 
     * @param query the name of the query.
     * @param rowHandler the RowHandler.
     * @param source the Source.
     */
    void queryWithRowhandler( String query, RowHandler rowHandler, Object parameter );
}
