package org.hisp.dhis.importexport.mapping;

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

import java.util.HashSet;
import java.util.Set;

import org.hisp.dhis.importexport.AssociationType;
import org.hisp.dhis.importexport.GroupMemberAssociation;
import org.hisp.dhis.importexport.GroupMemberType;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class GroupMemberAssociationVerifier
{
    private static final String SEPARATOR = "-";
    
    private static ThreadLocal<Set<String>> associationSet = new ThreadLocal<Set<String>>();
    
    public static boolean isUnique( GroupMemberAssociation association, GroupMemberType type )
    {
        Set<String> set = associationSet.get();
        
        boolean isUnique = set != null ? !set.contains( getString( association, type ) ) : true;
        
        add( association, type );
        
        return isUnique;
    }
    
    private static void add( GroupMemberAssociation association, GroupMemberType type )
    {
        Set<String> set = associationSet.get();

        if ( set == null )
        {
            set = new HashSet<String>();
        }
                
        set.add( getString( association, type ) );
        
        associationSet.set( set );        
    }

    public static void clear()
    {
        associationSet.remove();
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private static String getString( GroupMemberAssociation association, GroupMemberType type )
    {
        if ( association.getAssociationType().equals( AssociationType.SET ) )
        {
            return association.getGroupId() + SEPARATOR + association.getMemberId() + SEPARATOR + association.getAssociationType().name() + SEPARATOR + type.name();
        }
        else if ( association.getAssociationType().equals( AssociationType.LIST ) )
        {
            return association.getGroupId() + SEPARATOR + association.getSortOrder() + SEPARATOR + association.getAssociationType().name() + SEPARATOR + type.name();
        }
        else
        {
            throw new RuntimeException( "Unknown or no AssociationType: " + association.getAssociationType() );
        }
    }
}
