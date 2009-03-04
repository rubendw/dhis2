package org.hisp.dhis.de.state;

import org.hisp.dhis.datavalue.DataValue;

/**
 * Interface for how DataValues are saved in a stateful way.
 * Implementing classes must supply the remaining necessary properties in some
 * fashion, for example through a bean or some session storage.
 * 
 * The relevant properties are:
 * OrganisationUnit to store the DataValue for.
 * Period to store the DataValue for 
 * The storedBy property, who stored this DataValue
 * The timestamp property, when this DataValue was last updated
 *
 * @author Hans S. Toemmerholt
 * @version $Id$
 */
public interface StatefulDataValueSaver
{

    /**
     * Save a value for the given DataElement, and other relevant properties.
     * @param dataElementId Id of the DataElement to save for.
     * @param value String value to save.
     */
    DataValue saveValue( int dataElementId, int optionComboId, String value );

}
