package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "v_user_tab")
public class VUserTab extends BaseModel<VUserTab> {

    private static final long serialVersionUID = 1L;
    public static final VUserTab dao = new VUserTab();
}