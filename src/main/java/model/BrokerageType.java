package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by 冕寒 on 2016/6/8.
 *getter/setter
 */
//TableMapping 对应表名
@TableMapping(value = "brokerage_type")
//固定格式 类似于entity
public class BrokerageType extends BaseModel<BrokerageType> {
    public static final BrokerageType dao = new BrokerageType();
}
