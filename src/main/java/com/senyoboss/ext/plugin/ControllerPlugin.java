package com.senyoboss.ext.plugin;

import java.util.List;

import com.jfinal.config.Routes;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ext.kit.CFKit;
import com.senyoboss.ext.kit.ClassSearchKit;

/**
 * 扫描Controller上的注解，绑定Controller和controllerKey及配置viewPath
 */
public class ControllerPlugin implements IPlugin {

	protected final Logger log = Logger.getLogger(getClass());

	private Routes me;

	public ControllerPlugin(Routes me) {
		this.me = me;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean start() {
		// 查询所有继承BaseController的类
		List<String> scanPaths = CFKit.newList();
		scanPaths.add("controller");
		
		List<Class<? extends BaseController>> controllerClasses = ClassSearchKit
				.of(BaseController.class).scanPackages(scanPaths).search();
		// 循环处理自动注册映射
		for (Class controller : controllerClasses) {
			// 获取注解对象
			ControllerMapping controllerMapping = (ControllerMapping) controller
					.getAnnotation(ControllerMapping.class);
			if (controllerMapping == null) {
				log.error(controller.getName()
						+ "继承了BaseController，但是没有注解绑定映射路径");
				continue;
			}

			// 获取映射路径数组
			String[] controllerKeys = controllerMapping.value();
			String viewPath = controllerMapping.path();
			boolean hasViewPath = viewPath.length() > 0;
			for (String controllerKey : controllerKeys) {
				controllerKey = controllerKey.trim();
				if (controllerKey.equals("")) {
					log.error(controller.getName() + "注解错误，映射路径为空");
					continue;
				}
				// 注册映射
				if (hasViewPath) {
					me.add(controllerKey, controller, viewPath);
				} else {
					me.add(controllerKey, controller);
				}
			}
		}
		log.info("Controllers initialization is complete.");
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
