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

import java.util.Date;

import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
public class TimeController implements TestRule {

	private final TimeProviderOverrider timeProvider = TimeProviderOverrider
			.getInstance();

	public Statement apply(final Statement statement, Description arg1) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				try {
					statement.evaluate();
				} finally {
					timeProvider.reset();
				}
			}
		};
	}

	public void setCurrentTime(Date date) {
		timeProvider.setCurrentTime(date);
	}

	public void setCurrentTimeToNow() {
		timeProvider.setCurrentTimeToNow();
	}

	public void reset() {
		timeProvider.reset();
	}

}
