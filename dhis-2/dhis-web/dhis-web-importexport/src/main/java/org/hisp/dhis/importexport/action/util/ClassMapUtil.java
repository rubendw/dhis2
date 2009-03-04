package org.hisp.dhis.importexport.action.util;

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

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.datadictionary.DataDictionary;
import org.hisp.dhis.datadictionary.ExtendedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementGroup;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.indicator.Indicator;
import org.hisp.dhis.indicator.IndicatorGroup;
import org.hisp.dhis.indicator.IndicatorType;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.reporttable.ReportTable;
import org.hisp.dhis.validation.ValidationRule;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class ClassMapUtil
{
    //TODO move to API?
    
    public static enum ObjectType
    {
        DATAELEMENT,
        EXTENDEDDATAELEMENT,
        DATAELEMENTGROUP,
        INDICATORTYPE,
        INDICATOR,
        INDICATORGROUP,
        DATADICTIONARY,
        DATASET,
        ORGANISATIONUNIT,
        ORGANISATIONUNITGROUP,
        ORGANISATIONUNITGROUPSET,
        VALIDATIONRULE,
        REPORTTABLE,
        DATAVALUE
    }
    
    private static Map<ObjectType, Class<?>> classMap;
    
    static
    {
        classMap = new HashMap<ObjectType, Class<?>>();
        
        classMap.put( ObjectType.DATAELEMENT, DataElement.class );
        classMap.put( ObjectType.EXTENDEDDATAELEMENT, ExtendedDataElement.class );
        classMap.put( ObjectType.DATAELEMENTGROUP, DataElementGroup.class );
        classMap.put( ObjectType.INDICATORTYPE, IndicatorType.class );
        classMap.put( ObjectType.INDICATOR, Indicator.class );
        classMap.put( ObjectType.INDICATORGROUP, IndicatorGroup.class );
        classMap.put( ObjectType.DATADICTIONARY, DataDictionary.class );
        classMap.put( ObjectType.DATASET, DataSet.class );
        classMap.put( ObjectType.ORGANISATIONUNIT, OrganisationUnit.class );
        classMap.put( ObjectType.ORGANISATIONUNITGROUP, OrganisationUnitGroup.class );
        classMap.put( ObjectType.ORGANISATIONUNITGROUPSET, OrganisationUnitGroupSet.class );
        classMap.put( ObjectType.VALIDATIONRULE, ValidationRule.class );
        classMap.put( ObjectType.REPORTTABLE, ReportTable.class );
        classMap.put( ObjectType.DATAVALUE, DataValue.class );
    }
    
    public static Class<?> getClass( String type )
    {
        try
        {
            return classMap.get( ObjectType.valueOf( type ) );
        }
        catch ( IllegalArgumentException ex )
        {
            return null;
        }
    }
}
