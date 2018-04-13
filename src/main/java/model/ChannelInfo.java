package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/15.
 */
@TableMapping(value = "channel_info")
public class ChannelInfo extends BaseModel<ChannelInfo>{
    public  static final ChannelInfo dao =new ChannelInfo();
}
