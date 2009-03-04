package org.hisp.dhis.importexport.pdf.converter;

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

import java.util.ArrayList;
import java.util.Collection;

import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.importexport.pdf.util.PDFPrintUtil;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.system.util.PDFUtils;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author Lars Helge Overland
 * @version $Id: OrganisationUnitHierarchyConverter.java 5316 2008-06-02 19:06:28Z larshelg $
 */
public class OrganisationUnitHierarchyConverter
    extends PDFUtils 
        implements PDFConverter 
{
    private static final int SPACES_PER_INDENTATION = 6;
    
    private OrganisationUnitService organisationUnitService;
    
    public OrganisationUnitHierarchyConverter( OrganisationUnitService organisationUnitService )    
    {
        this.organisationUnitService = organisationUnitService;
    }

    // -------------------------------------------------------------------------
    // PDFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( Document document, ExportParams params )
    {
        PDFPrintUtil.printOrganisationUnitHierarchyFrontPage( document, params );
        
        if ( params.getOrganisationUnits() != null && params.getOrganisationUnits().size() > 0 )
        {
            Collection<OrganisationUnit> hierarchy = getHierarchy();
            
            PdfPTable table = getPdfPTable( false, 0.100f );
            
            for ( OrganisationUnit unit : hierarchy )
            {
                String indent = getIndent( unit.getLevel() );
                
                table.addCell( getTextCell( indent + unit.getName() ) );
            }
            
            addTableToDocument( document, table );
            
            moveToNewPage( document );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------
    
    private String getIndent( int step )
    {
        StringBuffer buffer = new StringBuffer();
        
        int indent = step * SPACES_PER_INDENTATION;
        
        for ( int i = 0; i < indent; i++ )            
        {
            buffer.append( " " );
        }
        
        return buffer.toString();
    }
    
    private Collection<OrganisationUnit> getHierarchy()
    {
        Collection<OrganisationUnit> hierarchy = new ArrayList<OrganisationUnit>();
        
        Collection<OrganisationUnit> roots = organisationUnitService.getRootOrganisationUnits();
        
        for ( OrganisationUnit root : roots )
        {
            hierarchy.addAll( organisationUnitService.getOrganisationUnitWithChildren( root.getId() ) );
        }            
        
        return hierarchy;
    }
}
