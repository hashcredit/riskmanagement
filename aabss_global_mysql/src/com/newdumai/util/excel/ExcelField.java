package com.newdumai.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	String value() default "";

	/**
	 * 导出字段标题
	 */
	String title();

	/**
	 * 字段类型（0：导出导入；1：仅导出；2：仅导入）
	 */
	int type() default 0;

	/**
	 * 导出字段对齐方式（0：自动；1：靠左；2：居中；3：靠右）
	 */
	int align() default 0;

	/**
	 * 导出字段字段排序（升序）
	 */
	int sort() default 0;

	/**
	 * 反射类型
	 */
	Class<?> fieldType() default Class.class;

}
