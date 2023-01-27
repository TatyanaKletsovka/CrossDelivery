package com.syberry.crossdelivery.authorization.util;

import com.syberry.crossdelivery.authorization.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtil {

    public static UserDetailsImpl getUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
