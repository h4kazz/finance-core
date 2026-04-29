package com.home.finance.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Allowed frontend origin for a local React Vite application.
 */
@ConfigurationProperties(prefix = "app.frontend")
public class FrontendCorsProperties {

    private String origin = "http://localhost:5173";

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
