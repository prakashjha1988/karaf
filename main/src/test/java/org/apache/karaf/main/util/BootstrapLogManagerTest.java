/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.karaf.main.util;

import java.io.File;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import junit.framework.Assert;

import org.junit.Test;

public class BootstrapLogManagerTest {

	@Test
	public void testGetLogManagerNoProperties() {
		System.setProperty("karaf.data", "target");
		BootstrapLogManager.setProperties(null);
		try {
			BootstrapLogManager.getDefaultHandler();
		} catch (IllegalStateException e) {
			Assert.assertEquals("Properties must be set before calling getDefaultHandler", e.getMessage());
		}
	}
	
	@Test
	public void testGetLogManager() {
		new File("target/log/karaf.log").delete();
		System.setProperty("karaf.data", "target");
		Properties configProps = new Properties();
		BootstrapLogManager.setProperties(configProps);
		Handler handler = BootstrapLogManager.getDefaultHandler();
		Assert.assertNotNull(handler);
        try {
            // introduce a delay just to give the time to the handler to actually create the log file
            Thread.sleep(200);
        } catch (Exception e) {
            // ignore
        }
		assertExists("target/log/karaf.log");
	}
	
	@Test
	public void testGetLogManagerFromPaxLoggingConfig() {
		new File("target/test.log").delete();
		System.setProperty("karaf.base", "src/test/resources/test-karaf-home");
		Properties configProps = new Properties();
		BootstrapLogManager.setProperties(configProps);
		Handler handler = BootstrapLogManager.getDefaultHandler();
		Assert.assertNotNull(handler);
		assertExists("target/test.log");
	}
	
	private void assertExists(String path) {
		Assert.assertTrue("File should exist at " + path, new File(path).exists());
	}
}
