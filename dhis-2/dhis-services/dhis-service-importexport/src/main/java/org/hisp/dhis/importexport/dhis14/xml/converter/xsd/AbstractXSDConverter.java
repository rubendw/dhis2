package org.hisp.dhis.importexport.dhis14.xml.converter.xsd;

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

import org.amplecode.staxwax.writer.XMLWriter;
import org.hisp.dhis.importexport.XMLConverter;

/**
 * @author Lars Helge Overland
 * @version $Id: AbstractXSDConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public abstract class AbstractXSDConverter
    implements XMLConverter 
{
    protected void writeAnnotation( XMLWriter writer )
    {
        writer.openElement( "xsd:annotation" );
        
        writer.writeElement( "xsd:appinfo", "" );
        
        writer.closeElement( "xsd:annotation" );
    }
    
    protected void writeInteger( XMLWriter writer, String name, int minOccurs, boolean nonNullable )
    {
        String nonNull = nonNullable ? "yes" : "no";
        
        writer.writeElement( "xsd:element", "", "name", name, "minOccurs", String.valueOf( minOccurs ), "od:jetType", "integer", "od:sqlType", "smallint", "od:nonNullable", nonNull, "type", "xsd:short" );        
    }
    
    protected void writeLongInteger( XMLWriter writer, String name, int minOccurs, boolean nonNullable )
    {
        String nonNull = nonNullable ? "yes" : "no";
        
        writer.writeElement( "xsd:element", "", "name", name, "minOccurs", String.valueOf( minOccurs ), "od:jetType", "longinteger", "od:sqlType", "int", "od:nonNullable", nonNull, "type", "xsd:int" );
    }
    
    protected void writeDouble( XMLWriter writer, String name, int minOccurs, boolean nonNullable )
    {
        String nonNull = nonNullable ? "yes" : "no";
        
        writer.writeElement( "xsd:element", "", "name", name, "minOccurs", String.valueOf( minOccurs ), "od:jetType", "double", "od:sqlSType", "float", "od:nonNullable", nonNull, "type", "xsd:double" );
    }
    
    protected void writeDateTime( XMLWriter writer, String name, int minOccurs, boolean nonNullable )
    {
        String nonNull = nonNullable ? "yes" : "no";
        
        writer.writeElement( "xsd:element", "", "name", name, "minOccurs", String.valueOf( minOccurs ), "od:jetType", "datetime", "od:sqlType", "datetime", "od:nonNullable", nonNull, "type", "xsd:dateTime" );
    }
    
    protected void writeText( XMLWriter writer, String name, int minOccurs, boolean nonNullable, int length )
    {
        String nonNull = nonNullable ? "yes" : "no";
        
        writer.openElement( "xsd:element", "name", name, "minOccurs", String.valueOf( minOccurs ), "od:jetType", "text", "od:sqlType", "nvarchar", "od:nonNullable", nonNull );
        
        writer.openElement( "xsd:simpleType" );
        
        writer.openElement( "xsd:restriction", "base", "xsd:string" );
        
        writer.writeElement( "xsd:maxLength", "", "value", String.valueOf( length ) );
        
        writer.closeElement( "xsd:restriction" );
        
        writer.closeElement( "xsd:simpleType" );
        
        writer.closeElement( "xsd:element" );
    }
    
    protected void writeMemo( XMLWriter writer, String name, int minOccurs, boolean nonNullable, int length )
    {
        String nonNull = nonNullable ? "yes" : "no";
                
        writer.openElement( "xsd:element", "name", name, "minOccurs", String.valueOf( minOccurs ), "od:jetType", "memo", "od:sqlType", "ntext", "od:nonNullable", nonNull );
        
        writer.openElement( "xsd:simpleType" );
        
        writer.openElement( "xsd:restriction", "base", "xsd:string" );
        
        writer.writeElement( "xsd:maxLength", "", "value", String.valueOf( length ) );
        
        writer.closeElement( "xsd:restriction" );
        
        writer.closeElement( "xsd:simpleType" );
        
        writer.closeElement( "xsd:element" );
    }
}
