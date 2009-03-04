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
 * @version $Id: ImportObjectService.java 5793 2008-10-02 14:14:00Z larshelg $
 */
public interface ImportObjectService
{
    String ID = ImportObjectService.class.getName();

    // -------------------------------------------------------------------------
    // ImportObject
    // -------------------------------------------------------------------------

    /**
     * Adds an ImportObject.
     * 
     * @param status the ImportObjectStatus of the ImportObject.
     * @param clazz the Class of the ImportObject.
     * @param groupMemberType the GroupMemberType of the object.
     * @param object the belonging Object.
     */
    int addImportObject( ImportObjectStatus status, Class<?> clazz, GroupMemberType groupMemberType, Object object );
    
    /**
     * Adds an ImportObject.
     * 
     * @param status the ImportObjectStatus of the ImportObject.
     * @param clazz the Class of the ImportObject.
     * @param object the belonging Object.
     * @param compareObject the matching Object of the belonging Object.
     */
    int addImportObject( ImportObjectStatus status, Class<?> clazz, Object object, Object compareObject );
    
    /**
     * Adds an ImportObject.
     * 
     * @param status the ImportObjectStatus of the ImportObject.
     * @param clazz the Class of the ImportObject.
     * @param groupMemberType the GroupMemberType of the object.
     * @param object the belonging Object.
     * @param compareObject the matching Object of the belonging Object.
     */
    int addImportObject( ImportObjectStatus status, Class<?> clazz, GroupMemberType groupMemberType, Object object, Object compareObject );
    /**
     * Gets the ImportObject with the given identifier.
     * 
     * @param id the identifier of the ImportObject.
     * @return the ImportObject with the given identifier.
     */
    ImportObject getImportObject( int id );

    /**
     * Gets the ImportObjects of the given Class type.
     * @param clazz the Class type of the ImportObjects.
     * 
     * @return a collection of ImportObjects of the given Class type.
     */
    Collection<ImportObject> getImportObjects( Class<?> clazz );
    
    /**
     * Gets the ImportObjects with the given ImportObjectStatus and of the given Class type.
     * @param status the status of the ImportObjects.
     * @param clazz the Class type of the ImportObjects.
     * 
     * @return a collection of ImportObjects of the given ImportObjectStatus and Class type.
     */
    Collection<ImportObject> getImportObjects( ImportObjectStatus status, Class<?> clazz );
    
    /**
     * Gets the ImportObjects of type GroupMemberAssociation with the given GroupMemberType.
     * 
     * @param groupMemberType the GroupMemberType.
     * @return the ImportObjects of type GroupMemberAssociation with the given GroupMemberType.
     */
    Collection<ImportObject> getImportObjects( GroupMemberType groupMemberType );

    /**
     * Deletes the ImportObject with the given identifier.
     * 
     * @param importObjectId the identifier of the ImportObject.
     */
    void deleteImportObject( int importObjectId );
    
    /**
     * Deletes the ImportObjects of the given Class type.
     * 
     * @param clazz the Class type of the ImportObjects.
     */
    void deleteImportObjects( Class<?> clazz );
    
    /**
     * Deletes all ImportObjects.
     */
    void deleteImportObjects();

    /**
     * Deletes the ImportObject with the given identifier, as well as all objects
     * depending on this object.
     * 
     * @param importObjectId the identifier of the ImportObject.
     */
    void cascadeDeleteImportObject( int importObjectId );
    
    /**
     * Deletes the ImportObjects of the given Class type and ImportObjectStatus, 
     * as well as all objects with the same ImportObjectStatus depending on objects of this Class.
     * 
     * @param clazz the Class type of the ImportObjects.
     */
    void cascadeDeleteImportObjects( Class<?> clazz );

    // -------------------------------------------------------------------------
    // Object
    // -------------------------------------------------------------------------

    /**
     * Matches the ImportObject with the given identifier to an existing object of the given identifier.
     * The Class type of the existing object is derived from the ImportObject. All properties, except the
     * identifier, of the relevant ImportObject is updated with the properties of the existing object. The
     * status of the ImportObject is set to MATCH.
     * 
     * @param importObjectId the identifier of the ImportObject.
     * @param existingObjectId the identifier of the existing object. 
     */
    void matchObject( int importObjectId, int existingObjectId );
        
    // -------------------------------------------------------------------------
    // Import
    // -------------------------------------------------------------------------

    /**
     * Imports or updates all ImportObjects.
     */
    void importAll();
}
