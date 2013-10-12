package com.wikishow.repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/12/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultRepository {

    private static final String ACCESS_KEY = "AKIAJTBPDH4NJDBK7YVQ";
    private static final String SECRET_KEY = "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";
    DynamoDBMapper mapper = null;

    public void getMapper() {
        if (mapper == null) {
            AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
            mapper = new DynamoDBMapper(client);
        }
    }

}
