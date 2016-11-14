package com.fincassa.jtest.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by vyn on 14.11.2016.
 */
public class CookiesManager {
    private static String _clientIdCookieKey;
    private static final int _clientCookieMagAge= 365*24*60*60;

    public static void init(String pCookieKey) {
        _clientIdCookieKey= pCookieKey;
    }

    public static String getClientId(HttpServletRequest pRequest) {
        String clientid= null;

        for(Cookie cookie: pRequest.getCookies()) {
            if (_clientIdCookieKey.equals(cookie.getName())) {
                clientid= cookie.getValue();
            }
        }

        return clientid;
    }

    public static String setClientId(HttpServletResponse pResponce) {
        String clientid= UUID.randomUUID().toString();

        Cookie cookie= new Cookie(_clientIdCookieKey,clientid);
        cookie.setMaxAge(_clientCookieMagAge);
        pResponce.addCookie(cookie);

        return clientid;
    }
}
