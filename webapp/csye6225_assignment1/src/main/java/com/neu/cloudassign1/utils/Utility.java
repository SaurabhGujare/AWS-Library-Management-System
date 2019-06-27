package com.neu.cloudassign1.utils;

import com.neu.cloudassign1.service.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;

public class Utility {


    public static String generateS3BucketObjectKey(String objectUrl) {
        return objectUrl.substring(objectUrl.lastIndexOf("/C")+1);
    }



}
