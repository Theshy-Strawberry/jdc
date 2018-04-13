package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@TableMapping(value = "business_main_part")
public class BusinessMainPart extends BaseModel<BusinessMainPart> {
    public static final BusinessMainPart dao = new BusinessMainPart();

}