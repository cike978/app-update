package com.cike978.appupdate.uitls;


import java.util.regex.Pattern;

/**
 * 版本对比
 */
public class VersionCompareUtils {

    //匹配三位语义化版本字符串
    private static final Pattern SEMANTIC_VERSION_REGEX = Pattern.compile("^([0]|[1-9][0-9]*)(\\.)([0]|[1-9][0-9]*)(\\.)([0]|[1-9][0-9]*)$");

    /**
     * 简单版本比较处理（必须符合VERSION_REGEX正则表达式）
     *
     * @param firstVersion
     * @param secondVersion
     * @return
     */
    public static int compareVersion(String firstVersion, String secondVersion) {

        if (SEMANTIC_VERSION_REGEX.matcher(firstVersion).matches() && SEMANTIC_VERSION_REGEX.matcher(secondVersion).matches()) {
            return compareSemanticVersion(firstVersion, secondVersion);
        }
        throw new RuntimeException("版本号格式错误！！必须为 xx.xx.xx格式");
    }

    /**
     * 版本比较处理（严格按照语义化版本文档比较）
     *
     * @param firstVersion
     * @param secondVersion
     * @return
     */
    private static int compareSemanticVersion(String firstVersion, String secondVersion) {
        String[] versionArray1;
        String[] versionArray2;
        try {
            versionArray1 = convertArray(firstVersion);
            versionArray2 = convertArray(secondVersion);
        } catch (Exception e) {
            return -1;
        }
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        while (idx < minLength) {
            if (idx < 3) {
                diff = Integer.parseInt(versionArray1[idx]) - Integer.parseInt(versionArray2[idx]);
            }
            if (diff != 0) {
                break;
            }
            idx++;
        }
        diff = (diff != 0) ? diff : secondVersion.length() - firstVersion.length();
        return diff;
    }


    private static String[] convertArray(String appVersion) {
        if (!SEMANTIC_VERSION_REGEX.matcher(appVersion).matches() || appVersion.contains("..") || appVersion.contains("--")) {
            throw new RuntimeException("Incorrect version information [" + appVersion + "] format");
        }
        String[] arrays = appVersion.split("\\.");
        return arrays;

    }


    /**
     * 校验版本是否合法
     *
     * @param version
     * @return
     */
    public static boolean verifyVersion(String version) {
        if (SEMANTIC_VERSION_REGEX.matcher(version).matches()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        compareVersion("1.0.1", "1.0.2");
    }
}
