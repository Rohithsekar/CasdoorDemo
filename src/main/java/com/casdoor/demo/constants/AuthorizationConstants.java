package com.casdoor.demo.constants;

import java.util.List;

public class AuthorizationConstants {


    public static final String[] ALL_RESOURCES = {"reports", "payment", "dashboard", "plan", "subscription", ""}; // Add all allowed resources

    public static final List<String> ALL_RESOURCES_LIST = List.of("reports", "payment", "dashboard", "plan", "subscription");
    public static final String[] ALL_ACTIONS = {"GET", "POST", "PUT", "DELETE"}; // Add all allowed actions

    public static final String[] RESOURCE_TYPES = {"API", "TABLE", "FILE"}; //ADD AS YOU NEED
}
