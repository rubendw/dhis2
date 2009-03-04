package org.hisp.dhis.aggregation;

import java.io.Serializable;

public class DataElementCategoryOptionComboName
    implements Serializable
{
    private int id;
    
    private int categoryOptionComboId;
    
    private String categoryOptionComboName;

    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    public DataElementCategoryOptionComboName()
    {   
    }

    public DataElementCategoryOptionComboName( int id, String name )
    {
        this.categoryOptionComboId = id;
        this.categoryOptionComboName = name;
    }
    
    // ----------------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getCategoryOptionComboId()
    {
        return categoryOptionComboId;
    }

    public void setCategoryOptionComboId( int categoryOptionComboId )
    {
        this.categoryOptionComboId = categoryOptionComboId;
    }

    public String getCategoryOptionComboName()
    {
        return categoryOptionComboName;
    }

    public void setCategoryOptionComboName( String categoryOptionComboName )
    {
        this.categoryOptionComboName = categoryOptionComboName;
    }
}
