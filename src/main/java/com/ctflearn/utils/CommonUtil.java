package com.ctflearn.utils;

import com.ctflearn.exception.AutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CommonUtil {

    private static final Logger logger = LogManager.getLogger(CommonUtil.class.getSimpleName());

    private CommonUtil() {
    }

    /**
     * Sleep seconds.
     *
     * @param seconds the seconds
     *
     * @throws AutomationException the automation exception
     */
    public static void sleepSeconds(int seconds) throws AutomationException {
        sleepMilliseconds(seconds * 1000L);
    }

    /**
     * Sleep milliseconds.
     *
     * @param milliseconds the milliseconds
     *
     * @throws AutomationException the automation exception
     */
    public static void sleepMilliseconds(long milliseconds) throws AutomationException {
        try {
            long interval = 1000;
            while (milliseconds > 0) {
                logger.debug("Sleeping for {} milliSeconds.", milliseconds);
                Thread.sleep(Math.min(milliseconds, interval));
                milliseconds -= interval;
            }
        } catch (InterruptedException e) {
            throw new AutomationException(e);
        }
    }

}
