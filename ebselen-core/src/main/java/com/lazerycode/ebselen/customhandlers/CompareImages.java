/*
 * Copyright (c) 2010-2011 Ardesco Solutions - http://www.ardescosolutions.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lazerycode.ebselen.customhandlers;

import com.lazerycode.ebselen.handlers.FileHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class CompareImages {

    private FileHandler localFileObject;
    private WebElement remoteImageObject;
    private FileHandler downloadedFileObject;
    private WebDriver driver;

    public CompareImages(File localFile, WebElement remoteImage, WebDriver driver) throws Exception {
        this.localFileObject = new FileHandler(localFile);
        this.remoteImageObject = remoteImage;
        this.driver = driver;
    }

    public CompareImages(String localFile, WebElement remoteImage, WebDriver driver) throws Exception {
        this.localFileObject = new FileHandler(localFile);
        this.remoteImageObject = remoteImage;
        this.driver = driver;
    }

    public void changeLocalFileTo(File localfile) throws Exception {
        this.localFileObject = new FileHandler(localfile);
    }

    public void changeLocalFileTo(String localfile) throws Exception {
        this.localFileObject = new FileHandler(localfile);
    }

    public void changeRemoteImageTo(WebElement remoteImage) {
        this.remoteImageObject = remoteImage;
    }

    public File getLocalImage() throws Exception {
        return this.localFileObject.getFile();
    }

    public WebElement getRemoteImageWebElement() {
        return this.remoteImageObject;
    }

    private byte[] generateMessageDigest(File fileToHash) throws Exception {
        MessageDigest MD5Hash = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[2048];
        InputStream theFileInputStream = new FileInputStream(fileToHash);
        int dataReadFromFile = 0;
        while (dataReadFromFile != -1) {
            dataReadFromFile = theFileInputStream.read(buffer);
            if (dataReadFromFile > 0) {
                MD5Hash.update(buffer, 0, dataReadFromFile);
            }
        }
        theFileInputStream.close();
        return MD5Hash.digest();
    }

    public Boolean compareImages() throws Exception {
        FileDownloader downloadRemoteImage = new FileDownloader(driver);
        downloadRemoteImage.imageDownloader(this.remoteImageObject);
        FileHandler downloadedImage = new FileHandler(downloadRemoteImage.getDownloadPath());
        if (generateMessageDigest(this.localFileObject.getFile()).equals(generateMessageDigest(downloadedImage.getFile()))) {
            return true;
        }
        return false;
    }

}

