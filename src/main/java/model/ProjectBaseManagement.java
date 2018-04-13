package model;

/**
 * Created by 冕寒 on 2016/6/23.
 */

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "project_base_management")
public class ProjectBaseManagement extends BaseModel<ProjectBaseManagement> {
    public static final ProjectBaseManagement dao = new ProjectBaseManagement();
}
