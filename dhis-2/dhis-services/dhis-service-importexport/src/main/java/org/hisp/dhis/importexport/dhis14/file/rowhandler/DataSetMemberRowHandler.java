package org.hisp.dhis.importexport.dhis14.file.rowhandler;

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

import java.util.Map;

import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.importexport.AssociationType;
import org.hisp.dhis.importexport.GroupMemberAssociation;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.converter.AbstractGroupMemberConverter;
import org.hisp.dhis.importexport.dhis14.object.Dhis14GroupMemberAssociation;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: DataSetMemberRowHandler.java 5488 2008-07-03 13:34:34Z larshelg $
 */
public class DataSetMemberRowHandler
    extends AbstractGroupMemberConverter implements RowHandler
{
    private Map<Object, Integer> dataElementMapping;
    
    private Map<Object, Integer> dataSetMapping;
    
    private ImportParams params;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DataSetMemberRowHandler( BatchHandler batchHandler,
        ImportObjectService importObjectService,
        Map<Object, Integer> dataElementMapping, 
        Map<Object, Integer> dataSetMapping,
        ImportParams params )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.dataElementMapping = dataElementMapping;
        this.dataSetMapping = dataSetMapping;
        this.params = params;
    }
    
    // -------------------------------------------------------------------------
    // BatchRowHandler implementation
    // -------------------------------------------------------------------------

    public void handleRow( Object object )
    {
        Dhis14GroupMemberAssociation dhis14Association = (Dhis14GroupMemberAssociation) object;

        GroupMemberAssociation association = new GroupMemberAssociation( AssociationType.SET );
        
        Integer groupId = dataSetMapping.get( dhis14Association.getGroupId() );
        Integer memberId = dataElementMapping.get( dhis14Association.getMemberId() );
        
        if ( groupId != null && memberId != null )
        {
            association.setGroupId( groupId );
            association.setMemberId( memberId );
                
            read( association, GroupMemberAssociation.class, GroupMemberType.DATASET, params );
        }
    }
}
