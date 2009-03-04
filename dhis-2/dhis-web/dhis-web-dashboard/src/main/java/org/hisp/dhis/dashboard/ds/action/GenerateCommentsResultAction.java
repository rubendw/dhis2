package org.hisp.dhis.dashboard.ds.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hisp.dhis.dashboard.util.DBConnection;
import org.hisp.dhis.dashboard.util.DashBoardService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetStore;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitShortNameComparator;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;

import com.opensymphony.xwork.Action;

public class GenerateCommentsResultAction implements Action
{

    // ---------------------------------------------------------------
    // Dependencies
    // ---------------------------------------------------------------
    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
    }

    public OrganisationUnitService getOrganisationUnitService()
    {
        return organisationUnitService;
    }
    
    private OrganisationUnitGroupService organisationUnitGroupService;

    public void setOrganisationUnitGroupService( OrganisationUnitGroupService organisationUnitGroupService )
    {
        this.organisationUnitGroupService = organisationUnitGroupService;
    }

    private PeriodStore periodStore;

    public void setPeriodStore( PeriodStore periodStore )
    {
        this.periodStore = periodStore;
    }

    private DataSetStore dataSetStore;

    public void setDataSetStore( DataSetStore dataSetStore )
    {
        this.dataSetStore = dataSetStore;
    }

    public DataSetStore getDataSetStore()
    {
        return dataSetStore;
    }

    private DashBoardService dashBoardService;

    public void setDashBoardService( DashBoardService dashBoardService )
    {
        this.dashBoardService = dashBoardService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataElementService dataElementService;
    
    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    // ---------------------------------------------------------------
    // Output Parameters
    // ---------------------------------------------------------------

    private List<OrganisationUnit> orgUnitList;

    public List<OrganisationUnit> getOrgUnitList()
    {
        return orgUnitList;
    }

    private List<DataSet> dataSetList;

    public List<DataSet> getDataSetList()
    {
        return dataSetList;
    }

    private List<Integer> results;

    public List<Integer> getResults()
    {
        return results;
    }

    private Map<DataSet, Map<OrganisationUnit, List<Integer>>> dataStatusResult;

    public Map<DataSet, Map<OrganisationUnit, List<Integer>>> getDataStatusResult()
    {
        return dataStatusResult;
    }

    private Map<DataSet, Collection<Period>> dataSetPeriods;

    public Map<DataSet, Collection<Period>> getDataSetPeriods()
    {
        return dataSetPeriods;
    }

    List<Period> selectedPeriodList;

    public List<Period> getSelectedPeriodList()
    {
        return selectedPeriodList;
    }

    List<String> levelNames;

    public List<String> getLevelNames()
    {
        return levelNames;
    }

    private int maxOULevel;
    
    public int getMaxOULevel()
    {
        return maxOULevel;
    }

    
    // ---------------------------------------------------------------
    // Input Parameters
    // ---------------------------------------------------------------

    private String dsId;

    public void setDsId( String dsId )
    {
        this.dsId = dsId;
    }

    private String ouId;
    public void setOuId( String ouId )
    {
        this.ouId = ouId;
    }

    private String immChildOption;

    public void setImmChildOption( String immChildOption )
    {
        this.immChildOption = immChildOption;
    }

    private int sDateLB;

    public void setSDateLB( int dateLB )
    {
        sDateLB = dateLB;
    }

    public int getSDateLB()
    {
        return sDateLB;
    }
    
    private int eDateLB;

    public void setEDateLB( int dateLB )
    {
        eDateLB = dateLB;
    }

    public int getEDateLB()
    {
        return eDateLB;
    }

    private String facilityLB;

    public void setFacilityLB( String facilityLB )
    {
        this.facilityLB = facilityLB;
    }

    private List<String> orgUnitListCB;

    public void setOrgUnitListCB( List<String> orgUnitListCB )
    {
        this.orgUnitListCB = orgUnitListCB;
    }

    private List<String> selectedDataSets;

    public void setSelectedDataSets( List<String> selectedDataSets )
    {
        this.selectedDataSets = selectedDataSets;
    }

    public List<String> getSelectedDataSets()
    {
        return selectedDataSets;
    }

    private int minOULevel;
            
    public int getMinOULevel()
    {
        return minOULevel;
    }

    private Connection con = null;
    String orgUnitInfo;
    String periodInfo;
    String deInfo;
    int orgUnitCount;
    private String dataTableName;
    // ---------------------------------------------------------------
    // Action Implementation
    // ---------------------------------------------------------------
    public String execute()
        throws Exception
    {
        con = (new DBConnection()).openConnection();
        orgUnitCount = 0;
        dataTableName = "";
        
        // Intialization
        results = new ArrayList<Integer>();
        maxOULevel = 1;
        minOULevel = organisationUnitService.getNumberOfOrganisationalLevels();
        
        
        if(immChildOption!= null && immChildOption.equalsIgnoreCase( "yes" ))
        {
            orgUnitListCB = new ArrayList<String>();
            orgUnitListCB.add( ouId );
            
            facilityLB = "immChildren";
            
            selectedDataSets = new ArrayList<String>();
            selectedDataSets.add( dsId );
        }
        
        // OrgUnit Related Info
        OrganisationUnit selectedOrgUnit = new OrganisationUnit();
        orgUnitList = new ArrayList<OrganisationUnit>();
        if ( facilityLB.equals( "children" ) )
        {
            selectedOrgUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitListCB.get( 0 ) ) );            
            orgUnitList = getChildOrgUnitTree( selectedOrgUnit );
        }
        else if ( facilityLB.equals( "immChildren" ) )
        {
            selectedOrgUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitListCB.get( 0 ) ) );

            // This is hard coded to get District and Corporation List
            if(organisationUnitService.getLevelOfOrganisationUnit( selectedOrgUnit ) == 1)
            {                
                orgUnitList = new ArrayList<OrganisationUnit>();            
                orgUnitList.add( selectedOrgUnit );
                
                OrganisationUnitGroup oug = organisationUnitGroupService.getOrganisationUnitGroupByName( "Districts" );                
                if(oug != null)
                {
                    List<OrganisationUnit> tempOUList = new ArrayList<OrganisationUnit>(oug.getMembers());
                    Collections.sort( tempOUList, new OrganisationUnitShortNameComparator() );
                    orgUnitList.addAll( tempOUList );
                }   
                
                oug = organisationUnitGroupService.getOrganisationUnitGroupByName( "Corporations" );
                if(oug != null)
                {
                    List<OrganisationUnit> tempOUList = new ArrayList<OrganisationUnit>(oug.getMembers());
                    Collections.sort( tempOUList, new OrganisationUnitShortNameComparator() );
                    orgUnitList.addAll( tempOUList );
                }   
            }
            else
            {
                orgUnitList = new ArrayList<OrganisationUnit>();
                
                Iterator<String> orgUnitIterator = orgUnitListCB.iterator();                
                while ( orgUnitIterator.hasNext() )
                {
                    OrganisationUnit o = organisationUnitService.getOrganisationUnit( Integer.parseInt( (String) orgUnitIterator.next() ) );
                    orgUnitList.add( o );
                    List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>( o.getChildren() );
                    Collections.sort( organisationUnits, new OrganisationUnitShortNameComparator() );
                    orgUnitList.addAll( organisationUnits );
                }                
            }
        }
        else
        {
            Iterator<String> orgUnitIterator = orgUnitListCB.iterator();
            OrganisationUnit o;
            while ( orgUnitIterator.hasNext() )
            {
                o = organisationUnitService.getOrganisationUnit( Integer
                    .parseInt( (String) orgUnitIterator.next() ) );
                orgUnitList.add( o );
            }
        }

        orgUnitInfo = "-1";
        for(OrganisationUnit ou : orgUnitList)
        {
            orgUnitInfo += "," + ou.getId();
            getOrgUnitInfo(ou);
        }    
            
        // Period Related Info
        Period startPeriod = periodStore.getPeriod( sDateLB );
        Period endPeriod = periodStore.getPeriod( eDateLB );

        selectedPeriodList = dashBoardService.getMonthlyPeriods( startPeriod.getStartDate(), endPeriod.getEndDate() );
        
        periodInfo = "-1";
        for(Period p : selectedPeriodList)            
            periodInfo +=  "," + p.getId();
        
        // DataSet Related Info
        dataSetList = new ArrayList<DataSet>();

        deInfo = "-1";
        for(String ds : selectedDataSets)
        {
            DataSet dSet = dataSetStore.getDataSet( Integer.parseInt( ds ) );
            for(DataElement de : dSet.getDataElements())
                deInfo += "," + de.getId();
        }

        System.out.println(orgUnitInfo);
        System.out.println(deInfo);
        System.out.println(periodInfo);
        
        dataTableName = dashBoardService.createDataTableForComments(orgUnitInfo, deInfo, periodInfo);
        
        dataSetPeriods = new HashMap<DataSet, Collection<Period>>();
        Iterator dataSetIterator = selectedDataSets.iterator();
        
        DataSet ds;
        Collection<DataElement> dataElements = new ArrayList<DataElement>();
        PeriodType dataSetPeriodType;
        Collection<Period> periodList = new ArrayList<Period>();
        
        while ( dataSetIterator.hasNext() )
        {            
            ds = dataSetStore.getDataSet( Integer.parseInt( (String) dataSetIterator.next() ) );
            dataSetList.add( ds );
            dataElements = ds.getDataElements();
            deInfo = getDEInfo(dataElements);
                        
            dataSetPeriodType = ds.getPeriodType();
            
            periodList = periodStore.getIntersectingPeriodsByPeriodType( dataSetPeriodType,
                startPeriod.getStartDate(), endPeriod.getEndDate() );
            dataSetPeriods.put( ds, periodList );

            Iterator orgUnitListIterator = orgUnitList.iterator();
            OrganisationUnit o;
            Set<Source> dso = new HashSet<Source>();
            Iterator periodIterator;
            
            while ( orgUnitListIterator.hasNext() )
            {
                
                o = (OrganisationUnit) orgUnitListIterator.next();
                orgUnitInfo = ""+o.getId();
                
                if(maxOULevel < organisationUnitService.getLevelOfOrganisationUnit( o ))
                    maxOULevel = organisationUnitService.getLevelOfOrganisationUnit( o );
                
                if(minOULevel > organisationUnitService.getLevelOfOrganisationUnit( o ))
                    minOULevel = organisationUnitService.getLevelOfOrganisationUnit( o );
                    
                dso = ds.getSources();
                periodIterator = periodList.iterator();
                
                Period p;
                Collection dataValueResult;
                double dataStatusPercentatge;
                
                while ( periodIterator.hasNext() )
                {                    
                    p = (Period) periodIterator.next();
                    periodInfo = ""+p.getId();
                    
                    PreparedStatement ps1 = null;
                    ResultSet rs1 = null;
                    String query = "";
                                                                               
                    if ( dso == null )
                    {
                        results.add( new Integer( -1 ) );
                        continue;
                    }
                    else if(!dso.contains( o ))
                    {                        
                        orgUnitInfo = "-1";
                        orgUnitCount = 0;
                        getOrgUnitInfo(o, dso);
                        
                        query = "SELECT sourceid, dataelementid, periodid, comment FROM "+ dataTableName +" WHERE dataelementid IN ("+deInfo+") AND sourceid IN ("+orgUnitInfo+") AND periodid IN ("+periodInfo+")";
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery(query);
                        
                        while(rs1.next())
                        {
                            OrganisationUnit ou = organisationUnitService.getOrganisationUnit( rs1.getInt( 1 ) );
                            DataElement de = dataElementService.getDataElement( rs1.getInt( 2 ));
                            Period per = periodStore.getPeriod( rs1.getInt(3) );
                            
                            String tempStr = ou.getShortName() + " --- " + de.getName(); 
                        }                            
                        
                        
                        //results.add( new Integer( (int) dataStatusPercentatge ) );
                        continue;
                    }
                    
                    /*
                    dataValueResult = dataValueService.getDataValues( o, p, dataElements );
                    dataStatusPercentatge = ((double) dataValueResult.size() / (double) dataElements.size()) * 100.0;
                 
                    */
                    
                    orgUnitInfo = ""+ o.getId();
                    query = "SELECT COUNT(*) FROM "+ dataTableName +" WHERE dataelementid IN ("+deInfo+") AND sourceid IN ("+orgUnitInfo+") AND periodid IN ("+periodInfo+")";
                    ps1 = con.prepareStatement(query);
                    rs1 = ps1.executeQuery(query);

                    if(rs1.next())
                    {
                        try
                        {
                            dataStatusPercentatge = ((double) rs1.getInt( 1 ) / (double) dataElements.size())*100.0;    
                        }
                        catch(Exception e)
                        {
                            dataStatusPercentatge = 0.0;
                        }
                    }                            
                    else
                        dataStatusPercentatge = 0.0;
                   
                    if(dataStatusPercentatge > 100.0) dataStatusPercentatge = 100;
                    
                    dataStatusPercentatge = Math.round( dataStatusPercentatge * Math.pow( 10, 0 ) ) / Math.pow( 10, 0 );                    

                    results.add( new Integer( (int) dataStatusPercentatge ) );
                    
                    try
                    {
                        
                    }
                    finally
                    {
                        try
                        {
                            if(rs1 != null) rs1.close();
                            if(ps1 != null) ps1.close();
                        }
                        catch( Exception e )
                        {
                            System.out.println("Exception while closing Prepared Statement, ResultSet");
                        }
                    }              

                    
                }
                // System.out.println( o.getShortName() );
            }
        }

        // For Level Names
        String ouLevelNames[] = { " ", "State", "District", "Block", "PHC", "Subcentre"};
        levelNames = new ArrayList<String>();
        int count1 = minOULevel;
        while ( count1 <= maxOULevel )
        {
            levelNames.add( ouLevelNames[count1] );
            count1++;
        }

        try
        {
            
        }
        finally
        {
            try
            {
                if(con != null) con.close();
            }
            catch( Exception e )
            {
                System.out.println("Exception while closing DB Connections : "+e.getMessage());
            }
        }// finally block end

        dashBoardService.deleteDataTable( dataTableName );
        
        return SUCCESS;
    }

    // Returns the OrgUnitTree for which Root is the orgUnit
    public List<OrganisationUnit> getChildOrgUnitTree( OrganisationUnit orgUnit )
    {
        List<OrganisationUnit> orgUnitTree = new ArrayList<OrganisationUnit>();
        orgUnitTree.add( orgUnit );

        Collection<OrganisationUnit> children = orgUnit.getChildren();

        Iterator childIterator = children.iterator();
        OrganisationUnit child;
        while ( childIterator.hasNext() )
        {
            child = (OrganisationUnit) childIterator.next();
            orgUnitTree.addAll( getChildOrgUnitTree( child ) );
        }
        return orgUnitTree;
    }// getChildOrgUnitTree end


    private void getOrgUnitInfo(OrganisationUnit organisationUnit)
    {                
        Collection<OrganisationUnit> children = organisationUnit.getChildren();

        Iterator<OrganisationUnit> childIterator = children.iterator();
        OrganisationUnit child;
        while ( childIterator.hasNext() )
        {
            child = (OrganisationUnit) childIterator.next();
            orgUnitInfo += "," + child.getId();
            getOrgUnitInfo( child);
        }               
    }

    private void getOrgUnitInfo(OrganisationUnit organisationUnit, Set<Source> dso)
    {                
        Collection<OrganisationUnit> children = organisationUnit.getChildren();

        Iterator<OrganisationUnit> childIterator = children.iterator();
        OrganisationUnit child;
        while ( childIterator.hasNext() )
        {
            child = (OrganisationUnit) childIterator.next();
            if(dso.contains( child ))
            {
                orgUnitInfo += "," + child.getId();
                orgUnitCount++;
            }            
            getOrgUnitInfo( child, dso );
        }               
    }

    
    private String getDEInfo(Collection<DataElement> dataElements)
    {
        String deInfo = "-1";
                        
        for(DataElement de : dataElements)
        {
               deInfo += "," + de.getId();
        }
        return deInfo;
    }


    
}// class end
