package com.example.client.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.prefs.Preferences;

public class Utils {
    private Preferences preferences = Preferences.userRoot();

    public HttpHeaders createAuthenticationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + preferences.get("accessToken", ""));

        return headers;
    }
}
