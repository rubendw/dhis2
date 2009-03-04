package org.hisp.dhis.gis.state;

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
import java.util.List;
import java.util.Map;

import org.hisp.dhis.gis.Legend;
import org.hisp.dhis.gis.MapFile;
import org.hisp.dhis.gis.action.export.FeatureStructure;
import org.hisp.dhis.gis.ext.BagSession;
import org.hisp.dhis.gis.ext.Feature;
import org.hisp.dhis.gis.ext.Title;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;

import com.opensymphony.xwork.ActionContext;

/**
 * @author Tran Thanh Tri
 * @version $Id: SelectionManager.java 28-04-2008 16:06:00 $
 */
public class SelectionManager
{

    private static final String MAP_FILE_SELECTED = "map_file_selected";

    private static final String INDICATOR_SELECTED = "indicator_selected";

    private static final String INDICATOR_GROUP_SELECTED = "indicator_group_selected";

    private static final String STARTDATE = "startdate";

    private static final String ENDDATE = "enddate";

    private static final String BAGSESSION = "bag_session"; 
    
    private static final String PERIOD_TYPE_SELECTED = "period_type_selected";
    
    private static final String PERIOD_SELECTED = "period_selected";
    
    
    public void setPeriodType(PeriodType periodType){
        getSession().put( PERIOD_TYPE_SELECTED, periodType );
    }
    
    public void setPeriod(Period period){
        getSession().put( PERIOD_SELECTED, period );
    }
    
    public PeriodType getPeriodTypeSelected(){
        return (PeriodType)getSession().get( PERIOD_TYPE_SELECTED );
    }

    public Period getPeriodSelected(){
        return (Period)getSession().get( PERIOD_SELECTED );
    }

   

    public void setSeletedBagSession( BagSession bagsession )
    {
        getSession().put( BAGSESSION, bagsession );
    }

    public BagSession getSeleteBagSession()
    {
        return (BagSession) getSession().get( BAGSESSION );
    }

    public String getSelectedStartDate()
    {
        return (String) getSession().get( STARTDATE );
    }

    public void setSelectedStartDate( String startDate )
    {
        getSession().put( STARTDATE, startDate );
    }

    public String getSelectedEndtDate()
    {
        return (String) getSession().get( ENDDATE );
    }

    public void setSelectedEndDate( String endDate )
    {
        getSession().put( ENDDATE, endDate );
    }

    public IndicatorGroup getSelectedIndicatorGroup()
    {
        return (IndicatorGroup) getSession().get( INDICATOR_GROUP_SELECTED );
    }

    public void setSelectedIndicatorGroup( IndicatorGroup indicatorGroup )
    {
        getSession().put( INDICATOR_GROUP_SELECTED, indicatorGroup );
    }

    public Indicator getSelectedIndicator()
    {
        return (Indicator) getSession().get( INDICATOR_SELECTED );
    }

    public void setSelectedIndicator( Indicator indicator )
    {
        getSession().put( INDICATOR_SELECTED, indicator );
    }

    public MapFile getSelectedMapFile()
    {
        return (MapFile) getSession().get( MAP_FILE_SELECTED );
    }

    public void setSelectedMapFie( MapFile mapFile )
    {
        getSession().put( MAP_FILE_SELECTED, mapFile );
    }

    private static final Map getSession()
    {
        return ActionContext.getContext().getSession();
    }

}
