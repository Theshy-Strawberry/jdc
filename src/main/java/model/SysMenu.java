package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "sys_menu")
public class SysMenu extends BaseModel<SysMenu> {

    private static final long serialVersionUID = 1L;
    public static final SysMenu dao = new SysMenu();
}