package com.casdoor.demo.constants;

public class Constants {

    public static final String ERROR = "Error";
    public static final String SUCCESS ="Success";
    public static final String SERVER_ERROR_MESSAGE = " We are facing some issue. Please try some time later.";

    public static final int SUPER_ADMIN =1;
    public static final String returnErrorTemplate(Exception exception, String methodName, String message){
        return String.format("%s caught in %s: %s ", exception.getClass().getSimpleName(), methodName, message);

    }
}
