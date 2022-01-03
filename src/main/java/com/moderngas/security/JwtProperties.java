package com.moderngas.security;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtProperties {

    public static final String SECRET = "@bhishek#567B";

    public static final int EXPIRATION_TIME = 864000000; /*420000*/ /*864000000*/

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";
}
