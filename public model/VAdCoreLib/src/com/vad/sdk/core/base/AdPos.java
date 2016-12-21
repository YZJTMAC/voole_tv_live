package com.vad.sdk.core.base;

import java.io.Serializable;
import java.util.List;

/**
 * 广告位,一个广告位有1~n个广告,当此广告位不配置任何广告时,此时广告位中的广告数据为空,但是reportvalue不为空
 *
 * @author luojunsheng
 * @date 2016年5月6日 上午11:33:28
 * @version 1.1
 */
public class AdPos implements Serializable {
  private static final long serialVersionUID = 8202166613851556371L;
  /** 对应xml的pos字段,根据此字段判断是何种类型的广告,例如17101210 */
  public String id;
  /** 该广告位展示延迟时间 */
  public String startTime;
  /** 该广告位展示总时间 */
  public String allLength;
  /** 广告展示位置,计算规则:id的最后2位减9 */
  public int indexId;
  /** 该广告位下面的广告集合 */
  public List<MediaInfo> mediaInfoList;

  public String reportUrl;

  public String version;
  public String adInterface;
}
