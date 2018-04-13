package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "sys_user_auth")
public class SysUserAuth extends BaseModel<SysUserAuth> {

    private static final long serialVersionUID = 1L;
    public static final SysUserAuth dao = new SysUserAuth();
}