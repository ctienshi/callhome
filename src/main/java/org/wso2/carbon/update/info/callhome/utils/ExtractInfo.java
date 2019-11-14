/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.update.info.callhome.utils;

import org.wso2.carbon.update.info.callhome.CallHome;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import static java.lang.System.getProperty;


/**
 * The ExtractInfo program implements an application where it retrieves the Operating System, email address
 * of the user and the latest update level of the product.
 *
 * @version 1.0.0
 * @since 2019-11-01
 */
public class ExtractInfo {
    private static final Logger LOGGER = Logger.getLogger(CallHome.class.getName());

    private String getProductHome() throws IOException, URISyntaxException {
        String productHome = new File(ExtractInfo.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        LOGGER.info("----------------------------------");
        LOGGER.info(productHome);
        LOGGER.info("----------------------------------");
        File file = new File("").getCanonicalFile();
        return file.getParent();
    }

    /**
     * This method is used to get the Operating System of the user.
     *
     * @return String This returns the Operating System.
     */
    public String getOS() {
        return getProperty("os.name");
    }

    /**
     * This method is used to get the email address of the user.
     *
     * @return String This returns the email address.
     */
    public String getUsername() throws IOException, URISyntaxException {

        Yaml yaml = new Yaml();
        String path = getProductHome() + "/updates/config.yaml";
        InputStream inputStream = new FileInputStream(path);
        Reader fileReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        Map<String, Object> yamlMaps = yaml.load(fileReader);
        return (String) yamlMaps.get("username");
    }

    /**
     * This method is used to retrieve the latest the update level of the users product.
     *
     * @return Long This returns the update time stamp
     */
    public Long getUpdateLevel() throws IOException, URISyntaxException {
        Long lastUpdateLevel = 0L;
        File updatesDirectory = new File(getProductHome() + "/updates/wum/");
        File[] listOfFiles = updatesDirectory.listFiles();
        List<Long> myList = new ArrayList<>();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isDirectory()) {
                    myList.add(Long.valueOf(listOfFile.getName()));
                }
            }
            lastUpdateLevel = Collections.max(myList);
        }
        return lastUpdateLevel;
    }
}
