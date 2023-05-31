package com.ERP.Softronix_ERP.constants;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 432_000_000; //5 days expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token can not be verified";
    public static final String SOFTRONIX = "SOFTRONIX";
    public static final String SOFTRONIX_ADMIN = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do ot have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
//    public static final String[] PUBLIC_URLS = {"/user/login", "/user/register", "/user/resetpassword/**", "/user/image/**"};
    public static final String[] PUBLIC_URLS = {"**"};

}
