package org.hisp.dhis.dashboard.util;

public class SurveyData
{

    private String name;
    
    private String value;
    
    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------
    
    public SurveyData()
    {
        
    }
    
    public SurveyData(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
    
    //--------------------------------------------------------------------------
    // Getters & Setters
    //--------------------------------------------------------------------------
    
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }
    
}
