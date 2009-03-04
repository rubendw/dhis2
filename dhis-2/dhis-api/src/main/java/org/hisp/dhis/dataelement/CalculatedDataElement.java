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

import org.hisp.dhis.expression.Expression;

/**
 * A CalculatedDataElement (CDE) is created for a set of other related
 * DataElements. Each constituent DataElement may be multiplied with a factor.
 * 
 * An example: A value for the CDE "Number of births" is summed together from
 * two other Data Elements, "Number of still births" and "Number of live
 * births".
 * 
 * CDEs may be displayed as subtotals in a data entry form. The user may specify
 * whether or not the calculated value should be stored in the database. In all
 * other cases, a CDE behaves just like any other DataElement.
 * 
 * @author Hans S. Toemmerholt
 * @version $Id$
 */
public class CalculatedDataElement
    extends DataElement
{
    private boolean saved;

    private Expression expression;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public CalculatedDataElement()
    {
    }

    /**
     * Constructor for building a complete DataElement, in addition to the
     * properties for the CalculatedDataElement itself.
     * 
     * @param name the unique name for this DataElement
     * @param alternativeName an optional unique alternative name of this
     *        DataElement
     * @param shortName an optional unique name representing this
     *        CalculatedDataElement
     * @param code an optional unique code representing this DataElement
     * @param description description of this DataElement
     * @param active boolean indicating if this DataElement is active or not
     *        (enabled / disabled)
     * @param type the type of this DataElement; e.g. DataElement.TYPE_INT,
     *        DataElement.TYPE_BOOL
     * @param aggregationOperator the aggregation operator of this DataElement;
     *        e.g. DataElement.SUM or DataElement.AVERAGE
     * @param parent the parent DataElement for this DataElement
     * @param saved boolean indicating if values for this CalculatedDataElement
     *        should be saved
     * @param expression Expression defining which DataElements and factors
     *        which make up this CalculatedDataElement
     */
    public CalculatedDataElement( String name, String alternativeName, String shortName, String code,
        String description, boolean active, String type, String aggregationOperator, DataElement parent, boolean saved,
        Expression expression )
    {
        super( name, alternativeName, shortName, code, description, active, type, aggregationOperator, parent );
        this.saved = saved;
        this.expression = expression;
    }

    public Expression getExpression()
    {
        return expression;
    }

    public void setExpression( Expression expression )
    {
        this.expression = expression;
    }

    public boolean isSaved()
    {
        return saved;
    }

    public void setSaved( boolean saved )
    {
        this.saved = saved;
    }
}
