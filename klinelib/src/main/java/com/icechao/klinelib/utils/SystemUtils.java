package com.icechao.klinelib.utils;


//从common-lang复制部分
public class SystemUtils {

    public static boolean isChooseReleaseEnvironment = false;

    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");

    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");

    private static String getSystemProperty(final String property) {
        try {
            return System.getProperty(property);
        } catch (final SecurityException ex) {
            // we are not allowed to look at this property
            System.err.println("Caught a SecurityException reading the system property '" + property
                    + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    /**
     * <p>
     * Decides if the Java version matches.
     * </p>
     *
     * @param versionPrefix the prefix for the java version
     * @return true if matches, or false if not or can't determine
     */
    private static boolean getJavaVersionMatches(final String versionPrefix) {
        return isJavaVersionMatch(JAVA_SPECIFICATION_VERSION, versionPrefix);
    }

    static boolean isJavaVersionMatch(final String version, final String versionPrefix) {
        if (version == null) {
            return false;
        }
        return version.startsWith(versionPrefix);
    }

    public static boolean isDebugAndReleaseEnvironment() {
        return false;
    }
}
