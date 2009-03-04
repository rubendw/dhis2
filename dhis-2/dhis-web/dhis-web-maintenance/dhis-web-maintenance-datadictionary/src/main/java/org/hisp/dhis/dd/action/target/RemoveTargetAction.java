package org.hisp.dhis.dd.action.target;

import org.hisp.dhis.target.Target;
import org.hisp.dhis.target.TargetService;

import com.opensymphony.xwork.Action;

/**
 * @author Martin Øinæs Myrseth
 * @version $Id: RemoveTargetAction.java 3998 2007-11-14 21:55:20Z torkildr $
 */
public class RemoveTargetAction
    implements Action
{    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private TargetService targetService;

    public void setTargetService( TargetService targetService )
    {
        this.targetService = targetService;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    
    private int targetId;
    
    public void setTargetId( int targetId )
    {
    	this.targetId = targetId;
    }  
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
        throws Exception
    {      
        
        Target target = targetService.getTarget( targetId );
        
        if ( target != null )
        {
            targetService.deleteTarget( target );
            
            return SUCCESS;
        }
        else
        {
            return ERROR;
        }
    }

}
