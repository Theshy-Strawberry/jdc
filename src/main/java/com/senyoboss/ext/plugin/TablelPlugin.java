package com.senyoboss.ext.plugin;

import java.util.List;
import java.util.Map;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.senyoboss.ext.annotation.TableMapping;
import com.senyoboss.ext.kit.CFKit;
import com.senyoboss.ext.kit.ClassSearchKit;
import com.senyoboss.model.BaseModel;

/**
 * 扫描model上的注解，绑定model和table
 * 
 * @author 董华健
 */
public class TablelPlugin implements IPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	private Map<String, ActiveRecordPlugin> arpMap;

	public TablelPlugin(Map<String, ActiveRecordPlugin> arpMap) {
		this.arpMap = arpMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean start() {
		List<String> scanPaths = CFKit.newList();
		scanPaths.add("model");
		List<Class<? extends BaseModel>> modelClasses = ClassSearchKit.of(
				BaseModel.class).scanPackages(scanPaths).search();// 查询所有继承BaseModel的类
		// 循环处理自动注册映射
		for (Class model : modelClasses) {
			// 获取注解对象
			TableMapping tableMapping = (TableMapping) model
					.getAnnotation(TableMapping.class);
			if (tableMapping == null) {
				log.error(model.getName() + "继承了BaseModel，但是没有注解绑定表名 。");
				break;
			}

			// 获取映射属性
			String dataSourceName = tableMapping.ds().trim();
			String tableName = tableMapping.value().trim();
			String pkName = tableMapping.pk().trim();
			if (dataSourceName.equals("") || tableName.equals("")
					|| pkName.equals("")) {
				log.error(model.getName() + "注解错误，数据源、表名、主键名为空 。");
				break;
			}

			// 映射注册
			ActiveRecordPlugin arp = arpMap.get(dataSourceName);
			if (arp == null) {
				log.error(model.getName() + "ActiveRecordPlugin不能为null。");
				break;
			}
			arp.addMapping(tableName, pkName, model);
		}
		log.info("Tables initialization is complete.");
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
