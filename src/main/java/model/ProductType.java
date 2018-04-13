package model;

import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.model.BaseModel;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@TableMapping(value = "product_type")
public class ProductType extends BaseModel<ProductType> {
    public static final ProductType dao = new ProductType();
}
