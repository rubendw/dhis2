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

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.i18n.I18n;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.PDFConverter;
import org.hisp.dhis.importexport.pdf.util.PDFPrintUtil;
import org.hisp.dhis.system.util.DateUtils;
import org.hisp.dhis.system.util.PDFUtils;

/**
 * @author Lars Helge Overland
 * @version $Id: ExtendedDataElementConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class ExtendedDataElementConverter
    extends PDFUtils
        implements PDFConverter 
{
    public ExtendedDataElementConverter()
    {   
    }    

    // -------------------------------------------------------------------------
    // PDFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( Document document, ExportParams params )
    {
        PDFPrintUtil.printDataElementFrontPage( document, params );
        
        I18n i18n = params.getI18n();
                
        for ( DataElement element : params.getDataElements() )
        {
            PdfPTable table = getPdfPTable( true, 0.40f, 0.60f );
            
            table.addCell( getHeader3Cell( element.getName(), 2 ) );
            
            table.addCell( getCell( 2, 15 ) );
    
            // -------------------------------------------------------------------------
            // Identifying and definitional attributes
            // -------------------------------------------------------------------------
            
            table.addCell( getHeader4Cell( i18n.getString( "identifying_and_definitional_attributes" ), 2 ) );
    
            table.addCell( getCell( 2, 8 ) );
    
            table.addCell( getItalicCell( i18n.getString( "short_name" ), 1 ) );
            table.addCell( getTextCell( element.getShortName() ) );
            
            table.addCell( getItalicCell( i18n.getString( "alternative_name" ), 1 ) );
            table.addCell( getTextCell( element.getAlternativeName() ) );
            
            table.addCell( getItalicCell( i18n.getString( "code" ), 1 ) );
            table.addCell( getTextCell( element.getCode() ) );
            
            table.addCell( getItalicCell( i18n.getString( "description" ), 1 ) );
            table.addCell( getTextCell( element.getDescription() ) );
            
            table.addCell( getItalicCell( i18n.getString( "active" ), 1 ) );
            table.addCell( getTextCell( i18n.getString( String.valueOf( element.isActive() ) ) ) );
            
            table.addCell( getItalicCell( i18n.getString( "type" ), 1 ) );
            table.addCell( getTextCell( i18n.getString( element.getType() ) ) );            
            
            table.addCell( getItalicCell( i18n.getString( "aggregation_operator" ), 1 ) );
            table.addCell( getTextCell( i18n.getString( element.getAggregationOperator() ) ) );
    
            table.addCell( getItalicCell( i18n.getString( "mnemonic" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getMnemonic() ) );
    
            table.addCell( getItalicCell( i18n.getString( "version" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getVersion() ) );
    
            table.addCell( getItalicCell( i18n.getString( "context" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getContext() ) );
    
            table.addCell( getItalicCell( i18n.getString( "synonyms" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getSynonyms() ) );
    
            table.addCell( getItalicCell( i18n.getString( "hononyms" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getHononyms() ) );
    
            table.addCell( getItalicCell( i18n.getString( "keywords" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getKeywords() ) );
    
            table.addCell( getItalicCell( i18n.getString( "status" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getStatus() ) );
    
            table.addCell( getItalicCell( i18n.getString( "status_date" ), 1 ) );
            table.addCell( getTextCell( DateUtils.getMediumDateString( element.getExtended().getStatusDate() ) ) );
            
            table.addCell( getCell( 2, 15 ) );
    
            // -------------------------------------------------------------------------
            // Relational and representational attributes
            // -------------------------------------------------------------------------
            
            table.addCell( getHeader4Cell( i18n.getString( "relational_and_representational_attributes" ), 2 ) );
    
            table.addCell( getCell( 2, 8 ) );
    
            table.addCell( getItalicCell( i18n.getString( "data_type" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getDataType() ) );
    
            table.addCell( getItalicCell( i18n.getString( "representational_form" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getRepresentationalForm() ) );
    
            table.addCell( getItalicCell( i18n.getString( "representational_layout" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getRepresentationalLayout() ) );
    
            table.addCell( getItalicCell( i18n.getString( "minimum_size" ), 1 ) );
            table.addCell( getTextCell( String.valueOf( element.getExtended().getMinimumSize() ) ) );
    
            table.addCell( getItalicCell( i18n.getString( "maximum_size" ), 1 ) );
            table.addCell( getTextCell( String.valueOf( element.getExtended().getMaximumSize() ) ) );
    
            table.addCell( getItalicCell( i18n.getString( "data_domain" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getDataDomain() ) );
    
            table.addCell( getItalicCell( i18n.getString( "validation_rules" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getValidationRules() ) );
    
            table.addCell( getItalicCell( i18n.getString( "related_data_references" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getRelatedDataReferences() ) );
    
            table.addCell( getItalicCell( i18n.getString( "guide_for_use" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getGuideForUse() ) );
    
            table.addCell( getItalicCell( i18n.getString( "collection_methods" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getCollectionMethods() ) );
    
            table.addCell( getCell( 2, 15 ) );
    
            // -------------------------------------------------------------------------
            // Administrative attributes 
            // -------------------------------------------------------------------------
    
            table.addCell( getHeader4Cell( i18n.getString( "administrative_attributes" ), 2 ) );
    
            table.addCell( getCell( 2, 8 ) );
    
            table.addCell( getItalicCell( i18n.getString( "responsible_authority" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getResponsibleAuthority() ) );
    
            table.addCell( getItalicCell( i18n.getString( "update_rules" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getUpdateRules() ) );
    
            table.addCell( getItalicCell( i18n.getString( "access_authority" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getAccessAuthority() ) );
    
            table.addCell( getItalicCell( i18n.getString( "update_frequency" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getUpdateFrequency() ) );
    
            table.addCell( getItalicCell( i18n.getString( "location" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getLocation() ) );
    
            table.addCell( getItalicCell( i18n.getString( "reporting_methods" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getReportingMethods() ) );
    
            table.addCell( getItalicCell( i18n.getString( "version_status" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getVersionStatus() ) );
    
            table.addCell( getItalicCell( i18n.getString( "previous_version_references" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getPreviousVersionReferences() ) );
    
            table.addCell( getItalicCell( i18n.getString( "source_document" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getSourceDocument() ) );
    
            table.addCell( getItalicCell( i18n.getString( "source_organisation" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getSourceOrganisation() ) );
    
            table.addCell( getItalicCell( i18n.getString( "comment" ), 1 ) );
            table.addCell( getTextCell( element.getExtended().getComment() ) );
    
            table.addCell( getItalicCell( i18n.getString( "saved" ), 1 ) );
            table.addCell( getTextCell( DateUtils.getMediumDateString( element.getExtended().getSaved() ) ) );
    
            table.addCell( getItalicCell( i18n.getString( "last_updated" ), 1 ) );
            table.addCell( getTextCell( DateUtils.getMediumDateString( element.getExtended().getLastUpdated() ) ) );
            
            table.addCell( getCell( 2, 30 ) );
            
            addTableToDocument( document, table );
        }
    }
}
