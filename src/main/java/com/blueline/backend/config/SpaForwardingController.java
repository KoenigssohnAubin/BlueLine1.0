package com.blueline.backend.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards client-side routes of the React console (backend/web) to index.html so a
 * hard refresh on e.g. /missions doesn't 404. Kept as an explicit route list rather than
 * a catch-all regex so it can never shadow /api/**.
 */
@Controller
public class SpaForwardingController {

    @RequestMapping({
            "/login",
            "/missions", "/missions/**",
            "/ambulances", "/ambulances/**",
            "/hospitals", "/hospitals/**",
            "/alerts", "/alerts/**",
            "/stats",
            "/users", "/users/**"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
