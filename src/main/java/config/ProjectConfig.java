package config;

import com.jfinal.config.*;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.cache.EhCache;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.JsonRender;
import com.jfinal.render.Render;
import com.senyoboss.common.AuthInterceptor;
import com.senyoboss.common.ExceptionInterceptor;
import com.senyoboss.common.FieldConst;
import com.senyoboss.ext.kit.CFKit;
import com.senyoboss.ext.kit.ConfigKit;
import com.senyoboss.ext.plugin.ConfigPlugin;
import com.senyoboss.ext.plugin.ControllerPlugin;
import com.senyoboss.ext.plugin.TablelPlugin;
import com.senyoboss.handler.CORSHandler;
import com.senyoboss.ioc.AutowiredInterceptor;
import com.senyoboss.ioc.Ioc;
import listener.JFinalStartListener;

import java.util.Map;

/**
 * API引导式配置
 */
public class ProjectConfig extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		new ConfigPlugin(".*.txt").reload(false).start();
		
		me.setDevMode(ConfigKit.getBoolean(FieldConst.conf_devMode, false));
//		me.setMainRenderFactory(new MyBeetlRenderFactory());

		me.setError404View("/error/404.html");
		me.setError401View("/error/401.html");
		me.setError403View("/error/403.html");
		me.setError500View("/error/500.html");

		/**
		 * 设置默认渲染json
		 */
        me.setMainRenderFactory(new IMainRenderFactory() {
			@Override
			public Render getRender(String view) {
				return new JsonRender();
			}
			@Override
			public String getViewExtension() {
				return null;
			}
		});
	}



	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {

		String[] dbConfigs = ConfigKit.getStr(FieldConst.db_configs).split(",");
		if (dbConfigs.length == 0) {
            throw new RuntimeException("Unable to find any database config. ");
        }
		
		//可配置多数据源
		Map<String, ActiveRecordPlugin> arpMap = CFKit.newMap();

		int initialSize = ConfigKit.getInt(FieldConst.db_initialSize, 50);
		int minIdle = ConfigKit.getInt(FieldConst.db_minIdle, 50);
		int maxActive = ConfigKit.getInt(FieldConst.db_maxActive, 100);

		for (String config : dbConfigs) {
			String dbType = ConfigKit.getStr(getKey(config, FieldConst.db_type));
			String jdbcUrl = ConfigKit.getStr(getKey(config, FieldConst.db_jdbcUrl));
			String user = ConfigKit.getStr(getKey(config, FieldConst.db_user));
			String password = ConfigKit.getStr(getKey(config, FieldConst.db_password));
			String driverClass = ConfigKit.getStr(getKey(config, FieldConst.db_driverClass), null);
			DruidPlugin druidPlugin = null;
			if (driverClass != null) {
				druidPlugin = new DruidPlugin(jdbcUrl, user, password);
			} else {
				druidPlugin = new DruidPlugin(jdbcUrl, user, password, driverClass);
			}
			druidPlugin.set(initialSize, minIdle, maxActive);
			druidPlugin.setValidationQuery("select 1 from dual");
			me.add(druidPlugin);
			
			ActiveRecordPlugin arp = new ActiveRecordPlugin(config, druidPlugin);
			arp.setShowSql(true);
			if (dbType.equalsIgnoreCase("mysql")) {
				arp.setDialect(new MysqlDialect());
                arp.setCache(new EhCache());
            } else if (dbType.equalsIgnoreCase("oracle")) {
				arp.setDialect(new OracleDialect());
                arp.setCache(new EhCache());
				arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
			} else if (dbType.equalsIgnoreCase("postgresql")) {
				arp.setDialect(new PostgreSqlDialect());
			}
			me.add(arp);
			arpMap.put(config, arp);

		}
		new TablelPlugin(arpMap).start();

        /**
         * IOC
         */
        Ioc ioc = Ioc.getIoc();
        me.add(ioc);
        //自动扫描business下有@Server注解的类，为single模式
        ioc.addPackage("business", true);
        /**
         * 缓存
		 */
		me.add(new EhCachePlugin());
		/**
		 * 任务调度
		 */
		QuartzPlugin quartzPlugin = new QuartzPlugin("job.properties");
		me.add(quartzPlugin);
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		/**
		 * 配置基于普通Action的协议
		 * 路由扫描注册
		 * new ControllerPlugin(me).start();
		 */
		new ControllerPlugin(me).start();
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		/**
		 * 支持在jfinal中用sesion
		 */
		me.add(new SessionInViewInterceptor());
        /**
         * 配置全局拦截器，当controller里字段加@Autowired注解后自动根据其类型或value自动注入
         */
        me.add(new AutowiredInterceptor());
		/**
		 * 配置全局拦截器，进行用户登陆控制
		 */
//		me.add(new AuthInterceptor());
		/**
		 * 配置全局拦截器，进行异常处理
		 */
		me.add(new ExceptionInterceptor());
	}

	public ProjectConfig() {}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		/**
		 * 如果使用Rest协议路由扫描方式，则需要配置handler。否则请注掉此handler
		 * 配置Rest-handler
		 */
		me.add(new CORSHandler());
	}

	//在系统启动时调用的方法
	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		super.afterJFinalStart();
		new JFinalStartListener().init();
	}

	private String getKey(String config, String key) {
		return String.format("%s.%s", config, key);
	}

}
