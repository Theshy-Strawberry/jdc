package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@TableMapping(value = "pay_subject")
public class PaySubject extends BaseModel<PaySubject> {
    public static final PaySubject dao = new PaySubject();
}
