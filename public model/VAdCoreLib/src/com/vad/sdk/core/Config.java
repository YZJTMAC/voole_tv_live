/**
 *
 */
package com.vad.sdk.core;

/**
 *
 * @author luojunsheng
 * @date 2016年6月28日 下午3:57:07
 * @version 1.1
 */
public final class Config {
  /**
   * 对导流SDK加入版本控制,方便日志输出
   */
  public static final String SDK_VERSION_CODE = "3.0.1-2016-1111-13:10:00-release";

  public static final String SHARED_PREFERENCES_SET = "shared_preferences_set";
  public static final String SHARED_PREFERENCES_SDK_VERSION_CODE = "sdk_version_code";
  // apk启动相关shareprefrece
  public static final String SHARED_PREFERENCES_BOOT_AD_ADPOS = "boot_ad_pos";// 10
  public static final String SHARED_PREFERENCES_BOOT_AD_ADPOSID = "boot_ad_posid";// 11101010
  public static final String SHARED_PREFERENCES_BOOT_AD_LENGTH = "boot_ad_length";
  public static final String SHARED_PREFERENCES_BOOT_AD_SOURCE = "boot_ad_source";
  public static final String SHARED_PREFERENCES_BOOT_AD_REPORTVALUE = "boot_ad_reportvalue";
  public static final String SHARED_PREFERENCES_BOOT_AD_REPORTURL = "boot_ad_reportUrl";
  public static final String SHARED_PREFERENCES_BOOT_AD_AMID = "boot_ad_amid";
  public static final String SHARED_PREFERENCES_BOOT_AD_SAVE = "boot_ad_save";
  public static final String SHARED_PREFERENCES_APP_IS_FIRST = "app_is_first";
  public static final String SHARED_PREFERENCES_APP_OEMID_BAN = "app_oemid_ban";// 导流禁止oemid
  public static final String SHARED_PREFERENCES_DOWN_NAME = "app_name";
  public static final String SHARED_PREFERENCES_DOWN_LODING = "app_down_loding";
  public static boolean mdownLoding = false;
  public static String mdownUrl = "";
}
