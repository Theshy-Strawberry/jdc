package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by 冕寒 on 2016/6/15.
 */
//TableMapping 对应表名  职位功能
@TableMapping(value = "job_title")
//固定格式 类似于entity
public class JobTitle extends BaseModel<JobTitle>{
        public static final JobTitle dao = new JobTitle();
}
