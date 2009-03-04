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

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

import org.hisp.dhis.common.MetaObject;
import org.hisp.dhis.datadictionary.ExtendedDataElement;

/**
 * A DataElement is a definition (meta-information about) of the entities that
 * are captured in the system. An example from public health care is a
 * DataElement representing the number BCG doses; A DataElement with "BCG dose"
 * as name, with type DataElement.TYPE_INT. DataElements can be structured
 * hierarchically, one DataElement can have a parent and a collection of
 * children. The sum of the children represent the same entity as the parent.
 * Hiearchies of DataElements are used to give more fine- or course-grained
 * representations of the entities.
 * 
 * @author Kristian Nordal
 * @version $Id: DataElement.java 5540 2008-08-19 10:47:07Z larshelg $
 */
public class DataElement
    implements Serializable, MetaObject
{
    public static final String TYPE_STRING = "string";

    public static final String TYPE_INT = "int";

    public static final String TYPE_BOOL = "bool";

    public static final String AGGREGATION_OPERATOR_SUM = "sum";

    public static final String AGGREGATION_OPERATOR_AVERAGE ="average";
    
    public static final String AGGREGATION_OPERATOR_COUNT = "count";

    /**
     * The database internal identifier for this DataElement.
     */
    private int id;
    
    /**
     * The Universally Unique Identifer for this DataElement. 
     */    
    private String uuid;

    /**
     * The name of this DataElement. Required and unique.
     */
    private String name;

    /**
     * An alternative name of this DataElement. Optional but unique.
     */
    private String alternativeName;

    /**
     * An short name representing this DataElement. Optional but unique.
     */
    private String shortName;

    /**
     * An code representing this DataElement. Optional but unique.
     */
    private String code;

    /**
     * Description of this DataElement.
     */
    private String description;

    /**
     * If this DataElement is active or not (enabled or disabled).
     */
    private boolean active;

    /**
     * The type of this DataElement; e.g. DataElement.TYPE_INT or
     * DataElement.TYPE_BOOL.
     */
    private String type;

    /**
     * The aggregation operator of this DataElement; e.g. DataElement.SUM og
     * DataElement.AVERAGE.
     */
    private String aggregationOperator;

    /**
     * A Collection of children DataElements.
     */
    private Set<DataElement> children = new HashSet<DataElement>();

    /**
     * The parent DataElement for this DataElement.
     */
    private DataElement parent;
    
    /**
     * Extended information about the DataElement.
     */
    private ExtendedDataElement extended;

    /**
     * A combination of categories to capture data.
     */    
    private DataElementCategoryCombo categoryCombo;
    
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public DataElement()
    {
    }

    /**
     * @param name the unique name for this DataElement
     * @param shortName an optional unique name representing this DataElement
     * @param description description of this DataElement
     * @param active boolean indicating if this DataElement is active or not
     *        (enabled / disabled)
     * @param type the type of this DataElement; e.g. DataElement.TYPE_INT,
     *        DataElement.TYPE_BOOL
     * @param parent the parent DataElement for this DataElement
     * @deprecated Use
     *             {@link #DataElement(String, String, String, boolean, String, int, DataElement)}
     */
    public DataElement( String name, String shortName, String description, boolean active, String type,
        DataElement parent )
    {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.active = active;
        this.type = type;
        this.parent = parent;
    }

    /**
     * @param name the unique name for this DataElement
     * @param shortName an optional unique name representing this DataElement
     * @param description description of this DataElement
     * @param active boolean indicating if this DataElement is active or not
     *        (enabled / disabled)
     * @param type the type of this DataElement; e.g. DataElement.TYPE_INT,
     *        DataElement.TYPE_BOOL
     * @param aggregationOperator the aggregation operator of this DataElement;
     *        e.g. DataElement.SUM or DataElement.AVERAGE
     * @param parent the parent DataElement for this DataElement
     */
    public DataElement( String name, String shortName, String description, boolean active, String type,
        String aggregationOperator, DataElement parent )
    {
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.active = active;
        this.type = type;
        this.aggregationOperator = aggregationOperator;
        this.parent = parent;
    }

    /**
     * @param name the unique name for this DataElement
     * @param alternativeName an optional unique alternative name of this
     *        DataElement
     * @param shortName an optional unique name representing this DataElement
     * @param code an optional unique code representing this DataElement
     * @param description description of this DataElement
     * @param active boolean indicating if this DataElement is active or not
     *        (enabled / disabled)
     * @param type the type of this DataElement; e.g. DataElement.TYPE_INT,
     *        DataElement.TYPE_BOOL
     * @param aggregationOperator the aggregation operator of this DataElement;
     *        e.g. DataElement.SUM or DataElement.AVERAGE
     * @param parent the parent DataElement for this DataElement
     */
    public DataElement( String name, String alternativeName, String shortName, String code, String description,
        boolean active, String type, String aggregationOperator, DataElement parent )
    {
        this.name = name;
        this.alternativeName = alternativeName;
        this.shortName = shortName;
        this.code = code;
        this.description = description;
        this.active = active;
        this.type = type;
        this.aggregationOperator = aggregationOperator;
        this.parent = parent;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof DataElement) )
        {
            return false;
        }

        final DataElement other = (DataElement) o;

        return name.equals( other.getName() );
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid( String uuid )
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getAlternativeName()
    {
        return alternativeName;
    }

    public void setAlternativeName( String alternativeName )
    {
        this.alternativeName = alternativeName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive( boolean active )
    {
        this.active = active;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getAggregationOperator()
    {
        return aggregationOperator;
    }

    public void setAggregationOperator( String aggregationOperator )
    {
        this.aggregationOperator = aggregationOperator;
    }

    public Set<DataElement> getChildren()
    {
        return children;
    }

    public void setChildren( Set<DataElement> children )
    {
        this.children = children;
    }

    public DataElement getParent()
    {
        return parent;
    }

    public void setParent( DataElement parent )
    {
        this.parent = parent;
    }

    public ExtendedDataElement getExtended()
    {
        return extended;
    }

    public void setExtended( ExtendedDataElement extended )
    {
        this.extended = extended;
    }
    

    public DataElementCategoryCombo getCategoryCombo()
    {
        return categoryCombo;
    }

    public void setCategoryCombo( DataElementCategoryCombo categoryCombo )
    {
        this.categoryCombo = categoryCombo;
    }

}
