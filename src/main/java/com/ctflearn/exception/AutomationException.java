package com.ctflearn.exception;

import com.ctflearn.utils.ExtentManager;

public class AutomationException extends Exception {

    /**
     * Instantiates a new Automation exception.
     *
     * @param message the message
     */
    public AutomationException(String message) {
        super(message);
        if (ExtentManager.getTest() != null && ExtentManager.isError())
            ExtentManager.getTest().fail(message);
    }

    /**
     * Instantiates a new Automation exception.
     *
     * @param message the message
     * @param strings the strings
     */
    public AutomationException(String message, Object... strings) {
        super(String.format(message, strings));
        if (ExtentManager.getTest() != null && ExtentManager.isError())
            ExtentManager.getTest().fail(String.format(message, strings));
    }

    /**
     * Instantiates a new Automation exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public AutomationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Instantiates a new Automation exception.
     *
     * @param message   the message
     * @param throwable the throwable
     * @param strings   the strings
     */
    public AutomationException(String message, Throwable throwable, Object... strings) {
        super(String.format(message, strings), throwable);
    }

    /**
     * Instantiates a new Automation exception.
     *
     * @param throwable the throwable
     */
    public AutomationException(Throwable throwable) {
        super(throwable);
    }

}
