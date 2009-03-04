package org.hisp.dhis.dataset;

import org.hisp.dhis.system.deletion.DeletionHandler;

public class CompleteDataSetRegistrationDeletionHandler
    extends DeletionHandler
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    public CompleteDataSetRegistrationService completeDataSetRegistrationService;

    public void setCompleteDataSetRegistrationService( CompleteDataSetRegistrationService completeDataSetRegistrationService )
    {
        this.completeDataSetRegistrationService = completeDataSetRegistrationService;
    }

    // -------------------------------------------------------------------------
    // DeletionHandler implementation
    // -------------------------------------------------------------------------

    @Override
    public String getClassName()
    {
        return CompleteDataSetRegistration.class.getSimpleName();
    }
    
    @Override
    public void deleteDataSet( DataSet dataSet )
    {
        completeDataSetRegistrationService.deleteCompleteDataSetRegistrations( dataSet );
    }
}
