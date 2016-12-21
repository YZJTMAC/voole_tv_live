/**
 *
 */
package com.vad.sdk.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luojunsheng
 * @date 2016年5月5日 下午4:48:45
 * @version 1.1
 */
public class AdInfo implements Serializable {
  private static final long serialVersionUID = 2431894123798666168L;
  public String version;// 版本
  public String cdnType;// CDN 类型
  public String status;// 在client与server交互成功的情况下恒为0
  public String resultDesc;// 数据描述,有时描述会放在CDATA区中,如<resultdesc><![CDATA[操作成功]]></resultdesc>
  public List<AdPos> adPostions; // 广告位

  public AdInfo() {
    adPostions = new ArrayList<AdPos>();
  }
}
