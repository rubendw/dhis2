package org.hisp.dhis.importexport.importobject;

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

import org.hisp.dhis.DhisConvenienceTest;
import org.hisp.dhis.importexport.ImportDataValue;
import org.hisp.dhis.importexport.ImportDataValueService;
import org.hisp.dhis.importexport.ImportObjectStatus;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ImportDataValueServiceTest
    extends DhisConvenienceTest
{
    private ImportDataValueService importDataValueService;
    
    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    public void setUpTest()
    {
        importDataValueService = (ImportDataValueService) getBean( ImportDataValueService.ID );
    }

    // -------------------------------------------------------------------------
    // ImportDataValue
    // -------------------------------------------------------------------------

    public void testAddGetImportDataValues()
    {
        ImportDataValue valueA = createImportDataValue( 1, 1, 1, 1, ImportObjectStatus.NEW );
        ImportDataValue valueB = createImportDataValue( 1, 1, 2, 2, ImportObjectStatus.NEW );
        ImportDataValue valueC = createImportDataValue( 2, 1, 2, 1, ImportObjectStatus.UPDATE );
        
        importDataValueService.addImportDataValue( valueA );
        importDataValueService.addImportDataValue( valueB );
        importDataValueService.addImportDataValue( valueC );
        
        Collection<ImportDataValue> values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 2 );

        assertTrue( values.contains( valueA ) );
        assertTrue( values.contains( valueB ) );        
        
        values = importDataValueService.getImportDataValues( ImportObjectStatus.UPDATE );
        
        assertEquals( values.size(), 1 );
        
        assertTrue( values.contains( valueC ) );
    }

    public void testDeleteImportDataValues()
    {
        ImportDataValue valueA = createImportDataValue( 1, 1, 1, 1, ImportObjectStatus.NEW );
        ImportDataValue valueB = createImportDataValue( 1, 1, 2, 2, ImportObjectStatus.NEW );
        ImportDataValue valueC = createImportDataValue( 2, 1, 2, 1, ImportObjectStatus.UPDATE );
        
        importDataValueService.addImportDataValue( valueA );
        importDataValueService.addImportDataValue( valueB );
        importDataValueService.addImportDataValue( valueC );

        Collection<ImportDataValue> values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 2 );

        values = importDataValueService.getImportDataValues( ImportObjectStatus.UPDATE );
        
        assertEquals( values.size(), 1 );
        
        importDataValueService.deleteImportDataValues();
        
        values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 0 );
        
        values = importDataValueService.getImportDataValues( ImportObjectStatus.UPDATE );
        
        assertEquals( values.size(), 0 );
    }
    
    public void testDeleteImportDataValuesByDataElement()
    {
        ImportDataValue valueA = createImportDataValue( 1, 1, 1, 1, ImportObjectStatus.NEW );
        ImportDataValue valueB = createImportDataValue( 1, 1, 2, 2, ImportObjectStatus.NEW );
        ImportDataValue valueC = createImportDataValue( 2, 1, 2, 1, ImportObjectStatus.NEW );
        
        importDataValueService.addImportDataValue( valueA );
        importDataValueService.addImportDataValue( valueB );
        importDataValueService.addImportDataValue( valueC );
        
        Collection<ImportDataValue> values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 3 );
        
        importDataValueService.deleteImportDataValuesByDataElement( 1 );
        
        values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 1 );
        
        assertTrue( values.contains( valueC ) );
    }

    public void testDeleteImportDataValuesBySource()
    {
        ImportDataValue valueA = createImportDataValue( 1, 1, 1, 1, ImportObjectStatus.NEW );
        ImportDataValue valueB = createImportDataValue( 1, 1, 2, 2, ImportObjectStatus.NEW );
        ImportDataValue valueC = createImportDataValue( 2, 1, 2, 1, ImportObjectStatus.NEW );
        
        importDataValueService.addImportDataValue( valueA );
        importDataValueService.addImportDataValue( valueB );
        importDataValueService.addImportDataValue( valueC );
        
        Collection<ImportDataValue> values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 3 );
        
        importDataValueService.deleteImportDataValuesBySource( 1 );
        
        values = importDataValueService.getImportDataValues( ImportObjectStatus.NEW );
        
        assertEquals( values.size(), 1 );
        
        assertTrue( values.contains( valueB ) );
    }
}
