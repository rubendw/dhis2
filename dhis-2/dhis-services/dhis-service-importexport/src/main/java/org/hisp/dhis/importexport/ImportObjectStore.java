package org.hisp.dhis.importexport;

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
 * @author Lars Helge Overland
 * @version $Id: ImportObjectStore.java 5793 2008-10-02 14:14:00Z larshelg $
 */
public interface ImportObjectStore
{
    String ID = ImportObjectStore.class.getName();
    
    /**
     * Adds an ImportObject.
     * 
     * @param importObject The ImportObject to add.
     * @return The generated identifier.
     */
    int addImportObject( ImportObject importObject );
    
    /**
     * Updates an ImportObject.
     * 
     * @param importObject The ImportObject to update.
     */
    void updateImportObject( ImportObject importObject );
    
    /**
     * Retrieves an ImportObject.
     * 
     * @param id The identifier of the ImportObject to retrieve.
     * @return The retrieved ImportObject.
     */
    ImportObject getImportObject( int id );
    
    /**
     * Retrieves ImportObjects of the given class.
     * 
     * @param clazz The class of the ImportObjects to retrieve.
     * @return A collection of retrieved ImportObjects.
     */
    Collection<ImportObject> getImportObjects( Class<?> clazz );
    
    /**
     * Retrieves ImportObjects of the given ImportObjectStatus and class.
     * 
     * @param status The ImportObjectStatus of the ImportObjects to retrive.
     * @param clazz The class of the ImportObjects to retrieve.
     * @return A collection of retrieved ImportObjects.
     */
    Collection<ImportObject> getImportObjects( ImportObjectStatus status, Class<?> clazz );
    
    /**
     * Retrives ImportObjects of the given GroupMemberType.
     * 
     * @param groupMemberType The GroupMemberType of the ImportObjects to retrieve.
     * @return A collection of retrieved ImportObjects.
     */
    Collection<ImportObject> getImportObjects( GroupMemberType groupMemberType );

    /**
     * Deletes an ImportObject.
     * 
     * @param importObject The ImportObject to delete. 
     */
    void deleteImportObject( ImportObject importObject );
    
    /**
     * Deletes ImportObjects of the given class.
     * 
     * @param clazz The class of the ImportObjects to delete.
     */
    void deleteImportObjects( Class<?> clazz );
    
    /**
     * Deletes ImportObjects of the given GroupMemberType.
     * 
     * @param groupMemberType The GroupMemberType of the ImportObjects to delete.
     */
    void deleteImportObjects( GroupMemberType groupMemberType );
    
    /**
     * Deletes all ImportObjects.
     */
    void deleteImportObjects();
}
