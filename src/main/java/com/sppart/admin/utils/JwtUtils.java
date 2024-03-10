package com.sppart.admin.utils;

public class JwtUtils {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; //access 2 hours
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //refresh 7 days
    public static final String AUTHORITY_VALUE = "authority";
}
