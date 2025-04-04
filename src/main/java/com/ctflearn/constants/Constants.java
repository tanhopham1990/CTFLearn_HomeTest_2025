package com.ctflearn.constants;

public final class Constants {

    /* For UI Element loads */
    public static final long WAITING_TIME_SHORT = 10;
    public static final long WAITING_TIME_DEFAULT = WAITING_TIME_SHORT * 2;
    public static final long WAITING_TIME_LONG = WAITING_TIME_DEFAULT * 3;

    /* For API */
    public static final long LIMIT_RESPONSE_TIME_IN_SECONDS = 60;
    public static final long LIMIT_RESPONSE_FOR_BUG_TIME_IN_SECONDS = LIMIT_RESPONSE_TIME_IN_SECONDS * 10;
    public static final long LIMIT_RESPONSE_TIME_IN_SECONDS_OTHER_SERVICES = LIMIT_RESPONSE_TIME_IN_SECONDS * 10;
    public static final long LIMIT_RESPONSE_TIME_IN_SECONDS_CURRENCY_CLOUD_SERVICES = LIMIT_RESPONSE_TIME_IN_SECONDS * 5;
    public static final long LIMIT_RESPONSE_TIME_IN_SECONDS_GENIES_SERVICES = LIMIT_RESPONSE_TIME_IN_SECONDS * 100;

    public static final String OTP = "1234";
    public static final String SIGN_IN_OTP = "123123";

    public static final int pageNumber = 1;
}

