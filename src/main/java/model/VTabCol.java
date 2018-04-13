package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "v_tab_col")
public class VTabCol extends BaseModel<VTabCol> {

    private static final long serialVersionUID = 1L;
    public static final VTabCol dao = new VTabCol();
}