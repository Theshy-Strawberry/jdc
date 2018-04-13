package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

@TableMapping(value = "data_tab")
public class DataTab extends BaseModel<DataTab> {

    private static final long serialVersionUID = 1L;
    public static final DataTab dao = new DataTab();
}