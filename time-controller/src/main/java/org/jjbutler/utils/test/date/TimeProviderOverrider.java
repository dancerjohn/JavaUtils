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
package org.jjbutler.utils.test.date;

import static com.google.common.base.Objects.*;

import java.util.Date;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.jjbutler.utils.date.TimeProvider;

/**
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
class TimeProviderOverrider extends TimeProvider {

	private static TimeProviderOverrider instance;

	public synchronized static TimeProviderOverrider getInstance() {
		if (null == instance) {
			TimeProviderOverrider.instance = new TimeProviderOverrider();
			TimeProvider.setInstance(instance);
		}

		return instance;
	}

	@Nullable
	private Date currentTime = null;

	/**
	 * Constructor
	 */
	private TimeProviderOverrider() {
	}

	@Override
	protected Date getOverridableDate() {
		return firstNonNull(currentTime, super.getOverridableDate());
	}

	public void setCurrentTime(Date date) {
		currentTime = date;
	}

	public void setCurrentTimeToNow() {
		currentTime = super.getOverridableDate();
	}

	public void reset() {
		currentTime = null;
	}
}
