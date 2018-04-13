package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/15.
 */
@TableMapping(value = "store_info")
public class StoreInfo extends BaseModel<StoreInfo> {
    public  static final StoreInfo dao =new StoreInfo();
}
