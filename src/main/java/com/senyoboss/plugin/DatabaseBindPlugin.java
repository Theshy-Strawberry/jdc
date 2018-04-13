package com.senyoboss.plugin;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.senyoboss.tool.ScriptRunner;
import org.apache.commons.io.IOUtils;
import org.joor.Reflect;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

/**
 * 用于首次安装，自动安装数据库或者更新数据库（更新暂未完成）
 * 
 * 在src包中添加database/db.sql文件
 * 
 * example
 * <pre>
 * // 配置DatabaseBindPlugin插件
 * DatabaseBindPlugin dbp = new DatabaseBindPlugin(dp);
 * dbp.addMapping("xxx", "xx", Xx.class);
 * me.add(dbp);
 * </pre>
 * 
 * @title DatabaseBindPlugin.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class DatabaseBindPlugin extends ActiveRecordPlugin {

	public DatabaseBindPlugin(DataSource dataSource) {
		super(dataSource);
	}

	public DatabaseBindPlugin(Config config) {
		super(config);
	}

	public DatabaseBindPlugin(DataSource dataSource, int transactionLevel) {
		super(dataSource, transactionLevel);
	}

	public DatabaseBindPlugin(IDataSourceProvider dataSourceProvider,
			int transactionLevel) {
		super(dataSourceProvider, transactionLevel);
	}

	public DatabaseBindPlugin(IDataSourceProvider dataSourceProvider) {
		super(dataSourceProvider);
	}

	public DatabaseBindPlugin(String configName, DataSource dataSource,
			int transactionLevel) {
		super(configName, dataSource, transactionLevel);
	}

	public DatabaseBindPlugin(String configName, DataSource dataSource) {
		super(configName, dataSource);
	}

	public DatabaseBindPlugin(String configName,
			IDataSourceProvider dataSourceProvider, int transactionLevel) {
		super(configName, dataSourceProvider, transactionLevel);
	}

	public DatabaseBindPlugin(String configName,
			IDataSourceProvider dataSourceProvider) {
		super(configName, dataSourceProvider);
	}

	@Override
	public boolean start() {
		// 1.从父类中反射出dataSource
		Reflect reflect = Reflect.on(this);
		DataSource dataSource = reflect.get("dataSource");
		IDataSourceProvider dataSourceProvider = reflect.get("dataSourceProvider");

		if (dataSourceProvider != null)
			dataSource = dataSourceProvider.getDataSource();
		if (dataSource == null)
			throw new RuntimeException("DatabaseBindPlugin start error: DatabaseBindPlugin need DataSource or DataSourceProvider");

		// 数据库为空时，第一次执行sql，建表
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			String sql = "SHOW TABLES"; 

			ResultSet resultSet = statement.executeQuery(sql);
			List<String> tableList = new ArrayList<String>();
			while (resultSet.next()) {
				tableList.add(resultSet.getString(1));
			}
			if (tableList.isEmpty()) {
				InputStream input = DatabaseBindPlugin.class.getClassLoader().getResourceAsStream("database/db.sql");
				ScriptRunner scriptRunner = new ScriptRunner(connection);
				scriptRunner.setAutoCommit(true);
				scriptRunner.setStopOnError(true);
				// 运行sql文件
				scriptRunner.runScript(new StringReader(IOUtils.toString(input, Charset.forName("UTF-8"))));
			}
		} catch (Exception e) {
			 throw new RuntimeException(e.getMessage(), e);
		}
		return super.start();
	}

	@Override
	public boolean stop() {
		return super.stop();
	}

}
