package com.vad.sdk.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version 1
 * @author zhangzexin
 * @since 2015-9-23 上午10:14:18
 */

public class AdRegister implements Serializable {
  private static final long serialVersionUID = 166966939875149609L;
  public String status;
  public String resultdesc;
  public String defaultreporturl;
  public String defaultadinf;
  public String adparams;
  public List<Adposinfo> adposs;
  // NOTE(ljs):为啥在注册接口比对posId做拦截操作,衍生出此字段
  public List<String> posIds;

  public AdRegister() {
    status = "";
    resultdesc = "";
    defaultreporturl = "";
    defaultadinf = "";
    adparams = "";
    adposs = new ArrayList<AdRegister.Adposinfo>();
    posIds = new ArrayList<String>();
  }

  public class Adposinfo implements Serializable {
    private static final long serialVersionUID = 8652397447925969547L;
    // public String defaultreporturl;
    // public String defaultadinf;
    public String pos;
    public String frequency;
    public String type;
    public String gtype;

    public Adposinfo() {
      pos = "";
      frequency = "";
      type = "";
      gtype = "";
    }
  }

  public Adposinfo getAdpos() {
    Adposinfo adpos = new Adposinfo();
    return adpos;
  }

  public boolean isContainPosId(String posId) {
    if (posId.length() == 6) {
      for (String posIdTmp : posIds) {
        if (posIdTmp.contains(posId)) {
          return true;
        }
      }
    }
    if (posId.length() == 8) {
      return posIds.contains(posId);
    }
    return false;
  }
}
