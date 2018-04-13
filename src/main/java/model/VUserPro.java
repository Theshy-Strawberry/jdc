package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "v_user_pro")
public class VUserPro extends BaseModel<VUserPro> {

    private static final long serialVersionUID = 1L;
    public static final VUserPro dao = new VUserPro();
}