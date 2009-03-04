package org.hisp.dhis.importexport.dxf.converter;

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

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hisp.dhis.expression.Expression;
import org.hisp.dhis.expression.ExpressionService;
import org.hisp.dhis.importexport.ExportParams;
import org.hisp.dhis.importexport.GroupMemberType;
import org.hisp.dhis.importexport.ImportObjectService;
import org.hisp.dhis.importexport.ImportParams;
import org.hisp.dhis.importexport.XMLConverter;
import org.hisp.dhis.importexport.converter.AbstractValidationRuleConverter;
import org.hisp.dhis.validation.ValidationRule;
import org.hisp.dhis.validation.ValidationRuleService;
import org.amplecode.staxwax.reader.XMLReader;
import org.amplecode.staxwax.writer.XMLWriter;

/**
 * @author Lars Helge Overland
 * @version $Id: ValidationRuleConverter.java 5455 2008-06-25 20:40:32Z larshelg $
 */
public class ValidationRuleConverter
    extends AbstractValidationRuleConverter implements XMLConverter
{
    public static final String COLLECTION_NAME = "validationRules";
    public static final String ELEMENT_NAME = "validationRule";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_OPERATOR = "operator";
    private static final String FIELD_LEFTSIDE_EXPRESSION = "leftSideExpression";
    private static final String FIELD_LEFTSIDE_DESCRIPTION = "leftSideDescription";
    private static final String FIELD_RIGHTSIDE_EXPRESSION = "rightSideExpression";
    private static final String FIELD_RIGHTSIDE_DESCRIPTION = "rightSideDescription";

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private Map<Object, Integer> dataElementMapping;
    
    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructor for write operations.
     */
    public ValidationRuleConverter()
    {   
    }
    
    /**
     * Constructor for read operations.
     * 
     * @param importObjectService the importObjectService to use.
     * @param validationRuleService the ValidationRuleService to use.
     * @param expressionService the expressionService to use.
     * @param dataElementMapping the data element mapping to use.
     */
    public ValidationRuleConverter( ImportObjectService importObjectService, 
        ValidationRuleService validationRuleService,
        ExpressionService expressionService,
        Map<Object, Integer> dataElementMapping )
    {
        this.importObjectService = importObjectService;
        this.validationRuleService = validationRuleService;
        this.expressionService = expressionService;
        this.dataElementMapping = dataElementMapping;
    }

    // -------------------------------------------------------------------------
    // XMLConverter implementation
    // -------------------------------------------------------------------------

    public void write( XMLWriter writer, ExportParams params )
    {
        Collection<ValidationRule> validationRules = params.getValidationRules();
        
        if ( validationRules != null && validationRules.size() > 0 )
        {
            writer.openElement( COLLECTION_NAME );
            
            for ( ValidationRule rule : validationRules )
            {
                writer.openElement( ELEMENT_NAME );
                
                writer.writeElement( FIELD_NAME, rule.getName() );
                writer.writeElement( FIELD_DESCRIPTION, rule.getDescription() );
                writer.writeElement( FIELD_TYPE, rule.getType() );
                writer.writeElement( FIELD_OPERATOR, rule.getOperator() );
                writer.writeElement( FIELD_LEFTSIDE_EXPRESSION, rule.getLeftSide().getExpression() );
                writer.writeElement( FIELD_LEFTSIDE_DESCRIPTION, rule.getLeftSide().getDescription() );
                writer.writeElement( FIELD_RIGHTSIDE_EXPRESSION, rule.getRightSide().getExpression() );
                writer.writeElement( FIELD_RIGHTSIDE_DESCRIPTION, rule.getRightSide().getDescription() );
                
                writer.closeElement( ELEMENT_NAME );
            }
            
            writer.closeElement( COLLECTION_NAME );
        }
    }
    
    public void read( XMLReader reader, ImportParams params )
    {
        while ( reader.moveToStartElement( ELEMENT_NAME, COLLECTION_NAME ) )
        {
            ValidationRule validationRule = new ValidationRule();
            
            Expression leftSide = new Expression();
            Expression rightSide = new Expression();
            
            validationRule.setLeftSide( leftSide );
            validationRule.setRightSide( rightSide );
            
            reader.moveToStartElement( FIELD_NAME );
            validationRule.setName( reader.getElementValue() );

            reader.moveToStartElement( FIELD_DESCRIPTION );
            validationRule.setDescription( reader.getElementValue() );

            reader.moveToStartElement( FIELD_TYPE );
            validationRule.setType( reader.getElementValue() );

            reader.moveToStartElement( FIELD_OPERATOR );
            validationRule.setOperator( reader.getElementValue() );
            
            reader.moveToStartElement( FIELD_LEFTSIDE_EXPRESSION );
            validationRule.getLeftSide().setExpression( getMappedExpression( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_LEFTSIDE_DESCRIPTION );
            validationRule.getLeftSide().setDescription( reader.getElementValue() );

            String leftSideExpression = validationRule.getLeftSide().getExpression();
            
            validationRule.getLeftSide().setDataElementsInExpression( 
                expressionService.getDataElementsInExpression( leftSideExpression ) );
            
            reader.moveToStartElement( FIELD_RIGHTSIDE_EXPRESSION );
            validationRule.getRightSide().setExpression( getMappedExpression( reader.getElementValue() ) );
            
            reader.moveToStartElement( FIELD_RIGHTSIDE_DESCRIPTION );
            validationRule.getRightSide().setDescription( reader.getElementValue() );
            
            String rightSideExpression = validationRule.getRightSide().getExpression();
            
            validationRule.getRightSide().setDataElementsInExpression(
                expressionService.getDataElementsInExpression( rightSideExpression ) );
            
            read( validationRule, ValidationRule.class, GroupMemberType.NONE, params );
        }
    }

    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    public String getMappedExpression( String expression ) 
    {
        if ( expression != null )
        {
            Matcher matcher = getMatcher( "\\[\\d+\\]", expression );
                        
            StringBuffer buffer = new StringBuffer();
            
            while ( matcher.find() )
            {
                String replaceString = matcher.group();
                
                replaceString = replaceString.replaceAll( "[\\[\\]]", "" );
                
                Integer dataElementId = dataElementMapping.get( Integer.parseInt( replaceString ) );

                matcher.appendReplacement( buffer, dataElementId.toString() );
            }

            matcher.appendTail( buffer );
            
            return buffer.toString();
        }

        return null;
    }    

    private Matcher getMatcher( String regex, String expression )
    {
        Pattern pattern = Pattern.compile( regex );
        
        return pattern.matcher( expression );
    }
}
