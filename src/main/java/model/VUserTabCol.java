package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "v_user_tab_col")
public class VUserTabCol extends BaseModel<VUserTabCol> {

    private static final long serialVersionUID = 1L;
    public static final VUserTabCol dao = new VUserTabCol();
}