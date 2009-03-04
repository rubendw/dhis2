package org.hisp.dhis.webwork.interceptor;

import java.util.HashMap;
import java.util.Map;

import ognl.NoSuchPropertyException;
import ognl.Ognl;

import org.hisp.dhis.options.datadictionary.DataDictionaryModeManager;

import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;

public class WebWorkDataDictionaryModeInterceptor
    extends AroundInterceptor
{
    private static final String KEY_DATA_DICTIONARY_MODE = "dataDictionaryMode";
    
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private DataDictionaryModeManager dataDictionaryModeManager;

    public void setDataDictionaryModeManager( DataDictionaryModeManager dataDictionaryModeManager )
    {
        this.dataDictionaryModeManager = dataDictionaryModeManager;
    }
    
    // -------------------------------------------------------------------------
    // AroundInterceptor implementation
    // -------------------------------------------------------------------------

    protected void before( ActionInvocation invocation )
        throws Exception
    {
        Action action = (Action) invocation.getAction();
        
        String currentMode = dataDictionaryModeManager.getCurrentDataDictionaryMode();
        
        // ---------------------------------------------------------------------
        // Make the objects available for web templates
        // ---------------------------------------------------------------------
        
        Map<String, Object> templateMap = new HashMap<String, Object>( 1 );
        
        templateMap.put( KEY_DATA_DICTIONARY_MODE, currentMode );
        
        invocation.getStack().push( templateMap );
        
        // ---------------------------------------------------------------------
        // Set the objects in the action class if the properties exist
        // ---------------------------------------------------------------------

        Map contextMap = invocation.getInvocationContext().getContextMap();
        
        try
        {
            Ognl.setValue( KEY_DATA_DICTIONARY_MODE, contextMap, action, currentMode );
        }
        catch ( NoSuchPropertyException e )
        {
        }
    }
    
    protected void after( ActionInvocation invocation, String result )
        throws Exception
    {
        // Not in use
    }   
}
