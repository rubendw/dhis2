package org.hisp.dhis.expression;

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

import static org.hisp.dhis.expression.Expression.SEPARATOR;
import static org.hisp.dhis.system.util.MathUtils.calculateExpression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.dataelement.CalculatedDataElement;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.source.Source;
import org.hisp.dhis.system.util.MathUtils;

/**
 * @author Margrethe Store
 * @author Lars Helge Overland
 * @version $Id: DefaultExpressionService.java 5450 2008-06-24 18:11:28Z larshelg $
 */
public class DefaultExpressionService
    implements ExpressionService
{
    private static final String NULL_REPLACEMENT = "0";
    
    private static final int MAX_VARCHAR_LENGTH = 254;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private ExpressionStore expressionStore;

    public void setExpressionStore( ExpressionStore expressionStore )
    {
        this.expressionStore = expressionStore;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private DataElementCategoryOptionComboService categoryOptionComboService;

    public void setCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.categoryOptionComboService = dataElementCategoryOptionComboService;
    }

    // -------------------------------------------------------------------------
    // Expression CRUD operations
    // -------------------------------------------------------------------------

    public int addExpression( Expression expression )
    {
        return expressionStore.addExpression( expression );
    }

    public void deleteExpression( Expression expression )
    {
        expressionStore.deleteExpression( expression );
    }

    public Expression getExpression( int id )
    {
        return expressionStore.getExpression( id );
    }

    public void updateExpression( Expression expression )
    {
        expressionStore.updateExpression( expression );
    }

    public Collection<Expression> getAllExpressions()
    {
        return expressionStore.getAllExpressions();
    }

    // -------------------------------------------------------------------------
    // Business logic
    // -------------------------------------------------------------------------

    public Double getExpressionValue( Expression expression, Period period, Source source )
    {
        String expressionString = generateExpression( expression.getExpression(), period, source );

        return expressionString != null ? calculateExpression( expressionString ) : null;
    }

    public Set<DataElement> getDataElementsInExpression( String expression )
    {
        Set<DataElement> dataElementsInExpression = null;

        if ( expression != null )
        {
            dataElementsInExpression = new HashSet<DataElement>();

            Matcher matcher = getMatcher( "(\\[\\d+\\" + SEPARATOR + "\\d+\\])", expression );

            while ( matcher.find() )
            {
                String replaceString = matcher.group();

                replaceString = replaceString.replaceAll( "[\\[\\]]", "" );

                replaceString = replaceString.substring( 0, replaceString.indexOf( SEPARATOR ) );

                int dataElementId = Integer.parseInt( replaceString );

                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                if ( dataElement != null )
                {
                    dataElementsInExpression.add( dataElement );
                }
            }
        }

        return dataElementsInExpression;
    }

    public Set<String> getOperandsInExpression( String expression )
    {
        Set<String> operandsInExpression = null;

        if ( expression != null )
        {
            operandsInExpression = new HashSet<String>();

            Matcher matcher = getMatcher( "(\\[\\d+\\" + SEPARATOR + "\\d+\\])", expression );

            while ( matcher.find() )
            {
                String operand = matcher.group();

                operand = operand.replaceAll( "[\\[\\]]", "" );

                operandsInExpression.add( operand );
            }
        }

        return operandsInExpression;
    }

    public int expressionIsValid( String formula )
    {
        StringBuffer buffer = new StringBuffer();
        
        Matcher matcher = getMatcher( "\\[.+?\\" + SEPARATOR + ".+?\\]", formula );

        int dataElementId = -1;
        int optionComboId = -1;

        while ( matcher.find() )
        {
            String match = matcher.group();

            match = match.replaceAll( "[\\[\\]]", "" );

            String dataElementIdString = match.substring( 0, match.indexOf( SEPARATOR ) );
            String optionComboIdString = match.substring( match.indexOf( SEPARATOR ) + 1, match.length() );

            try
            {
                dataElementId = Integer.parseInt( dataElementIdString );
            }
            catch ( NumberFormatException ex )
            {
                return DATAELEMENT_ID_NOT_NUMERIC;
            }

            try
            {
                optionComboId = Integer.parseInt( optionComboIdString );
            }
            catch ( NumberFormatException ex )
            {
                return CATEGORYOPTIONCOMBO_ID_NOT_NUMERIC;
            }

            if ( dataElementService.getDataElement( dataElementId ) == null )
            {
                return DATAELEMENT_DOES_NOT_EXIST;
            }

            if ( categoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId ) == null )
            {
                return CATEGORYOPTIONCOMBO_DOES_NOT_EXIST;
            }

            // -----------------------------------------------------------------
            // Replacing the operand with 1 in order to later be able to verify
            // that the formula is mathematically valid
            // -----------------------------------------------------------------

            matcher.appendReplacement( buffer, "1" );
        }
        
        matcher.appendTail( buffer );
        
        if ( MathUtils.expressionHasErrors( buffer.toString() ) )
        {
            return EXPRESSION_NOT_WELL_FORMED;
        }        

        return VALID;
    }

    public String getExpressionDescription( String formula )
    {
        StringBuffer buffer = null;

        if ( formula != null )
        {
            buffer = new StringBuffer();

            Matcher matcher = getMatcher( "\\[.+?\\" + SEPARATOR + ".+?\\]", formula );

            int dataElementId = -1;
            int optionComboId = -1;

            DataElement dataElement = null;
            DataElementCategoryOptionCombo optionCombo = null;

            while ( matcher.find() )
            {
                String replaceString = matcher.group();

                replaceString = replaceString.replaceAll( "[\\[\\]]", "" );

                String dataElementIdString = replaceString.substring( 0, replaceString.indexOf( SEPARATOR ) );
                String optionComboIdString = replaceString.substring( replaceString.indexOf( SEPARATOR ) + 1, replaceString.length() );

                try
                {
                    dataElementId = Integer.parseInt( dataElementIdString );
                }
                catch ( NumberFormatException ex )
                {
                    throw new IllegalArgumentException( "Data element identifier must be a number: " + replaceString );
                }

                try
                {
                    optionComboId = Integer.parseInt( optionComboIdString );
                }
                catch ( NumberFormatException ex )
                {
                    throw new IllegalArgumentException( "Category option combo identifier must be a number: "
                        + replaceString );
                }

                dataElement = dataElementService.getDataElement( dataElementId );
                optionCombo = categoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId );

                if ( dataElement == null )
                {
                    throw new IllegalArgumentException( "Identifier does not reference a data element: "
                        + dataElementId );
                }

                if ( optionCombo == null )
                {
                    throw new IllegalArgumentException( "Identifier does not reference a category option combo: "
                        + optionComboId );
                }

                replaceString = dataElement.getName() + SEPARATOR + categoryOptionComboService.getOptionNames( optionCombo );

                if ( replaceString.endsWith( SEPARATOR ) )
                {
                    replaceString = replaceString.substring( 0, replaceString.length() - 1 );
                }

                matcher.appendReplacement( buffer, replaceString );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? substring( buffer.toString(), 0, MAX_VARCHAR_LENGTH ) : null;
    }

    public String replaceCDEsWithTheirExpression( String expression )
    {
        StringBuffer buffer = null;

        if ( expression != null )
        {
            buffer = new StringBuffer();

            Set<DataElement> caclulatedDataElementsInExpression = getDataElementsInExpression( expression );

            Iterator<DataElement> iterator = caclulatedDataElementsInExpression.iterator();

            while ( iterator.hasNext() )
            {
                DataElement dataElement = iterator.next();

                if ( !(dataElement instanceof CalculatedDataElement) )
                {
                    iterator.remove();
                }
            }

            Matcher matcher = getMatcher( "(\\[\\d+\\" + SEPARATOR + "\\d+\\])", expression );

            while ( matcher.find() )
            {
                String replaceString = matcher.group();

                for ( DataElement dataElement : caclulatedDataElementsInExpression )
                {
                    if ( replaceString.startsWith( "[" + dataElement.getId() + SEPARATOR ) )
                    {
                        CalculatedDataElement calculatedDataElement = (CalculatedDataElement) dataElement;

                        replaceString = calculatedDataElement.getExpression().getExpression();

                        break;
                    }
                }

                matcher.appendReplacement( buffer, replaceString );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }

    public String generateExpression( String expression, Period period, Source source )
    {
        StringBuffer buffer = null;

        if ( expression != null )
        {
            Matcher matcher = getMatcher( "(\\[\\d+\\" + SEPARATOR + "\\d+\\])", expression );

            buffer = new StringBuffer();

            while ( matcher.find() )
            {
                String replaceString = matcher.group();

                replaceString = replaceString.replaceAll( "[\\[\\]]", "" );

                String dataElementIdString = replaceString.substring( 0, replaceString.indexOf( SEPARATOR ) );
                String optionComboIdString = replaceString.substring( replaceString.indexOf( SEPARATOR ) + 1, replaceString.length() );

                int dataElementId = Integer.parseInt( dataElementIdString );
                int optionComboId = Integer.parseInt( optionComboIdString );

                DataElement dataElement = dataElementService.getDataElement( dataElementId );

                DataElementCategoryOptionCombo optionCombo = categoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId );

                DataValue dataValue = dataValueService.getDataValue( source, dataElement, period, optionCombo );

                if ( dataValue == null )
                {
                    replaceString = NULL_REPLACEMENT;
                }
                else
                {
                    replaceString = String.valueOf( dataValue.getValue() );
                }

                matcher.appendReplacement( buffer, replaceString );
            }

            matcher.appendTail( buffer );
        }

        return buffer != null ? buffer.toString() : null;
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private Matcher getMatcher( String regex, String expression )
    {
        Pattern pattern = Pattern.compile( regex );

        return pattern.matcher( expression );
    }

    private String substring( String string, int startIndex, int length )
    {
        if ( string.length() > length )
        {
            return string.substring( startIndex, length );
        }

        return string;
    }
}
