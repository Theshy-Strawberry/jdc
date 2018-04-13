package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "sys_user")
public class SysUser extends BaseModel<SysUser> {

    private static final long serialVersionUID = 1L;
    public static final SysUser dao = new SysUser();
}