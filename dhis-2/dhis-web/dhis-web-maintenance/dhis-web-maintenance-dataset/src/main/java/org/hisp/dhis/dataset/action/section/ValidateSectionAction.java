package org.hisp.dhis.dataset.action.section;

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

import org.hisp.dhis.dataset.Section;
import org.hisp.dhis.dataset.SectionService;
import org.hisp.dhis.i18n.I18n;

import com.opensymphony.xwork.Action;

public class ValidateSectionAction
    implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SectionService sectionService;

    public void setSectionService( SectionService sectionService )
    {
        this.sectionService = sectionService;
    }

    // -------------------------------------------------------------------------
    // Input & output
    // -------------------------------------------------------------------------

    private I18n i18n;

    private String name;

    private String label;

    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }

    public void setI18n( I18n i18n )
    {
        this.i18n = i18n;
    }

    public String getName()
    {
        return name;
    }

    public String getLabel()
    {
        return label;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {
        if ( name == null )
        {
            message = i18n.getString( "specify_name" );

            return INPUT;
        }
        else
        {
            name = name.trim();

            if ( name.length() == 0 )
            {
                message = i18n.getString( "specify_name" );

                return INPUT;
            }

            if ( label == null )
            {
                message = i18n.getString( "specify_label" );

                return INPUT;
            }
            else
            {
                label = label.trim();

                if ( label.length() == 0 )
                {

                    message = i18n.getString( "specify_label" );

                    return INPUT;
                }
            }

            Section match = sectionService.getSectionByName( name );

            if ( match != null )
            {
                message = i18n.getString( "duplicate_section_names" );

                return INPUT;
            }
        }

        return SUCCESS;
    }
}
