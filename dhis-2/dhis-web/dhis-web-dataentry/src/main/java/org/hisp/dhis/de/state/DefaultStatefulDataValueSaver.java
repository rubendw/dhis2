package org.hisp.dhis.de.state;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataelement.DataElementCategoryOptionCombo;
import org.hisp.dhis.dataelement.DataElementCategoryOptionComboService;
import org.hisp.dhis.dataelement.DataElementService;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueService;
import org.hisp.dhis.de.action.SaveValueAction;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.CurrentUserService;

/**
 * This implementation uses the SelectedStateManager to retrieve the relevant
 * properties for the value being saved. It is basically a copy of the
 * functionality found in SaveValueAction, but not using CGI parameters.
 * 
 * @see SaveValueAction
 * @author Hans S. Toemmerholt
 * @version $Id$
 */
public class DefaultStatefulDataValueSaver
    implements StatefulDataValueSaver
{

    private static final Log LOG = LogFactory.getLog( SaveValueAction.class );

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private SelectedStateManager selectedStateManager;

    public void setSelectedStateManager( SelectedStateManager selectedStateManager )
    {
        this.selectedStateManager = selectedStateManager;
    }

    private DataElementService dataElementService;

    public void setDataElementService( DataElementService dataElementService )
    {
        this.dataElementService = dataElementService;
    }

    private DataElementCategoryOptionComboService dataElementCategoryOptionComboService;

    public void setDataElementCategoryOptionComboService( DataElementCategoryOptionComboService dataElementCategoryOptionComboService )
    {
        this.dataElementCategoryOptionComboService = dataElementCategoryOptionComboService;
    }
    
    private DataValueService dataValueService;

    public void setDataValueService( DataValueService dataValueService )
    {
        this.dataValueService = dataValueService;
    }

    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    // -------------------------------------------------------------------------
    // Interface implementation
    // -------------------------------------------------------------------------
    
    public DataValue saveValue( int dataElementId, int optionComboId, String value )
    {
        OrganisationUnit organisationUnit = selectedStateManager.getSelectedOrganisationUnit();

        Period period = selectedStateManager.getSelectedPeriod();

        DataElement dataElement = dataElementService.getDataElement( dataElementId );
        
        DataElementCategoryOptionCombo optionCombo =  dataElementCategoryOptionComboService.getDataElementCategoryOptionCombo( optionComboId );

        String storedBy = currentUserService.getCurrentUsername();

        if ( storedBy == null )
        {
            storedBy = "[unknown]";
        }

        if ( value != null && value.trim().equals( "" ) )
        {
            value = null;
        }

        // ---------------------------------------------------------------------
        // Update DB
        // ---------------------------------------------------------------------

        DataValue dataValue = dataValueService.getDataValue( organisationUnit, dataElement, period, optionCombo );

        if ( dataValue == null )
        {
            if ( value != null )
            {
                LOG.debug( "Adding DataValue, value added" );

                dataValue = new DataValue( dataElement, period, organisationUnit, value, storedBy, new Date(), null, optionCombo );

                dataValueService.addDataValue( dataValue );
            }
        }
        else
        {
            LOG.debug( "Updating DataValue, value added/changed" );

            dataValue.setValue( value );
            dataValue.setTimestamp( new Date() );
            dataValue.setStoredBy( storedBy );

            dataValueService.updateDataValue( dataValue );
        }

        return dataValue;
        
    }

}
