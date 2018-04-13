package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "sys_log")
public class SysLog extends BaseModel<SysLog> {

    private static final long serialVersionUID = 1L;
    public static final SysLog dao = new SysLog();
}