package org.hisp.dhis.importexport.dhis14.xml.typehandler;

import org.hisp.dhis.dataelement.DataElement;

/**
 * @author Lars Helge Overland
 * @version $Id: TypeHandler.java 4646 2008-02-26 14:54:29Z larshelg $
 */
public class TypeHandler
{
    private static final String DHIS14_TRUE = "1";
    private static final String DHIS14_FALSE = "0";
    
    private static final String DHIS14_AGGREGATION_OPERATOR_SUM = "Sum";
    private static final String DHIS14_AGGREGATION_OPERATOR_AVERAGE = "Average";
    private static final String DHIS14_AGGREGATION_OPERATOR_COUNT = "Count";
    

    // -------------------------------------------------------------------------
    // Boolean
    // -------------------------------------------------------------------------
    
    public static String convertBooleanTo( boolean value )
    {
        if ( value )
        {
            return DHIS14_TRUE;
        }
        else
        {
            return DHIS14_FALSE;
        }
    }
    
    public static boolean convertBooleanFrom( String value )
    {
        return value.equals( DHIS14_TRUE );
    }

    // -------------------------------------------------------------------------
    // Aggregation operator
    // -------------------------------------------------------------------------
    
    public static String convertAggregationOperatorTo( String value )
    {
        if ( value.equals( DataElement.AGGREGATION_OPERATOR_SUM ) )
        {
            return DHIS14_AGGREGATION_OPERATOR_SUM;
        }
        else if ( value.equals( DataElement.AGGREGATION_OPERATOR_AVERAGE ) )
        {
            return DHIS14_AGGREGATION_OPERATOR_AVERAGE;
        }
        else if ( value.equals( DataElement.AGGREGATION_OPERATOR_COUNT ) )
        {
            return DHIS14_AGGREGATION_OPERATOR_COUNT;
        }
        
        return new String();
    }
    
    public static String convertAggregationOperatorFrom( String value )
    {
        if ( value.equals( DHIS14_AGGREGATION_OPERATOR_SUM ) )
        {
            return DHIS14_AGGREGATION_OPERATOR_SUM;
        }
        else if ( value.equals( DHIS14_AGGREGATION_OPERATOR_AVERAGE ) )
        {
            return DHIS14_AGGREGATION_OPERATOR_AVERAGE;            
        }
        else if ( value.equals( DHIS14_AGGREGATION_OPERATOR_COUNT ) )
        {
            return DHIS14_AGGREGATION_OPERATOR_COUNT;
        }
        
        return new String();
    }
}
