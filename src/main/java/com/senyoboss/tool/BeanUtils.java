package com.senyoboss.tool;

import java.util.Map;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.Converter;

/**
 * 实体工具类，目前copy不支持map、list和model
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class BeanUtils {

	/**
	 * 实例化对象
	 * @param clazz 类
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 实例化对象
	 * @param clazz 类名
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * copy 对象属性到另一个对象，默认不使用Convert
	 * @param src
	 * @param clazz 类名
	 * @return T
	 */
	public static <T> T copy(Object src, Class<T> clazz) {
		BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);

		T to = newInstance(clazz);
		copier.copy(src, to, null);
		return to;
	}

	/**
	 * copy 对象属性到另一个对象
	 * @param src 源对象
	 * @param clazz 生成的对象Class
	 * @param converter 自定义转换器
	 * @return
	 */
	public static <T> T copy(Object src, Class<T> clazz, Converter converter) {
		BeanCopier copier = BeanCopier.create(src.getClass(), clazz, true);

		T to = newInstance(clazz);
		copier.copy(src, to, converter);
		return to;
	}

	/**
	 * 拷贝对象
	 * @param src 源对象
	 * @param dist 需要赋值的对象
	 */
	public static void copy(Object src, Object dist) {
		BeanCopier copier = BeanCopier
				.create(src.getClass(), dist.getClass(), false);

		copier.copy(src, dist, null);
	}

	/**
	 * 拷贝对象
	 * @param src 源对象
	 * @param dist 需要赋值的对象
	 * @param converter 自定义转换器
	 * @return
	 */
	public static void copy(Object src, Object dist, Converter converter) {
		BeanCopier copier = BeanCopier
				.create(src.getClass(), dist.getClass(), true);
		
		copier.copy(src, dist, converter);
	}

	/**
	 * 将对象装成map形式
	 * @param src
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map toMap(Object src) {
		return BeanMap.create(src);
	}

}
