package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/25.
 */
@TableMapping(value = "frame_contract")
public class FrameContract extends BaseModel<FrameContract>{
    public static final FrameContract dao = new FrameContract();

}
