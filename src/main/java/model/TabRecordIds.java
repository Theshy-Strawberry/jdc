package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "tab_record_ids")
public class TabRecordIds extends BaseModel<TabRecordIds> {

    private static final long serialVersionUID = 1L;
    public static final TabRecordIds dao = new TabRecordIds();
}