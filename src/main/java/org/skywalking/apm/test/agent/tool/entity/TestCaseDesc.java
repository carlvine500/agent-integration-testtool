/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.skywalking.apm.test.agent.tool.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestCaseDesc {
    private static Logger logger = LogManager.getLogger(TestCaseDesc.class);
    private String testFramework;
    private String testVersion;
    private List<Component> cooperativeFrameworks;

    private TestCaseDesc() {
        cooperativeFrameworks = new ArrayList<>();
    }

    public static class Parser {
        public static TestCaseDesc parse(File caseDescFile) {
            TestCaseDesc testCase = new TestCaseDesc();
            Properties properties = new Properties();
            if (caseDescFile.exists()) {
                try {
                    properties.load(new FileInputStream(caseDescFile));
                } catch (IOException e) {
                    logger.error("Failed to parse case desc file[{}]", caseDescFile.getAbsoluteFile());
                    return testCase;
                }
            }

            testCase.testFramework = properties.getProperty("case.testFramework", null);
            testCase.testVersion = properties.getProperty("case.testVersion", null);
            testCase.cooperativeFrameworks = formatComponents(properties.getProperty("case.cooperative_frameworks", null));
            logger.info("load case desc: testFramework[{}], testVersion[{}], cooperativeFrameworks[{}]",
                testCase.testFramework,
                testCase.testVersion,
                testCase.cooperativeFrameworks);
            return testCase;
        }

        private static List<Component> formatComponents(String componentStr) {
            List<Component> components = new ArrayList<>();
            if (componentStr == null) {
                return components;
            }

            String[] componentSegments = componentStr.split(",");
            for (String segment : componentSegments) {
                components.add(new Component(segment));
            }
            return components;
        }

    }

    public String getTestFramework() {
        return testFramework;
    }

    public String getTestVersion() {
        return testVersion;
    }

    public List<Component> getCooperativeFrameworks() {
        return cooperativeFrameworks;
    }
}
