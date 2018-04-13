package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "tab_permission")
public class TabPermission extends BaseModel<TabPermission> {

    private static final long serialVersionUID = 1L;
    public static final TabPermission dao = new TabPermission();
}