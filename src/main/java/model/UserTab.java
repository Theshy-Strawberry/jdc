package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "user_tab")
public class UserTab extends BaseModel<UserTab> {

    private static final long serialVersionUID = 1L;
    public static final UserTab dao = new UserTab();
}