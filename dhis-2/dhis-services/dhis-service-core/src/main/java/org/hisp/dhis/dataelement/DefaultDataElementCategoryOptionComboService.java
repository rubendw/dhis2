package org.hisp.dhis.dataelement;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Abyot Asalefew
 * @version $Id$
 */
public class DefaultDataElementCategoryOptionComboService
    implements DataElementCategoryOptionComboService
{
    private final static String SEPARATOR = ",";

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataElementCategoryOptionComboStore dataElementCategoryOptionComboStore;

    public void setDataElementCategoryOptionComboStore(
        DataElementCategoryOptionComboStore dataElementCategoryOptionComboStore )
    {
        this.dataElementCategoryOptionComboStore = dataElementCategoryOptionComboStore;
    }

    private DataElementCategoryComboService dataElementCategoryComboService;

    public void setDataElementCategoryComboService( DataElementCategoryComboService dataElementCategoryComboService )
    {
        this.dataElementCategoryComboService = dataElementCategoryComboService;
    }

    private DataElementCategoryService dataElementCategoryService;

    public void setDataElementCategoryService( DataElementCategoryService dataElementCategoryService )
    {
        this.dataElementCategoryService = dataElementCategoryService;
    }

    private DataElementCategoryOptionService dataElementCategoryOptionService;

    public void setDataElementCategoryOptionService( DataElementCategoryOptionService dataElementCategoryOptionService )
    {
        this.dataElementCategoryOptionService = dataElementCategoryOptionService;
    }

    private DataElementDimensionRowOrderService dataElementDimensionRowOrderService;

    public void setDataElementDimensionRowOrderService(
        DataElementDimensionRowOrderService dataElementDimensionRowOrderService )
    {
        this.dataElementDimensionRowOrderService = dataElementDimensionRowOrderService;
    }

    private DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService;

    public void setDataElementDimensionColumnOrderService(
        DataElementDimensionColumnOrderService dataElementDimensionColumnOrderService )
    {
        this.dataElementDimensionColumnOrderService = dataElementDimensionColumnOrderService;
    }

    // -------------------------------------------------------------------------
    // DataElementCategoryOptionCombo
    // -------------------------------------------------------------------------

    public int addDataElementCategoryOptionCombo( DataElementCategoryOptionCombo dataElementCategoryOptionCombo )
    {
        return dataElementCategoryOptionComboStore.addDataElementCategoryOptionCombo( dataElementCategoryOptionCombo );
    }

    public void updateDataElementCategoryOptionCombo( DataElementCategoryOptionCombo dataElementCategoryOptionCombo )
    {
        dataElementCategoryOptionComboStore.addDataElementCategoryOptionCombo( dataElementCategoryOptionCombo );
    }

    public void deleteDataElementCategoryOptionCombo( DataElementCategoryOptionCombo dataElementCategoryOptionCombo )
    {
        dataElementCategoryOptionComboStore.deleteDataElementCategoryOptionCombo( dataElementCategoryOptionCombo );
    }

    public DataElementCategoryOptionCombo getDataElementCategoryOptionCombo( int id )
    {
        return dataElementCategoryOptionComboStore.getDataElementCategoryOptionCombo( id );
    }

    public DataElementCategoryOptionCombo getDataElementCategoryOptionCombo( DataElementCategoryOptionCombo categoryOptionCombo )
    {
        for ( DataElementCategoryOptionCombo dcoc : getAllDataElementCategoryOptionCombos() )
        {
            // -----------------------------------------------------------------
            // Hibernate puts proxies on associations and makes the native
            // equals methods unusable
            // -----------------------------------------------------------------

            if ( dcoc.equalsOnName( categoryOptionCombo ) )
            {
                return dcoc;
            }
        }

        return null;
    }
    
    public Collection<DataElementCategoryOptionCombo> getAllDataElementCategoryOptionCombos()
    {
        return dataElementCategoryOptionComboStore.getAllDataElementCategoryOptionCombos();
    }

    public Collection<DataElementCategoryOptionCombo> sortDataElementCategoryOptionCombos(
        DataElementCategoryCombo catCombo )
    {
        Collection<DataElementCategoryOptionCombo> optionCombos = new ArrayList<DataElementCategoryOptionCombo>(
            catCombo.getOptionCombos() );

        List<DataElementCategory> categories = new ArrayList<DataElementCategory>( catCombo.getCategories() );

        Map<Integer, DataElementCategory> categoryMap = new TreeMap<Integer, DataElementCategory>();

        int totalColumns = optionCombos.size();

        // ---------------------------------------------------------------------
        // Get the order of categories
        // ---------------------------------------------------------------------

        int index = 1;

        for ( DataElementCategory category : categories )
        {
            DataElementDimensionRowOrder rowOrder = dataElementDimensionRowOrderService
                .getDataElementDimensionRowOrder( catCombo, category );

            if ( rowOrder != null )
            {
                categoryMap.put( rowOrder.getDisplayOrder(), category );
            }
            else
            {
                categoryMap.put( index, category );
            }

            index++;
        }

        Collection<DataElementCategory> orderedCategories = categoryMap.values();

        // ---------------------------------------------------------------------
        // Get the order of options in each category
        // ---------------------------------------------------------------------

        Map<Integer, Collection<DataElementCategoryOption>> orderedOptionsMap = new HashMap<Integer, Collection<DataElementCategoryOption>>();

        index = 1;

        for ( DataElementCategory category : orderedCategories )
        {
            Map<Integer, DataElementCategoryOption> optionsMap = new TreeMap<Integer, DataElementCategoryOption>();

            for ( DataElementCategoryOption option : category.getCategoryOptions() )
            {
                DataElementDimensionColumnOrder columnOrder = dataElementDimensionColumnOrderService
                    .getDataElementDimensionColumnOrder( category, option );

                if ( columnOrder != null )
                {
                    optionsMap.put( columnOrder.getDisplayOrder(), option );
                }
                else
                {
                    optionsMap.put( index, option );
                }

                index++;
            }

            orderedOptionsMap.put( category.getId(), optionsMap.values() );
        }

        int categoryColSpan = totalColumns;

        Map<Integer, Integer> categoryRepeat = new HashMap<Integer, Integer>();

        for ( DataElementCategory category : orderedCategories )
        {
            categoryColSpan = categoryColSpan / category.getCategoryOptions().size();

            categoryRepeat.put( category.getId(), categoryColSpan );

        }

        Map<Integer, Collection<DataElementCategoryOption>> orderedOptions = new HashMap<Integer, Collection<DataElementCategoryOption>>();

        for ( DataElementCategory cat : orderedCategories )
        {
            int outerForLoopCount = totalColumns;
            int innerForLoopCount = categoryRepeat.get( cat.getId() );

            Collection<DataElementCategoryOption> requiredOptions = new ArrayList<DataElementCategoryOption>();
            Collection<DataElementCategoryOption> options = orderedOptionsMap.get( cat.getId() );

            int x = 0;

            while ( x < outerForLoopCount )
            {
                for ( DataElementCategoryOption option : options )
                {
                    for ( int i = 0; i < innerForLoopCount; i++ )
                    {
                        requiredOptions.add( option );

                        x++;
                    }
                }
            }

            orderedOptions.put( cat.getId(), requiredOptions );
        }

        Collection<DataElementCategoryOptionCombo> orderdCategoryOptionCombos = new ArrayList<DataElementCategoryOptionCombo>();

        for ( int i = 0; i < totalColumns; i++ )
        {
            Collection<DataElementCategoryOption> options = new ArrayList<DataElementCategoryOption>( orderedCategories
                .size() );
            Collection<DataElementCategory> copyOforderedCategories = orderedCategories;
            Iterator<DataElementCategory> categoryIterator = copyOforderedCategories.iterator();

            while ( categoryIterator.hasNext() )
            {
                DataElementCategory category = categoryIterator.next();
                Iterator<DataElementCategoryOption> optionIterator = orderedOptions.get( category.getId() ).iterator();
                DataElementCategoryOption option = optionIterator.next();
                options.add( option );
                optionIterator.remove();
            }

            for ( DataElementCategoryOptionCombo optionCombo : optionCombos )
            {
                if ( optionCombo.getCategoryOptions().containsAll( options ) )
                {
                    orderdCategoryOptionCombos.add( optionCombo );
                    break;
                }
            }
        }

        return orderdCategoryOptionCombos;
    }

    public void generateDefaultDimension()
    {
        // ---------------------------------------------------------------------
        // Add default DataElementCategoryOption
        // ---------------------------------------------------------------------

        DataElementCategoryOption categoryOption = new DataElementCategoryOption(
            DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );

        dataElementCategoryOptionService.addDataElementCategoryOption( categoryOption );

        // ---------------------------------------------------------------------
        // Add default DataElementCategory containing default
        // DataElementCategoryOption
        // ---------------------------------------------------------------------

        DataElementCategory category = new DataElementCategory( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );

        Set<DataElementCategoryOption> categoryOptions = new HashSet<DataElementCategoryOption>();
        categoryOptions.add( categoryOption );
        category.setCategoryOptions( categoryOptions );

        dataElementCategoryService.addDataElementCategory( category );

        // ---------------------------------------------------------------------
        // Add default DataElementCategoryCombo made of the default
        // DataElementCategory
        // ---------------------------------------------------------------------

        DataElementCategoryCombo categoryCombo = new DataElementCategoryCombo(
            DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );

        Set<DataElementCategory> categories = new HashSet<DataElementCategory>();
        categories.add( category );
        categoryCombo.setCategories( categories );

        dataElementCategoryComboService.addDataElementCategoryCombo( categoryCombo );

        // ---------------------------------------------------------------------
        // Add default DataElementCategoryOptionCombo
        // ---------------------------------------------------------------------

        DataElementCategoryOptionCombo categoryOptionCombo = new DataElementCategoryOptionCombo();

        categoryOptionCombo.setCategoryCombo( categoryCombo );
        categoryOptionCombo.setCategoryOptions( categoryOptions );

        addDataElementCategoryOptionCombo( categoryOptionCombo );

        Set<DataElementCategoryOptionCombo> categoryOptionCombos = new HashSet<DataElementCategoryOptionCombo>();
        categoryOptionCombos.add( categoryOptionCombo );
        categoryCombo.setOptionCombos( categoryOptionCombos );

        dataElementCategoryComboService.updateDataElementCategoryCombo( categoryCombo );
    }

    public DataElementCategoryOptionCombo getDefaultDataElementCategoryOptionCombo()
    {
        DataElementCategoryCombo categoryCombo = dataElementCategoryComboService
            .getDataElementCategoryComboByName( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME );

        return categoryCombo.getOptionCombos().iterator().next();
    }

    public void generateOptionCombos( DataElementCategoryCombo categoryCombo )
    {
        List<DataElementCategory> categories = new ArrayList<DataElementCategory>( categoryCombo.getCategories() );

        int totalOptionCombos = 1;

        for ( DataElementCategory category : categories )
        {
            totalOptionCombos = totalOptionCombos * category.getCategoryOptions().size();
        }

        int categoryOptionShare = totalOptionCombos;

        Map<Integer, Integer> categoryOptionAppearance = new HashMap<Integer, Integer>();

        for ( DataElementCategory cat : categories )
        {
            categoryOptionShare = categoryOptionShare / cat.getCategoryOptions().size();

            categoryOptionAppearance.put( cat.getId(), categoryOptionShare );
        }

        Map<Integer, Collection<DataElementCategoryOption>> optionsMap = new HashMap<Integer, Collection<DataElementCategoryOption>>();

        for ( DataElementCategory cat : categories )
        {
            int outerForLoopCount = totalOptionCombos;
            int innerForLoopCount = categoryOptionAppearance.get( cat.getId() );

            Collection<DataElementCategoryOption> requiredOptions = new ArrayList<DataElementCategoryOption>();
            Set<DataElementCategoryOption> options = cat.getCategoryOptions();

            int x = 0;

            while ( x < outerForLoopCount )
            {
                for ( DataElementCategoryOption option : options )
                {
                    for ( int i = 0; i < innerForLoopCount; i++ )
                    {
                        requiredOptions.add( option );

                        x++;
                    }
                }
            }

            optionsMap.put( cat.getId(), requiredOptions );
        }

        Set<DataElementCategoryOptionCombo> optionCombos = new HashSet<DataElementCategoryOptionCombo>(
            totalOptionCombos );

        for ( int i = 0; i < totalOptionCombos; i++ )
        {
            Set<DataElementCategoryOption> options = new HashSet<DataElementCategoryOption>( categories.size() );
            
            Collection<DataElementCategory> copyOfCategories = categories;
            
            Iterator<DataElementCategory> categoryIterator = copyOfCategories.iterator();

            while ( categoryIterator.hasNext() )
            {
                DataElementCategory cat = categoryIterator.next();
            
                Iterator<DataElementCategoryOption> optionIterator = optionsMap.get( cat.getId() ).iterator();
                
                DataElementCategoryOption option = optionIterator.next();

                options.add( option );
                
                optionIterator.remove();
            }

            DataElementCategoryOptionCombo optionCombo = new DataElementCategoryOptionCombo();

            optionCombo.setCategoryCombo( categoryCombo );
            
            optionCombo.setCategoryOptions( options );

            addDataElementCategoryOptionCombo( optionCombo );

            optionCombos.add( optionCombo );
        }

        if ( categoryCombo.getOptionCombos().size() != optionCombos.size() )
        {
            categoryCombo.setOptionCombos( optionCombos );

            dataElementCategoryComboService.updateDataElementCategoryCombo( categoryCombo );
        }
    }

    public String getOptionNames( DataElementCategoryOptionCombo dataElementCategoryOptionCombo )
    {
        String optionsName = new String();

        DataElementCategoryCombo catCombo = dataElementCategoryOptionCombo.getCategoryCombo();

        Set<DataElementCategory> categories = catCombo.getCategories();

        Map<Integer, DataElementCategory> categoryOrder = new TreeMap<Integer, DataElementCategory>();

        int index = 1;

        for ( DataElementCategory cat : categories )
        {
            DataElementDimensionRowOrder order = dataElementDimensionRowOrderService.getDataElementDimensionRowOrder(
                catCombo, cat );

            if ( order != null )
            {
                categoryOrder.put( dataElementDimensionRowOrderService.getDataElementDimensionRowOrder( catCombo, cat )
                    .getDisplayOrder(), cat );
            }
            else
            {
                categoryOrder.put( index, cat );
            }

            index++;
        }

        Set<DataElementCategoryOption> tempOptions = dataElementCategoryOptionCombo.getCategoryOptions();

        Map<Integer, DataElementCategoryOption> options = new TreeMap<Integer, DataElementCategoryOption>();

        DataElementCategory cat = null;

        for ( int i = 1; i <= categories.size(); i++ )
        {
            cat = categoryOrder.get( i );

            if ( cat != null )
            {
                for ( DataElementCategoryOption option : tempOptions )
                {
                    if ( cat.getCategoryOptions() != null )
                    {
                        if ( cat.getCategoryOptions().contains( option ) )
                        {
                            options.put( i, option );

                            break;
                        }
                    }
                }
            }
        }

        for ( int i = 1; i <= options.size(); i++ )
        {
            optionsName += options.get( i ).getName() + SEPARATOR;
        }

        if ( optionsName.length() > 0 )
        {
            optionsName = optionsName.substring( 0, optionsName.length() - 1 );
        }

        if ( optionsName.equalsIgnoreCase( DataElementCategoryCombo.DEFAULT_CATEGORY_COMBO_NAME ) )
        {
            return new String();
        }

        String names = "(" + optionsName + ")";

        return names;
    }

    public Collection<Operand> getOperands( Collection<DataElement> dataElements )
    {
        Collection<Operand> operands = new ArrayList<Operand>();
        
        for ( DataElement dataElement : dataElements )
        {
            DataElementCategoryCombo categoryCombo = dataElement.getCategoryCombo();

            Set<DataElementCategoryOptionCombo> categoryOptionCombos = categoryCombo.getOptionCombos();

            if ( categoryOptionCombos.size() > 1 && !(dataElement instanceof CalculatedDataElement) )
            {
                for ( DataElementCategoryOptionCombo optionCombo : categoryOptionCombos )
                {
                    Operand operand = new Operand( dataElement.getId(), optionCombo.getId(), dataElement.getName()
                        + getOptionNames( optionCombo ) );

                    operands.add( operand );
                }
            }
            else
            {
                Operand operand = new Operand( dataElement.getId(), categoryOptionCombos.iterator().next().getId(), dataElement
                    .getName() );

                operands.add( operand );
            }
        }
        
        return operands;
    }
}
