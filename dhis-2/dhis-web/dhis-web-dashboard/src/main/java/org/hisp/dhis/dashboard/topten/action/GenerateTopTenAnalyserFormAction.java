package org.hisp.dhis.dashboard.topten.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hisp.dhis.dashboard.util.PeriodStartDateComparator;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;

import com.opensymphony.xwork.ActionSupport;

public class GenerateTopTenAnalyserFormAction extends ActionSupport
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    private DataSetService dataSetService;
    
    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }
    
    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------
    private Collection<OrganisationUnit> organisationUnits;

    public Collection<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private Collection<DataSet> dataSetList;

    public Collection<DataSet> getDataSetList()
    {
        return dataSetList;
    }

    private List<Period> monthlyPeriods;

    public List<Period> getMonthlyPeriods()
    {
        return monthlyPeriods;
    }

    private SimpleDateFormat simpleDateFormat;

    public SimpleDateFormat getSimpleDateFormat()
    {
        return simpleDateFormat;
    }

    public String execute() throws Exception
    {
        /* OrganisationUnit */
        organisationUnits = organisationUnitService.getAllOrganisationUnits();

        /* DataSet List */
        dataSetList = dataSetService.getAllDataSets();

        /* Monthly Periods */
        monthlyPeriods = new ArrayList<Period>( periodStore.getPeriodsByPeriodType( new MonthlyPeriodType() ) );
        Collections.sort( monthlyPeriods, new PeriodStartDateComparator() );
        simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        return SUCCESS;
    }
       
}
