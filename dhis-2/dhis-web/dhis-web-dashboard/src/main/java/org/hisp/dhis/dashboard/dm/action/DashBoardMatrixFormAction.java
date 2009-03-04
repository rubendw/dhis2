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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.dashboard.util.PeriodStartDateComparator;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorService;
import org.hisp.dhis.indicator.comparator.IndicatorGroupNameComparator;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupService;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodStore;
import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetService;

import com.opensymphony.xwork.Action;

public class DashBoardMatrixFormAction
    implements Action
{

    // --------------------------------------------------------------------------
    // Dependencies
    // --------------------------------------------------------------------------

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

    private IndicatorService indicatorService;

    public void setIndicatorService( IndicatorService indicatorService )
    {
        this.indicatorService = indicatorService;
    }

    // --------------------------------------------------------------------------
    // Input & Output parameters
    // --------------------------------------------------------------------------
    private final int ALL = 0;

    public int getALL()
    {
        return ALL;
    }

    private OrganisationUnit organisationUnit;

    public OrganisationUnit getOrganisationUnit()
    {
        return organisationUnit;
    }

    private Collection<OrganisationUnit> organisationUnits;

    public Collection<OrganisationUnit> getOrganisationUnits()
    {
        return organisationUnits;
    }

    private Collection<OrganisationUnitGroupSet> organisationUnitGroupSets;
    
    public Collection<OrganisationUnitGroupSet> getOrganisationUnitGroupSets()
    {
        return organisationUnitGroupSets;
    }

    public  void setOrganisationUnitGroupSets(Collection<OrganisationUnitGroupSet> organisationUnitGroupSets)
    {
        this.organisationUnitGroupSets = organisationUnitGroupSets;
    }

    private List<IndicatorGroup> indicatorGroups;

    public List<IndicatorGroup> getIndicatorGroups()
    {
        return indicatorGroups;
    }

    private Collection<Indicator> indicators;

    public Collection<Indicator> getIndicators()
    {
        return indicators;
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

    private Collection<Target> targets;

    public Collection<Target> getTargets()
    {
        return targets;
    }

    private Map<OrganisationUnit, Integer> orgChildren;

    public Map<OrganisationUnit, Integer> getOrgChildren()
    {
        return orgChildren;
    }

    private int childrenSize;

    public int getChildrenSize()
    {
        return childrenSize;
    }

    public String execute()
        throws Exception
    {
        organisationUnits = organisationUnitService.getAllOrganisationUnits();

        organisationUnitGroupSets = organisationUnitGroupService.getAllOrganisationUnitGroupSets();

        Iterator orgUnitIterator = organisationUnits.iterator();

        organisationUnit = new OrganisationUnit();
        
        orgChildren = new HashMap<OrganisationUnit, Integer>();

        while ( orgUnitIterator.hasNext() )
        {
            organisationUnit = (OrganisationUnit) orgUnitIterator.next();

            childrenSize = organisationUnit.getChildren().size();
            
            orgChildren.put( organisationUnit, childrenSize );
        }

        // organisationUnitGroups =
        // organisationUnitGroupService.getAllOrganisationUnitGroups();

        indicatorGroups = new ArrayList<IndicatorGroup>( indicatorService.getAllIndicatorGroups() );

        Collections.sort( indicatorGroups, new IndicatorGroupNameComparator() );

        indicators = indicatorService.getAllIndicators();

        // targets = targetService.getAllTargets();

        monthlyPeriods = new ArrayList<Period>( periodStore.getPeriodsByPeriodType( new MonthlyPeriodType() ) );
        Collections.sort( monthlyPeriods, new PeriodStartDateComparator() );
        simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        return SUCCESS;
    }

}
