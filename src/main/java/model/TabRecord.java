package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "tab_record")
public class TabRecord extends BaseModel<TabRecord> {

    private static final long serialVersionUID = 1L;
    public static final TabRecord dao = new TabRecord();
}