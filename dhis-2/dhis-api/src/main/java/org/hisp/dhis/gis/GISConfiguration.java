package org.hisp.dhis.gis;

public class GISConfiguration
{
    public static final String AggregationService = "aggregation_service";
    
    public static final String AggregatedIndicatorValue  = "aggregated_indicator_value";
    
    public static final String KEY_DIRECTORY = "directory";
    
    public static final String KEY_GETINDICATOR = "getindicator";
    
    private int id;
    
    private String key;
    
    private String value;    

    public GISConfiguration( String key, String value )
    {
        super();
        this.key = key;
        this.value = value;
    }


    public String getKey()
    {
        return key;
    }


    public void setKey( String key )
    {
        this.key = key;
    }


    public String getValue()
    {
        return value;
    }


    public void setValue( String value )
    {
        this.value = value;
    }


    public GISConfiguration()
    {
        super();
    }

  
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

   
    
    
}
