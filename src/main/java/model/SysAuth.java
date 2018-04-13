package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "sys_auth")
public class SysAuth extends BaseModel<SysAuth> {

    private static final long serialVersionUID = 1L;
    public static final SysAuth dao = new SysAuth();
}