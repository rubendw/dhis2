package org.hisp.dhis.dashboard.dm.action;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.aggregation.AggregationService;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.organisationunit.comparator.OrganisationUnitShortNameComparator;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.source.SourceStore;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetService;

import com.opensymphony.xwork.Action;

public class DashBoardMatrixResultAction
    implements Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private OrganisationUnitService organisationUnitService;

    public void setOrganisationUnitService( OrganisationUnitService organisationUnitService )
    {
        this.organisationUnitService = organisationUnitService;
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

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    private TargetService targetService;

    public void setTargetService( TargetService targetService )
    {
        this.targetService = targetService;
    }

    private AggregationService aggregationService;

    public void setAggregationService( AggregationService aggregationService )
    {
        this.aggregationService = aggregationService;
    }

    private SourceStore sourceStore;

    public void setSourceStore( SourceStore sourceStore )
    {
        this.sourceStore = sourceStore;
    }

    // -------------------------------------------------------------------
    // Input Parameters
    // -------------------------------------------------------------------

    private int sDateLB;

    public void setSDateLB( int dateLB )
    {
        sDateLB = dateLB;
    }

    private String ougSetCB;

    public void setOugSetCB( String ougSetCB )
    {
        this.ougSetCB = ougSetCB;
    }

    private List<String> orgUnitListCB;

    public void setOrgUnitListCB( List<String> orgUnitListCB )
    {
        this.orgUnitListCB = orgUnitListCB;
    }

    private int eDateLB;

    public void setEDateLB( int dateLB )
    {
        eDateLB = dateLB;
    }

    private String ouNameTB;

    public String getOuNameTB()
    {
        return ouNameTB;
    }

    private Integer ouLevel;

    public Integer getOuLevel()
    {
        return ouLevel;
    }

    private String selectedGroup;

    public String getSelectedGroup()
    {
        return selectedGroup;
    }

    public void setSelectedGroup( String selectedGroup )
    {
        this.selectedGroup = selectedGroup;
    }

    // -----------------------------------------------------------------
    // Output parameters
    // -----------------------------------------------------------------

    private List<String> selectedIndicators;

    public List<String> getSelectedIndicators()
    {
        return selectedIndicators;
    }

    public void setSelectedIndicators( List<String> selectedIndicators )
    {
        this.selectedIndicators = selectedIndicators;
    }

    private List<String> indicatorNames;

    public List<String> getIndicatorNames()
    {
        return indicatorNames;
    }

    private Indicator indicator;

    public Indicator getIndicator()
    {
        return indicator;
    }

    private List<Indicator> indicators;

    public List<Indicator> getIndicators()
    {
        return indicators;
    }

    private String indicatorName;

    public String getIndicatorName()
    {
        return indicatorName;
    }

    private ArrayList<OrganisationUnit> orgUnitList;

    public ArrayList<OrganisationUnit> getOrgUnitList()
    {
        return orgUnitList;
    }

    private OrganisationUnitGroup organisationUnitGroup;

    public void setOrganisationUnitGroup( OrganisationUnitGroup organisationUnitGroup )
    {
        this.organisationUnitGroup = organisationUnitGroup;
    }

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private Collection<OrganisationUnit> corporations;

    public Collection<OrganisationUnit> getCorporations()
    {
        return corporations;
    }

    private Source source;

    public Source getSource()
    {
        return source;
    }

    private Period period;

    public Period getPeriod()
    {
        return period;
    }

    private PeriodType periodType;

    public PeriodType getPeriodType()
    {
        return periodType;
    }

    private Period startPeriod;

    public Period getStartPeriod()
    {

        return startPeriod;
    }

    private Period endPeriod;

    public Period getEndPeriod()
    {
        return endPeriod;
    }

    private Target target;

    public Target getTarget()
    {
        return target;
    }

    private List<Target> targets;

    public List<Target> getTargets()
    {
        return targets;
    }

    private String minMaxOrgUnit;

    public String getMinMaxOrganisationUnit()
    {
        return minMaxOrgUnit;
    }

    private List<Double> collectedIndicatorValues;

    public List<Double> getCollectedIndicatorValues()
    {
        return collectedIndicatorValues;
    }

    private Double indicatorValueResult;

    public Double getIndicatorValueResult()
    {
        return indicatorValueResult;
    }

    private String minMaxArray[][];

    public String[][] getMinMaxArray()
    {
        return minMaxArray;
    }

    private Double indicatorValue;

    public Double getIndicatorValue()
    {
        return indicatorValue;
    }

    private List<String> minValues;

    public List<String> getMinValues()
    {
        return minValues;
    }

    private List<String> maxValues;

    public List<String> getMaxValues()
    {
        return maxValues;
    }

    private List<String> achievementRates;

    public List<String> getAchievementRates()
    {
        return achievementRates;
    }

    private List<Double> targetValues;

    public List<Double> getTargetValues()
    {
        return targetValues;
    }

    private String achievementRate;

    public String getAchievementRate()
    {
        return achievementRate;
    }

    private Double targetValue;

    public Double getTargetValue()
    {
        return targetValue;
    }

    private String minValue;

    public String getMinValue()
    {
        return minValue;

    }

    private String maxValue;

    public String getMaxValue()
    {
        return maxValue;
    }

    private String value;

    public String getValue()
    {
        return value;
    }

    private String minOrgUnit;

    public String getMinOrgUnit()
    {
        return minOrgUnit;
    }

    private String maxOrgUnit;

    public String getMaxOrgUnit()
    {
        return maxOrgUnit;
    }

    private String message;

    public String getMessage()
    {
        return message;
    }

    private Collection<OrganisationUnitGroup> orgUnitGroups;

    public Collection<OrganisationUnitGroup> getOrgUnitGroups()
    {
        return orgUnitGroups;
    }

    private Integer counter;

    private Integer ougSetControl;

    public Integer getOugSetControl()
    {
        return ougSetControl;
    }

    // ------------------------------------------------------------------
    // Action implementation
    // ------------------------------------------------------------------

    @SuppressWarnings( { "unchecked", "static-access" } )
    public String execute()
        throws Exception
    {

        organisationUnit = new OrganisationUnit();

        organisationUnitGroup = new OrganisationUnitGroup();

        orgUnitList = new ArrayList<OrganisationUnit>();

        orgUnitGroups = new ArrayList<OrganisationUnitGroup>();

        counter = 0;

        ougSetControl = 0;

        if ( ougSetCB == null )
        {
            ougSetControl = 0;

            organisationUnit = organisationUnitService.getOrganisationUnit( Integer.parseInt( orgUnitListCB.get( 0 ) ) );

            ouLevel = organisationUnitService.getLevelOfOrganisationUnit( organisationUnit );
            
            Collection orgList = new ArrayList<OrganisationUnit>();

            orgList = organisationUnit.getChildren();

            organisationUnitGroup = new OrganisationUnitGroup();

            if ( orgList == null || orgList.isEmpty() || (orgList.size() == 0) )
            {
                message = "Please select an Organisation Unit with Children";

                return INPUT;
            }

            else
            {
                if ( selectedGroup == null || selectedGroup == "" )
                {
                    organisationUnitGroup = organisationUnitGroupService
                        .getOrganisationUnitGroupByName( "Corporations" );

                    corporations = new ArrayList<OrganisationUnit>();

                    corporations = organisationUnitGroup.getMembers();

                    Iterator corpIterator = corporations.iterator();

                    while ( corpIterator.hasNext() )
                    {
                        OrganisationUnit currentCorp = (OrganisationUnit) corpIterator.next();

                        if ( orgList.contains( currentCorp ) )
                        {
                            orgList.remove( currentCorp );
                        }
                    }

                    orgUnitList.addAll( orgList );
                }

                else
                {
                    if ( selectedGroup.equalsIgnoreCase( "corporations" ) && (ouLevel == 1) )
                    {
                        organisationUnitGroup = organisationUnitGroupService
                            .getOrganisationUnitGroupByName( "Corporations" );

                        corporations = new ArrayList<OrganisationUnit>();

                        corporations = organisationUnitGroup.getMembers();

                        orgUnitList.addAll( corporations );

                    }

                    else if ( selectedGroup.equalsIgnoreCase( "corporations" ) && (ouLevel != 1) )
                    {
                        List<OrganisationUnit> rootOrgUnits = new ArrayList<OrganisationUnit>( organisationUnitService
                            .getRootOrganisationUnits() );

                        OrganisationUnit topOrgUnit = new OrganisationUnit();

                        topOrgUnit = rootOrgUnits.get( 0 );

                        message = "Please select " + topOrgUnit.getShortName() + " or select another group";

                        return INPUT;

                    }

                    else
                    {
                        organisationUnitGroup = organisationUnitGroupService
                            .getOrganisationUnitGroupByName( "Corporations" );

                        corporations = new ArrayList<OrganisationUnit>();

                        corporations = organisationUnitGroup.getMembers();

                        Iterator corpIterator = corporations.iterator();

                        while ( corpIterator.hasNext() )
                        {
                            OrganisationUnit currentCorp = (OrganisationUnit) corpIterator.next();

                            if ( orgList.contains( currentCorp ) )
                            {
                                orgList.remove( currentCorp );
                            }
                        }

                        orgUnitList.addAll( orgList );
                    }

                }

            }

            ouLevel = organisationUnitService.getLevelOfOrganisationUnit( organisationUnit );

            Collections.sort( orgUnitList, new OrganisationUnitShortNameComparator() );

        }

        else
        {
            ougSetControl = 1;

            Iterator cbIterator = orgUnitListCB.iterator();

            while ( cbIterator.hasNext() )
            {
                String cb = (String) cbIterator.next();

                OrganisationUnitGroup og = new OrganisationUnitGroup();

                og = organisationUnitGroupService.getOrganisationUnitGroup( Integer.parseInt( cb ) );

                orgUnitGroups.add( og );

            }

        }

        indicatorName = new String();

        indicatorNames = new ArrayList<String>();

        minValues = new ArrayList<String>();

        maxValues = new ArrayList<String>();
        
        targetValues = new ArrayList<Double>();
        
        targetValue = 0.0;

        collectedIndicatorValues = new ArrayList<Double>();

        startPeriod = periodStore.getPeriod( sDateLB );

        endPeriod = periodStore.getPeriod( eDateLB );

        indicators = new ArrayList<Indicator>();

        Iterator indiIterator = selectedIndicators.iterator();

        while ( indiIterator.hasNext() )
        {
            String str = (String) indiIterator.next();

            Indicator ind = new Indicator();

            ind = indicatorService.getIndicator( Integer.parseInt( str ) );

            indicators.add( ind );
        }

        Date startDate = startPeriod.getStartDate();

        Date endDate = endPeriod.getEndDate();

        int i;

        if ( ougSetCB == null )
        {

            Iterator indicatorsIterator = selectedIndicators.iterator();
            
            targetValue = 0.0;

            indicatorName = new String();

            indicatorNames = new ArrayList<String>();

            minValues = new ArrayList<String>();

            maxValues = new ArrayList<String>();

            targetValues = new ArrayList<Double>();

            collectedIndicatorValues = new ArrayList<Double>();

            while ( indicatorsIterator.hasNext() )
            {

                i = 0;

                indicator = indicatorService.getIndicator( Integer.parseInt( (String) indicatorsIterator.next() ) );

                indicatorNames.add( indicator.getName() );

                period = periodStore.getPeriod( startDate, endDate, PeriodType.getPeriodTypeByName( "monthly" ) );

                targets = new ArrayList<Target>();

                List<Period> tempPeriodList = new ArrayList<Period>( periodStore
                    .getIntersectingPeriods( startDate, endDate ) );
                Iterator tempPeriodListIterator = tempPeriodList.iterator();
                while ( tempPeriodListIterator.hasNext() )
                {
                    Period tempPeriod = (Period) tempPeriodListIterator.next();
                    List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( indicator, tempPeriod,
                        organisationUnit ) );
                    if ( tempTarget != null )
                        targets.addAll( tempTarget );
                }
                
                if (targets != null && targets.size() > 0)
                {
                    targetValue = targets.get( 0 ).getValue();
                }

                else
                {
                    targetValue = 0.0;
                }

                targetValue = Math.round( targetValue * Math.pow( 10, 0 ) ) / Math.pow( 10, 0 );

                if ( targetValue == -1 )
                {
                    targetValue = 0.0;
                }

                targetValues.add(targetValue);
                
                if ( organisationUnit != null )
                {
                    indicatorValue = aggregationService.getAggregatedIndicatorValue( indicator, startDate, endDate,
                        organisationUnit );

                }

                else
                {
                    indicatorValue = aggregationService.getAggregatedIndicatorValue( indicator, startDate, endDate,
                        organisationUnit );

                }

                indicatorValue = Math.round( indicatorValue * Math.pow( 10, 0 ) ) / Math.pow( 10, 0 );

                if ( indicatorValue == -1 )
                {
                    indicatorValue = 0.0;
                }

                collectedIndicatorValues.add( indicatorValue );

                Iterator organisationUnitListIterator = orgUnitList.iterator();

                minMaxArray = new String[orgUnitList.size()][2];

                while ( organisationUnitListIterator.hasNext() )
                {

                    OrganisationUnit o = (OrganisationUnit) organisationUnitListIterator.next();

                    indicatorValueResult = aggregationService.getAggregatedIndicatorValue( indicator, startDate,
                        endDate, o );

                    indicatorValueResult = Math.round( indicatorValueResult * Math.pow( 10, 0 ) ) / Math.pow( 10, 0 );

                    if ( indicatorValueResult == -1 )
                    {
                        indicatorValueResult = 0.0;
                    }

                    minMaxOrgUnit = (String) o.getShortName();

                    minMaxArray[i][0] = String.valueOf( 0 );

                    minMaxArray[i][1] = String.valueOf( 0 );

                    minMaxArray[i][0] = String.valueOf( indicatorValueResult );

                    minMaxArray[i][1] = minMaxOrgUnit;

                    i++;

                }

                minMaxSort( minMaxArray );

                String minValue = minMaxArray[0][0] + " ";

                String maxValue = minMaxArray[orgUnitList.size() - 1][0] + " ";

                int count = 4;

                if (selectedGroup != null && selectedGroup.equalsIgnoreCase( "corporations" ))
                {
                    count = 3;
                }
                
                else
                {
                    count = 4;
                }

                for ( int q = 0; q < minMaxArray.length; q++ )
                {
                    String currentValue = minMaxArray[q][0];

                    String currentOrgUnit = minMaxArray[q][1];

                    if ( currentValue.equalsIgnoreCase( minMaxArray[0][0] ) )
                    {
                        if ( q == 0 || q % count == 0 )
                        {
                            minValue += "<br>";
                        }

                        if ( q == minMaxArray.length - 1 )
                        {
                            minValue += currentOrgUnit;
                        }

                        else
                        {
                            minValue += currentOrgUnit + ", ";
                        }

                    }

                    if ( currentValue.equalsIgnoreCase( minMaxArray[orgUnitList.size() - 1][0] ) )
                    {
                        if ( q == 0 || q % count == 0 )
                        {
                            maxValue += "<br>";
                        }

                        if ( q == minMaxArray.length - 1 )
                        {
                            maxValue += currentOrgUnit;
                        }
                        else
                        {
                            maxValue += currentOrgUnit + ", ";
                        }
                    }

                }

                minValues.add( minValue );

                maxValues.add( maxValue );

                counter++;

            }

        }

        else
        {
            OrganisationUnitGroup oGroup = new OrganisationUnitGroup();

            Iterator orgUnitGroupIterator = orgUnitGroups.iterator();

            minMaxArray = new String[orgUnitList.size()][2];

            while ( orgUnitGroupIterator.hasNext() )
            {
                oGroup = (OrganisationUnitGroup) orgUnitGroupIterator.next();

                Iterator indicatorsIterator = selectedIndicators.iterator();

                while ( indicatorsIterator.hasNext() )
                {

                    i = 0;

                    Double indicatorSum = 0.0;

                    indicator = indicatorService.getIndicator( Integer.parseInt( (String) indicatorsIterator.next() ) );

                    indicatorNames.add( indicator.getName() );

                    period = periodStore.getPeriod( startDate, endDate, PeriodType.getPeriodTypeByName( "monthly" ) );

                    /*targets = new ArrayList<Target>();

                    List<Period> tempPeriodList = new ArrayList<Period>( periodStore
                        .getIntersectingPeriods( startDate, endDate ) );
                    Iterator tempPeriodListIterator = tempPeriodList.iterator();
                    while ( tempPeriodListIterator.hasNext() )
                    {
                        Period tempPeriod = (Period) tempPeriodListIterator.next();
                        List<Target> tempTarget = new ArrayList<Target>( targetService.getTargets( indicator, tempPeriod,
                            organisationUnit ) );
                        if ( tempTarget != null )
                            targets.addAll( tempTarget );
                    }
                    
                    if (targets == null)
                    {
                        targetValue = 0.0;
                    }
                    
                    else
                    {
                        target = targets.get(0);
                        
                        targetValue = target.getValue();
                    }

                    targetValue = Math.round( targetValue * Math.pow( 10, 0 ) ) / Math.pow( 10, 0 );

                    if ( targetValue == -1 )
                    {
                        targetValue = 0.0;
                    }

                    targetValues.add(targetValue);*/
                    
                    orgUnitList = new ArrayList<OrganisationUnit>();

                    orgUnitList.addAll( oGroup.getMembers() );

                    Iterator organisationUnitListIterator = orgUnitList.iterator();

                    minMaxArray = new String[orgUnitList.size()][2];

                    while ( organisationUnitListIterator.hasNext() )
                    {

                        OrganisationUnit o = (OrganisationUnit) organisationUnitListIterator.next();

                        indicatorValueResult = aggregationService.getAggregatedIndicatorValue( indicator, startDate,
                            endDate, o );

                        indicatorValueResult = Math.round( indicatorValueResult * Math.pow( 10, 0 ) )
                            / Math.pow( 10, 0 );

                        if ( indicatorValueResult == -1 )
                        {
                            indicatorValueResult = 0.0;
                        }

                        minMaxOrgUnit = (String) o.getShortName();

                        minMaxArray[i][0] = String.valueOf( 0 );

                        minMaxArray[i][1] = String.valueOf( 0 );

                        minMaxArray[i][0] = String.valueOf( indicatorValueResult );

                        minMaxArray[i][1] = minMaxOrgUnit;

                        i++;

                        indicatorSum += indicatorValueResult;

                    }

                    indicatorValue = indicatorSum / orgUnitList.size();

                    indicatorValue = Math.round( indicatorValue * Math.pow( 10, 0 ) ) / Math.pow( 10, 0 );

                    collectedIndicatorValues.add( indicatorValue );

                    minMaxSort( minMaxArray );

                    String minValue = minMaxArray[0][0] + " ";

                    String maxValue = minMaxArray[orgUnitList.size() - 1][0] + " ";

                    int count = 4;

                    for ( int q = 0; q < minMaxArray.length; q++ )
                    {
                        String currentValue = minMaxArray[q][0];

                        String currentOrgUnit = minMaxArray[q][1];

                        if ( currentValue.equalsIgnoreCase( minMaxArray[0][0] ) )
                        {
                            if ( q == 0 || q % count == 0 )
                            {
                                minValue += "<br>";
                            }

                            if ( q == minMaxArray.length - 1 )
                            {
                                minValue += currentOrgUnit;
                            }

                            else
                            {
                                minValue += currentOrgUnit + ", ";
                            }

                        }

                        if ( currentValue.equalsIgnoreCase( minMaxArray[orgUnitList.size() - 1][0] ) )
                        {
                            if ( q == 0 || q % count == 0 )
                            {
                                maxValue += "<br>";
                            }

                            if ( q == minMaxArray.length - 1 )
                            {
                                maxValue += currentOrgUnit;
                            }
                            else
                            {
                                maxValue += currentOrgUnit + ", ";
                            }
                        }

                    }

                    minValues.add( minValue );

                    maxValues.add( maxValue );

                    counter++;

                }

            }

        }

        return SUCCESS;
    }

    public String[][] minMaxSort( String[][] minMaxArray )
    {
        int n = minMaxArray.length;

        boolean doMore = true;

        Double currentValue;

        Double nextValue;

        String currentOrgUnit;

        String nextOrgUnit;

        while ( doMore )
        {
            n--;

            doMore = false;

            for ( int m = 0; m < n; m++ )
            {
                if ( m <= n - 1 )
                {
                    currentValue = Double.valueOf( minMaxArray[m][0] );

                    nextValue = Double.valueOf( minMaxArray[m + 1][0] );

                    currentOrgUnit = minMaxArray[m][1];

                    nextOrgUnit = minMaxArray[m + 1][1];
                }

                else
                {
                    currentValue = Double.valueOf( minMaxArray[m][0] );

                    nextValue = currentValue;

                    currentOrgUnit = minMaxArray[m][1];

                    nextOrgUnit = currentOrgUnit;

                }
                if ( currentValue > nextValue )
                {
                    String tempValue = minMaxArray[m][0];

                    String tempOrgUnit = minMaxArray[m][1];

                    minMaxArray[m][0] = minMaxArray[m + 1][0];

                    minMaxArray[m + 1][0] = tempValue;

                    minMaxArray[m][1] = minMaxArray[m + 1][1];

                    minMaxArray[m + 1][1] = tempOrgUnit;

                    doMore = true;
                }

                if ( currentValue == nextValue )
                {
                    if ( currentOrgUnit.compareTo( nextOrgUnit ) < 0 )
                    {
                        String tempValue = minMaxArray[m][0];

                        String tempOrgUnit = minMaxArray[m][1];

                        minMaxArray[m][0] = minMaxArray[m + 1][0];

                        minMaxArray[m + 1][0] = tempValue;

                        minMaxArray[m][1] = minMaxArray[m + 1][1];

                        minMaxArray[m + 1][1] = tempOrgUnit;

                        doMore = true;

                    }
                }

            }
        }

        return minMaxArray;
    }

}
