package com.ctflearn.constants;

import lombok.Getter;

public enum ToastMessageConstant {
    LOGIN_SUCCESS("Successfully logged in"),
    LOGOUT_SUCCESS("Successfully logged out"),
    SUBMIT_FOR_VERIFICATION_SUCCESS("Submitted for verification!");

    @Getter
    private final String name;

    ToastMessageConstant(String name) {
        this.name = name;
    }
}