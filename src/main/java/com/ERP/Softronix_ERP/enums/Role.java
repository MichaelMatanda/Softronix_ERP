package com.ERP.Softronix_ERP.enums;

import com.ERP.Softronix_ERP.constants.Authorities;

public enum Role {
    ROLE_USER(Authorities.USER_AUTHORITIES),
    ROLE_HR(Authorities.HR_AUTHORITIES),
    ROLE_MANAGER(Authorities.MANAGER_AUTHORITIES),
    ROLE_ADMIN(Authorities.ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(Authorities.SUPER_ADMIN_AUTHORITIES);

    private String[] authorities;
    Role(String... authorities){
        this.authorities=authorities;
    }
    public String[] getAuthorities(){
        return authorities;
    }
}
