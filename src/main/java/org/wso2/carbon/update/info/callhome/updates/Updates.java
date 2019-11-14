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
package org.wso2.carbon.update.info.callhome.updates;

import org.wso2.carbon.update.info.callhome.CallHome;
import org.wso2.carbon.update.info.callhome.utils.ExtractInfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * The Updates program implements an application where it retrieves the data from the ExtractInfo class and calls
 * to the WSO2 Update Servers to check whether there are new updates available.
 *
 * @version 1.0.0
 * @since 2019-11-01
 */
public class Updates {
    private static final Logger LOGGER = Logger.getLogger(CallHome.class.getName());

    /**
     * This method calls the WSO2 Update server and warns the user regarding the available updates.
     *
     * @throws IOException        If an IO exception occurs
     * @throws URISyntaxException If an URI Syntax Error occurs
     */
    public void getUpdateInfo() throws IOException, URISyntaxException {
        ExtractInfo inf = new ExtractInfo();
        int noOfUpdates = 0;
        String readLine;
        String url = "https://freetest.free.beeceptor.com/my/api/path" + "&username=" +
                URLEncoder.encode(inf.getUsername(), "UTF-8") +
                "&os=" +
                URLEncoder.encode(inf.getOS(), "UTF-8") + "&updatelevel=" +
                URLEncoder.encode(String.valueOf(inf.getUpdateLevel()), "UTF-8");
        URL urlForGetRequest = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urlForGetRequest.openConnection();
        connection.setRequestProperty("-H", "Content-Type: application/json");
        connection.setRequestProperty("-H", "Accept: application/json");
        connection.setRequestMethod("GET");
        LOGGER.info("connection: " + connection.toString());
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    Charset.forName("UTF-8")));
            StringBuilder response = new StringBuilder();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            LOGGER.info("--------------------------------------------------------------------------");
            LOGGER.info("JSON String Result " + response.toString());
            LOGGER.info("--------------------------------------------------------------------------");
            LOGGER.info("There are " + noOfUpdates + " updates available for the product " + "");
        }
    }

}
