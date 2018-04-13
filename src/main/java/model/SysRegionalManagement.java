package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/17.
 */
@TableMapping("sys_regional_management")
public class SysRegionalManagement extends BaseModel<SysRegionalManagement> {
    public static final SysRegionalManagement dao = new SysRegionalManagement();

}
