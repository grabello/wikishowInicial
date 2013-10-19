package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
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
public class PersonRepository extends DefaultRepository {

    private static final Integer MIN = 1;
    private static final Integer MAX = 999999;

    public void addPersonData(Person personEntity) {
        getMapper();
        mapper.save(personEntity);
    }

    public Person findById(Integer id) {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Id",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue().withN(id.toString())));
        List<Person> scanResult = mapper.scan(Person.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }
        return scanResult.get(0);
    }

    public Person findByEmail(String email) {
        getMapper();
        if (email == null) {
            return null;
        }
        return mapper.load(Person.class, email);

    }

    public Person findByName(String name) {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Name",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(new AttributeValue().withS(name)));
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

}
