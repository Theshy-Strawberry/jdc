package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@TableMapping(value = "visit_way")
public class VisitWay extends BaseModel<VisitWay> {
    public static final VisitWay dao = new VisitWay();
}