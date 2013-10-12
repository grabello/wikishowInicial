package com.wikishow.repository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/9/13
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class PersonRepository {

    private static final String ACCESS_KEY = "AKIAJTBPDH4NJDBK7YVQ";
    private static final String SECRET_KEY = "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";
    private static final Integer MIN = 1;
    private static final Integer MAX = 999999;
    DynamoDBMapper mapper = null;

    public void addPersonData(Person personEntity) {
        getMapper();
        mapper.save(personEntity);
    }

    public List<Person> listAllPersons() {
//        return mongoTemplate.findAll(Person.class, COLLECTION_NAME);
        return null;
    }

    public Person findById(Integer id) {
        getMapper();
        return mapper.load(Person.class, id);
    }

    //
    public Person findByEmail(String email) {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Email",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue().withS(email)));
        List<Person> scanResult = mapper.scan(Person.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }

        return scanResult.get(0);
    }

    public void deletePerson(Person personEntity) {
        getMapper();
        mapper.delete(personEntity);
    }

    public Integer getNextId() {
        Integer id = null;
        do {
            id = MIN + (int) (Math.random() * ((MAX - MIN) + 1));
        } while (findById(id) != null);

        return id;
    }

    public void getMapper() {
        if (mapper == null) {
            AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
            mapper = new DynamoDBMapper(client);
        }
    }

}
