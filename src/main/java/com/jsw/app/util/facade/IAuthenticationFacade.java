package com.jsw.app.util.facade;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    
   Authentication getAuthentication();

}