package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "tab_col")
public class TabCol extends BaseModel<TabCol> {

    private static final long serialVersionUID = 1L;
    public static final TabCol dao = new TabCol();
}