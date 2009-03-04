package org.hisp.dhis.gis.ext;

public class Title
{
    private String name;

    private String startDate;

    private String endDate;

    public Title( String name, String startDate, String endDate )
    {
        super();
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Title()
    {
        // TODO Auto-generated constructor stub
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate( String startDate )
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }

}