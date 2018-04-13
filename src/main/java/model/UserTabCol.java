package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "user_tab_col")
public class UserTabCol extends BaseModel<UserTabCol> {

    private static final long serialVersionUID = 1L;
    public static final UserTabCol dao = new UserTabCol();
}