package com.cs.bbyz.http

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author chenshuai12619
 * @date 2018-02-22
 */
class ParameterizedTypeImpl(private var clazz: Class<*>, private var args: Array<Type>) : ParameterizedType {
	override fun getRawType(): Type {
		return clazz
	}

	override fun getOwnerType(): Type? {
		return null
	}

	override fun getActualTypeArguments(): Array<Type> {
		return args
	}
}