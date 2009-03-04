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
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.converter.AbstractDataSetConverter;
import org.hisp.dhis.importexport.mapping.NameMappingUtil;

import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @author Lars Helge Overland
 * @version $Id: DataSetRowHandler.java 4699 2008-03-11 14:56:56Z larshelg $
 */
public class DataSetRowHandler
    extends AbstractDataSetConverter implements RowHandler
{
    private ImportParams params;

    private Map<String, Integer> periodTypeMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DataSetRowHandler( BatchHandler batchHandler,
        ImportObjectService importObjectService,
        DataSetService dataSetService,
        Map<String, Integer> periodTypeMapping,
        ImportParams params )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.dataSetService = dataSetService;
        this.periodTypeMapping = periodTypeMapping;
        this.params = params;
    }
    
    // -------------------------------------------------------------------------
    // BatchRowHandler implementation
    // -------------------------------------------------------------------------

    public void handleRow( Object object )
    {
        DataSet dataSet = (DataSet) object;
        
        dataSet.getPeriodType().setId( periodTypeMapping.get( dataSet.getPeriodType().getName() ) );
        
        NameMappingUtil.addDataSetMapping( dataSet.getId(), dataSet.getName() );
        
        read( dataSet, DataSet.class, GroupMemberType.NONE, params );
    }
}
