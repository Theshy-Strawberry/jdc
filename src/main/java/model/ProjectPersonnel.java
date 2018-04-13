package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by liu on 2016/6/27.
 */
@TableMapping(value = "project_personnel")
public class ProjectPersonnel extends BaseModel<ProjectPersonnel> {
    public static final ProjectPersonnel dao = new ProjectPersonnel();
}
