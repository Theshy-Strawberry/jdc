package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/14.
 */
@TableMapping(value = "pos_info")
public class PosInfo extends BaseModel<PosInfo>{
    public static final PosInfo dao =new PosInfo();
}
