package com.ctflearn.utils;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Random value generator.
 */
public final class RandomValueGenerator {

    /**
     * The constant WEBSITE_STRING_REGEX.
     */
    public static final String WEBSITE_STRING_REGEX = "\\b(https)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    /**
     * The constant REGISTRATION_NUMBER_REGEX.
     */
    public static final String REGISTRATION_NUMBER_REGEX = "^([0-9]{9}[a-zA-Z]{1})$";
    private static final int LOW_INT = 1;
    private static final int HIGH_INT = 10000;
    private static final String NUMBER = "1234567890";
    private static final String STRING_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String STRING_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String STRING_SPECIAL = "`~!@#$%^&*()_+-=[]\\{}|;':\",./<>?";

    private static final Random random = new Random();

    /**
     * Gets random alpha number string.
     *
     * @return the random alpha number string
     */
    public static String getRandomAlphaNumberString() {
        return getRandomAlphaNumberStringFixedLength(16);
    }

    /**
     * Gets random alpha + number string.
     *
     * @param prefix the prefix
     *
     * @return the random alpha number string
     */
    public static String getRandomAlphaNumberString(String prefix) {
        return prefix + "_" + getRandomAlphaNumberString();
    }

    /**
     * Gets random alpha + number string.
     *
     * @param prefix       the prefix
     * @param suffixLength the suffix length
     *
     * @return the random alpha number string
     */
    public static String getRandomAlphaNumberString(String prefix, int suffixLength) {
        String randomString = getRandomAlphaNumberString();

        if (suffixLength < randomString.length()) {
            randomString = randomString.substring(0, suffixLength);
        }

        return prefix + "_" + randomString;
    }

    /**
     * Gets random alpha + number string fixed length.
     *
     * @param len the len
     *
     * @return the random alpha number string fixed length
     */
    public static String getRandomAlphaNumberStringFixedLength(int len) {
        return getRandomFixedLength(NUMBER + STRING_LOWER + STRING_UPPER, len);
    }

    /**
     * Gets random string fixed length.
     *
     * @param len the len
     *
     * @return the random string fixed length
     */
    public static String getRandomStringFixedLength(int len) {
        return getRandomFixedLength(STRING_LOWER + STRING_UPPER, len);
    }

    /**
     * Gets random string fixed length with space.
     *
     * @param len      the len
     * @param spaceLen the space len
     *
     * @return the random string fixed length with space
     */
    public static String getRandomStringFixedLengthWithSpace(int len, int spaceLen) {
        return getRandomFixedLengthWithSpace(STRING_LOWER + STRING_UPPER, len, spaceLen);
    }

    /**
     * Gets random alpha + number string fixed length with space.
     *
     * @param len      the len
     * @param spaceLen the space len
     *
     * @return the random alpha number string fixed length with space
     */
    public static String getRandomAlphaNumberStringFixedLengthWithSpace(int len, int spaceLen) {
        return getRandomFixedLengthWithSpace(NUMBER + STRING_LOWER + STRING_UPPER, len, spaceLen);
    }

    /**
     * Gets random alpha + number + special string fixed length with space.
     *
     * @param len      the len
     * @param spaceLen the space len
     *
     * @return the random alpha number special string fixed length with space
     */
    public static String getRandomAlphaNumberSpecialStringFixedLengthWithSpace(int len, int spaceLen) {
        return getRandomFixedLengthWithSpace(NUMBER + STRING_LOWER + STRING_UPPER + STRING_SPECIAL, len, spaceLen);
    }

    /**
     * Gets random string in list.
     *
     * @param len  the len
     * @param list the list
     *
     * @return the random string in list
     */
    public static String getRandomStringInList(int len, String list) {
        return getRandomFixedLength(list, len);
    }

    /**
     * Gets random email.
     *
     * @param recipientNameLen the recipient name len
     * @param domainLen        the domain len
     *
     * @return the random email
     */
    public static String getRandomEmail(int recipientNameLen, int domainLen) {
        return getRandomEmail(recipientNameLen, domainLen, 3);
    }

    /**
     * Gets random email.
     *
     * @return the random email
     */
    public static String getRandomEmail() {
        return getRandomEmail(getRandomNumber(5, 10), getRandomNumber(3, 7), getRandomNumber(3));
    }

    /**
     * Gets random email.
     *
     * @param recipientNameLen the recipient name len
     *
     * @return the random email
     */
    public static String getRandomEmail(int recipientNameLen) {
        String recipientName = getRandomStringFixedLength(recipientNameLen).toLowerCase(Locale.ROOT);
        return recipientName + "@" + "aspireqa.com";
    }

    /**
     * Gets random email.
     *
     * @param recipientNameLen the recipient name len
     * @param domainLen        the domain len
     * @param topDomainLen     the top domain len
     *
     * @return the random email
     */
    public static String getRandomEmail(int recipientNameLen, int domainLen, int topDomainLen) {
        String recipientName = getRandomStringFixedLength(recipientNameLen).toLowerCase(Locale.ROOT);
        String domain = getRandomStringFixedLength(domainLen).toLowerCase(Locale.ROOT);
        String topDomain = getRandomStringFixedLength(topDomainLen).toLowerCase(Locale.ROOT);

        return recipientName + "@" + domain + "." + topDomain;
    }

    private static String getRandomFixedLength(String alphabet, int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        }

        return sb.toString();
    }

    private static String getRandomFixedLengthWithSpace(String alphabet, int len, int spaceLen) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        int space = 1;
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
            if (space >= spaceLen) {
                sb.append(" ");
                space = 1;
            } else {
                space++;
            }
        }

        return sb.toString();
    }

    /**
     * Gets random number.
     *
     * @param high the high
     *
     * @return the random number
     */
    public static int getRandomNumber(int high) {
        return getRandomNumber(LOW_INT, high);
    }

    /**
     * Gets random number.
     *
     * @return the random number
     */
    public static int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(LOW_INT, HIGH_INT + 1);
    }

    /**
     * Gets random number.
     *
     * @param low  the low
     * @param high the high
     *
     * @return the random number
     */
    public static int getRandomNumber(int low, int high) {
        return ThreadLocalRandom.current().nextInt(low, high + 1);
    }

    /**
     * Get random number set int [ ].
     *
     * @param high  the high
     * @param count the count
     *
     * @return the int [ ]
     */
    public static int[] getRandomNumberSet(int high, int count) {
        int[] numbers = new int[count];
        for (int i = 0; i < count; i++) {
            numbers[i] = random.nextInt(high);
        }

        return numbers;
    }

    /**
     * Gets random date in range.
     *
     * @param start the start
     * @param end   the end
     *
     * @return the random date in range
     */
    public static Date getRandomDateInRange(Date start, Date end) {
        return new Date(randomNumberInRange(start.getTime(), end.getTime()));
    }

    /**
     * Gets random enum.
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     *
     * @return the random enum
     */
    public static <T extends Enum<?>> T getRandomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);

        return clazz.getEnumConstants()[x];
    }

    /**
     * Gets random number fixed length.
     *
     * @param charLength the char length
     *
     * @return the random number fixed length
     */
    public static String getRandomNumberFixedLength(int charLength) {
        if (charLength < 1) {
            return "";
        } else {
            StringBuilder k = new StringBuilder();
            for (int i = 0; i < charLength; i++) {
                k.append(getRandomNumber(0, 9));
            }
            return k.toString();
        }
    }

    /**
     * Gets random string in list.
     *
     * @param list the list
     *
     * @return the random string in list
     */
    public static String getRandomStringInList(String[] list) {
        return getRandomStringItemInList(Arrays.asList(list));
    }

    /**
     * Gets random string item in list.
     *
     * @param list the list
     *
     * @return the random string item in list
     */
    public static String getRandomStringItemInList(List<String> list) {
        int pos = getRandomNumber(0, list.size() - 1);
        Collections.shuffle(list);

        return list.get(pos);
    }

    /**
     * Gets random boolean.
     *
     * @return the random boolean
     */
    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    /**
     * Random number int.
     *
     * @return the int
     */
    public static int randomNumber() {
        return randomNumber(50);
    }

    /**
     * Random number int.
     *
     * @param bound the bound
     *
     * @return the int
     */
    public static int randomNumber(int bound) {
        return RandomValueGenerator.getRandomNumber(Math.max(bound, 1));
    }

    /**
     * Random number in range int.
     *
     * @param range the range
     *
     * @return the int
     */
    public static int randomNumberInRange(int range) {
        return (randomNumber() % range);
    }

    /**
     * Random number in range long.
     *
     * @param start the start
     * @param end   the end
     *
     * @return the long
     */
    public static long randomNumberInRange(long start, long end) {
        return ThreadLocalRandom.current().nextLong(start, end);
    }

    /**
     * Random object in list t.
     *
     * @param <T>  the type parameter
     * @param list the list
     *
     * @return the t
     */
    public static <T> T randomObjectInList(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }

        return list.get(randomNumber(list.size()) - 1);
    }

    /**
     * Random object in list with ignored items t.
     *
     * @param <T>          the type parameter
     * @param list         the list
     * @param ignoredItems the ignored items
     *
     * @return the t
     */
    public static <T> T randomObjectInListWithIgnoredItems(List<T> list, List<T> ignoredItems) {
        if (ignoredItems.isEmpty()) {
            return randomObjectInList(list);
        }

        List<T> remove = new ArrayList<>(list);
        try {
            remove.removeAll(ignoredItems);

            return randomObjectInList(remove);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
