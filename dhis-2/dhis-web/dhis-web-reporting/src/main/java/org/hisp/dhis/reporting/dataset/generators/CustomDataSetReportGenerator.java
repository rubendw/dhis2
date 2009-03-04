package org.hisp.dhis.reporting.dataset.generators;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Abyot Asalefew Gizaw
 * @version $Id$
 */
public class CustomDataSetReportGenerator 
{
    /**
     * Puts in aggregated datavalues in the custom dataentry form and returns
     * whole report text.
     */
    public static String prepareReportContent( String dataEntryFormCode, Map<String, String> dataValues )
    {        
        StringBuffer buffer = new StringBuffer();

        Pattern patDataElement = Pattern.compile( "(<input.*?)[/]?>", Pattern.DOTALL );
        Matcher matDataElement = patDataElement.matcher( dataEntryFormCode );

        // ---------------------------------------------------------------------
        // Iterate through all matching data element fields.
        // ---------------------------------------------------------------------
        
        while ( matDataElement.find() )
        {	
            // -----------------------------------------------------------------
            // Get input HTML code
            // -----------------------------------------------------------------
            
            String dataElementCode = matDataElement.group( 1 );
            
            // -----------------------------------------------------------------
            // Pattern to extract data element ID from data element field
            // -----------------------------------------------------------------

            Pattern patDataElementId = Pattern.compile( "value\\[(.*)\\].value:value\\[(.*)\\].value" );
            Matcher matDataElementId = patDataElementId.matcher( dataElementCode );
            
            if ( matDataElementId.find() && matDataElementId.groupCount() > 0 )
            {	
            	// -------------------------------------------------------------
                // Get data element ID of data element.
                // -------------------------------------------------------------
                
            	int dataElementId = Integer.parseInt( matDataElementId.group( 1 ) );                
                    
                int optionComboId = Integer.parseInt( matDataElementId.group( 2 ) ); 
                
               // --------------------------------------------------------------
               // Find existing value of data element in data set.
               // --------------------------------------------------------------               
                
                String dataElementValue = dataValues.get(dataElementId+":"+optionComboId);               
                
                if (dataElementValue == null )
                {
                	dataElementValue = "";
                }
                	
               	dataElementCode = dataElementValue;
               	
               	matDataElement.appendReplacement( buffer, dataElementCode );
            }
        }

        // --------------------------------------------------------------
        // Add remaining text 
        // --------------------------------------------------------------               
        
        matDataElement.appendTail( buffer );
        
        return buffer.toString();
    }
}
