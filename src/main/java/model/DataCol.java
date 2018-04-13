package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "data_col")
public class DataCol extends BaseModel<DataCol> {

    private static final long serialVersionUID = 1L;
    public static final DataCol dao = new DataCol();
}