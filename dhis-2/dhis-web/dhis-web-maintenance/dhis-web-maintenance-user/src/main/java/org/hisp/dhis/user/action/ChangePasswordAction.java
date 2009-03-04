package org.hisp.dhis.user.action;

import org.hisp.dhis.security.PasswordManager;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserStore;

import com.opensymphony.xwork.Action;

public class ChangePasswordAction implements Action{
	
	// -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
	
	private UserStore userStore;
	
	private PasswordManager passwordManager;
	
	private CurrentUserService currentUserService;
	// -------------------------------------------------------------------------
    // Input & Output
    // -------------------------------------------------------------------------
	
	private String username;
	 
	private String rawPassword;
	
	private String retypePassword;

	// -------------------------------------------------------------------------
    // Getter & Setter
    // -------------------------------------------------------------------------
	
	
    public void setUserStore( UserStore userStore )
    {
        this.userStore = userStore;
    }

    public void setPasswordManager( PasswordManager passwordManager )
    {
        this.passwordManager = passwordManager;
    }
    
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setCurrentUserService(CurrentUserService currentUserService) {
		this.currentUserService = currentUserService;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
	
	// -------------------------------------------------------------------------
    // Implement Method
    // -------------------------------------------------------------------------

	

	public String execute() throws Exception {
		
		User user = userStore.getUser(currentUserService.getCurrentUser().getId());
		
		UserCredentials userCredentials = userStore.getUserCredentials( user );
		
		username = userCredentials.getUsername();
		
		if(rawPassword==null||retypePassword==null){
		
			return INPUT;
		}
		
		rawPassword = rawPassword.trim();
		retypePassword = retypePassword.trim();
		
		if(rawPassword.length()==0||retypePassword.length()==0){
		
			return INPUT;
		}	
		if(!rawPassword.equals(retypePassword)){
	
			return INPUT;
		}	
	
		
	     userCredentials.setPassword( passwordManager.encodePassword( userCredentials.getUsername(), rawPassword ) );
	  
		 userStore.updateUserCredentials( userCredentials );
		 
	     userStore.updateUser( user );
	
	    return SUCCESS;
	
	}
}