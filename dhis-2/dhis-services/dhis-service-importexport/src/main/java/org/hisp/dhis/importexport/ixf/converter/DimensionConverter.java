package org.hisp.dhis.importexport.ixf.converter;

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
import java.util.HashSet;
import java.util.Set;

import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.aggregation.batch.handler.BatchHandler;
import org.hisp.dhis.dataelement.DataElementCategory;
import org.hisp.dhis.dataelement.DataElementCategoryComboService;
import org.hisp.dhis.dataelement.DataElementCategoryOptionService;
import org.hisp.dhis.dataelement.DataElementCategoryService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractDataElementCategoryConverter;

/**
 * @author Lars Helge Overland
 * @version $Id: DimensionConverter.java 5742 2008-09-26 11:37:35Z larshelg $
 */
public class DimensionConverter
    extends AbstractDataElementCategoryConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "dimensions";
    public static final String ELEMENT_NAME = "simpleDim";
    
    private static final String FIELD_NAME = "name";
    
    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private XMLConverter itemConverter;
    
    private BatchHandler categoryOptionBatchHandler;
    
    private DataElementCategoryOptionService categoryOptionService;
    
    private DataElementCategoryComboService categoryComboService;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public DimensionConverter()
    {
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param batchHandler
     * @param importObjectService
     * @param categoryService
     */
    public DimensionConverter( BatchHandler batchHandler,
        ImportObjectService importObjectService,
        DataElementCategoryService categoryService,
        BatchHandler categoryOptionBatchHandler,
        DataElementCategoryOptionService categoryOptionService,
        DataElementCategoryComboService categoryComboService )
    {
        this.batchHandler = batchHandler;
        this.importObjectService = importObjectService;
        this.categoryService = categoryService;
        this.categoryOptionBatchHandler = categoryOptionBatchHandler;
        this.categoryOptionService = categoryOptionService;
        this.categoryComboService = categoryComboService;
    }
    
    // -------------------------------------------------------------------------
    // IXFConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<DataElementCategory> categories = params.getCategories();
        
        if ( categories != null && categories.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( DataElementCategory category : categories )
            {
                writer.openElement( ELEMENT_NAME, "name", category.getName() );
                
                writer.writeElement( FIELD_NAME, category.getName(), "lang", "en" );
    
                // -----------------------------------------------------------------
                // Items are embedded in the dimension collection
                // -----------------------------------------------------------------
                
                itemConverter = new DimensionItemConverter( category.getCategoryOptions() );
                
                itemConverter.write( writer, params );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }

    public void read( XMLReader reader, ImportParams params )
    {
        Set<DataElementCategory> categories = new HashSet<DataElementCategory>();
        
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            DataElementCategory category = new DataElementCategory();
            
            reader.moveToStartElement( FIELD_NAME );
            category.setName( reader.getElementValue() );
            
            categories.add( category );
            
            read( category, DataElementCategory.class, GroupMemberType.NONE, params );

            // -----------------------------------------------------------------
            // Items are embedded in the dimension collection
            // -----------------------------------------------------------------
            
            itemConverter = new DimensionItemConverter( categoryOptionBatchHandler,
                importObjectService, categoryOptionService );
            
            itemConverter.read( reader, params );
        }
        
        XMLConverter dimensionComboConverter = new DimensionComboConverter( categories, categoryComboService );
        
        dimensionComboConverter.read( reader, params );
    }
}
