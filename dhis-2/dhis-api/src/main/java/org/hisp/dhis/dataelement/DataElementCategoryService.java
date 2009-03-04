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
public interface DataElementCategoryService 
{
    String ID = DataElementCategoryService.class.getName();    

    /**
     * Adds a DataElementCategory.
     * 
     * @param dataElementCategory the DataElementCategory to add.
     * @return a generated unique id of the added Category.
     */
    int addDataElementCategory( DataElementCategory dataElementCategory );

    /**
     * Updates a DataElementCategory.
     * 
     * @param dataElementCategory the DataElementCategory to update.
     */
    void updateDataElementCategory( DataElementCategory dataElementCategory );

    /**
     * Deletes a DataElementCategory. The DataElementCategory is also removed from any
     * DataElementCategoryCombos if it is a member of. It is not possible to delete a
     * DataElementCategory with options.
     * 
     * @param dataElementCategory the DataElementCategory to delete.
     * @throws HierarchyViolationException if the DataElementCategory has children.
     */
    void deleteDataElementCategory( DataElementCategory dataElementCategory );
 
    /**
     * Returns a DataElementCategory.
     * 
     * @param id the id of the DataElementCTEGORY to return.
     * @return the DataElementCategory with the given id, or null if no match.
     */
    DataElementCategory getDataElementCategory( int id );
    
    /**
     * 
     * @param name
     * @return
     */
    DataElementCategory getDataElementCategoryByName( String name );
    
    /**
     * Returns all DataElementCategories.
     * 
     * @return a collection of all DataElementCategories, or an empty collection if there
     *         are no DataElementCategories.
     */
    Collection<DataElementCategory> getAllDataElementCategories();
    
    /**
     * 
     * @param category
     * @return
     */
    Collection<DataElementCategoryOption> getOrderedOptions( DataElementCategory category );
}
