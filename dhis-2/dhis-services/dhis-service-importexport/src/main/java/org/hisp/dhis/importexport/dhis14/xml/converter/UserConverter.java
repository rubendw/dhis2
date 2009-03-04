package org.hisp.dhis.importexport.dhis14.xml.converter;

import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: UserConverter.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class UserConverter
    implements XMLConverter
{
    private static final String ELEMENT_NAME = "UserName";
    
    private static final String FIELD_ID = "UserID";
    private static final String FIELD_NAME = "UserName";
    private static final String FIELD_INFO_ROLE = "InfoRoleID";
    private static final String FIELD_SELECTED = "Selected";

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */    
    public UserConverter()
    {  
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------
    
    public void write( XMLWriter writer, ExportParams params )
    {
        writer.openElement( ELEMENT_NAME );
        
        writer.writeElement( FIELD_ID, String.valueOf( 1 ) );
        writer.writeElement( FIELD_NAME, "DHIS 2 Export" );
        writer.writeElement( FIELD_INFO_ROLE, String.valueOf( 1 ) );
        writer.writeElement( FIELD_SELECTED, String.valueOf( 1 ) );
        
        writer.closeElement( ELEMENT_NAME );
    }    

    public void read( XMLReader reader, ImportParams params )
    {
        // Not implemented        
    }
}
