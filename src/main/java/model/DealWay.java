package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@TableMapping(value = "deal_way")
public class DealWay extends BaseModel<DealWay> {
    public static final DealWay dao = new DealWay();
}