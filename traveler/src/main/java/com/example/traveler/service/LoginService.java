package com.example.traveler.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    
    public static boolean isLoggedIn(String accessToken) {
        return (accessToken != null && accessToken != "") ? true : false;
    }
    
    public static boolean hasRole(int roleId) {
        return (roleId == (int)roleId && roleId > 0) ? true : false;
    }
    
}
