package org.jjbutler.utils.enums;

/**
 * Copyright 2012 John Butler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import static com.google.common.base.Preconditions.*;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;

/**
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
public class JbEnums {

	private static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
	private static final Lock readLock = cacheLock.readLock();
	private static final Lock writeLock = cacheLock.writeLock();
	private static final Map<Class<?>, Map<?, ?>> cache =
			Maps.newHashMap();

	@Nonnull
	public static <U, T extends Enum<T> & Supplier<U>> T valueOf(
			Class<T> enumClass,
			@Nullable U value,
			boolean useCache) {

		T result = valueOf(enumClass, value, useCache, null);

		if (result == null)
			throw new IllegalArgumentException("No enum cost "
					+ enumClass.getName() + " for code <" + value + ">");

		return result;
	}

	@Nonnull
	public static <U, T extends Enum<T> & Supplier<U>> T valueOf(
			Class<T> enumClass,
			@Nullable U value,
			boolean useCache,
			@Nullable T defaultValue) {
		checkNotNull(enumClass, "enumClass");

		if (useCache)
			return getValueFromCache(enumClass, value, defaultValue);
		else
			return getValueFromClass(enumClass, value, defaultValue);
	}

	@Nullable
	private static <U, T extends Enum<T> & Supplier<U>> T getValueFromClass(
			Class<T> enumClass,
			@Nullable U value,
			@Nullable T defaultValue) {

		for (T e : enumClass.getEnumConstants()) {
			if (Objects.equal(e.get(), value)) {
				return e;
			}
		}

		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private static <U, T extends Enum<T> & Supplier<U>> T getValueFromCache(
			Class<T> enumClass,
			@Nullable U value,
			@Nullable T defaultValue) {

		Map<U, T> map = null;
		readLock.lock();
		try {
			map = (Map<U, T>) cache.get(enumClass);
		} finally {
			readLock.unlock();
		}

		if (map == null) {
			writeLock.lock();
			try {
				map = (Map<U, T>) cache.get(enumClass);

				if (map == null) {
					map = Maps.newHashMap();
					for (T e : enumClass.getEnumConstants()) {
						map.put(e.get(), e);
					}
					cache.put(enumClass, map);
				}
			} finally {
				writeLock.unlock();
			}
		}

		T result = map.get(value);
		return (result != null) ? result : defaultValue;
	}
}
