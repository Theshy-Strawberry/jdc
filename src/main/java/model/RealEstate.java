package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by 冕寒 on 2016/6/15.real_estate
 */
@TableMapping(value = "real_estate")
public class RealEstate extends BaseModel<RealEstate> {
    public static final RealEstate dao = new RealEstate();
}
