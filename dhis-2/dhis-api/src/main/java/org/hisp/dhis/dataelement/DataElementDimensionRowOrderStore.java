package org.hisp.dhis.dataelement;

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

import java.util.Collection;

/**
* @author Abyot Asalefew
* @version $Id$
*/
public interface DataElementDimensionRowOrderStore 
{
    String ID = DataElementDimensionRowOrderStore.class.getName();    

    /**
     * Adds a DataElementDimensionRowOrder.
     * 
     * @param dataElementDimensionRowOrder the DataElementDimensionRowOrder to add.
     * @return a generated unique id of the added DataElementDimensionRowOrder.
     */
    void addDataElementDimensionRowOrder( DataElementDimensionRowOrder dataElementDimensionRowOrder );

    /**
     * Updates a DataElementDimensionRowOrder.
     * 
     * @param dataElementDimensionRowOrder the DataElementDimensionRowOrder to update.
     */
    void updateDataElementDimensionRowOrder( DataElementDimensionRowOrder dataElementDimensionRowOrder );

    /**
     * Deletes a DataElementDimensionRowOrder.  
     * 
     * @param dataElementDimensionRowOrder the DataElementDimensionRowOrder to delete.     
     */
    void deleteDataElementDimensionRowOrder( DataElementDimensionRowOrder dataElementDimensionRowOrder );
   
    /**
     * 
     * @param categoryCombo
     * @return
     */
    int deleteDataElementDimensionRowOrder( DataElementCategoryCombo categoryCombo );
    
    /**
     * 
     * @param category
     * @return
     */
    int deleteDataElementDimensionRowOrder( DataElementCategory category );
    
    /**
     * 
     * @param categoryCombo
     * @param category
     * @return
     */
    DataElementDimensionRowOrder getDataElementDimensionRowOrder( DataElementCategoryCombo categoryCombo,  DataElementCategory category);
    
    /**
     * Returns all DataElementDimensionRowOrders which belong to a single table row.
     * 
     * @return a collection of all DataElementDimensionRowOrders, or an empty collection if there
     *         are no DataElementDimensionRowOrders.
     */
    Collection<DataElementDimensionRowOrder> getDataElementDimensionRowOrders( DataElementCategoryCombo categoryCombo );
}
