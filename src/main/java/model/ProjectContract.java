package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by 冕寒 on 2016/6/25. 渠道项目合同
 */
@TableMapping(value = "project_contract")
public class ProjectContract extends BaseModel<ProjectContract> {
    public static final ProjectContract dao = new ProjectContract();
}
