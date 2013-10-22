package com.wikishow.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/3/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class RoleRepository extends DefaultRepository {

    public void addRole(Role role) {
        getMapper();
        mapper.save(role);
    }

    public List<Role> listRoleBySeriesId(String seriesId) {
        getMapper();
        System.out.println("Starting RoleRepository.listRoleBySeriesId seriesID=" + seriesId);
        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(seriesId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("TVShowID", rangeKeyCondition);

        List<Role> scanResult = mapper.scan(Role.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }
        System.out.println("Finished RoleRepository.listRoleBySeriesId size=" + scanResult.size());
        return scanResult;
    }

    public List<Role> findByIds(Set<String> seasonIds) {
        getMapper();
        List<Role> scanResult;
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Set<AttributeValue> attributeValueSet = new HashSet<AttributeValue>();
        for (String seasonId : seasonIds) {
            attributeValueSet.add(new AttributeValue().withS(seasonId));
        }
        scanExpression.addFilterCondition("Role", new Condition()
                .withComparisonOperator(ComparisonOperator.IN)
                .withAttributeValueList(attributeValueSet));

        scanResult = mapper.scan(Role.class, scanExpression);
        return scanResult;
    }

    public Role findByRoleAndSeriesId(String role, String seriesId) {
        getMapper();
        Condition roleRangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(role));

        Condition seriesIDRangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(seriesId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("TVShowID", seriesIDRangeKeyCondition);
        scanExpression.addFilterCondition("Role", roleRangeKeyCondition);

        List<Role> scanResult = mapper.scan(Role.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }
        return scanResult.get(0);
    }

    public Role findByActorAndSeriesId(String actor, String seriesId) {
        getMapper();
        Condition roleRangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(actor));

        Condition seriesIDRangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(seriesId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("TVShowID", seriesIDRangeKeyCondition);
        scanExpression.addFilterCondition("CastName", roleRangeKeyCondition);

        List<Role> scanResult = mapper.scan(Role.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }
        return scanResult.get(0);
    }

    public Role findById(String id) {
        getMapper();
        Condition idCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(id));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Id", idCondition);
        List<Role> scanResult = mapper.scan(Role.class, scanExpression);

        if (scanResult == null || scanResult.isEmpty()) {
            return null;
        }

        return scanResult.get(0);
    }

    public Role findByRoleAndName(String role, String name) {
        getMapper();
        if (role == null || role.isEmpty()) {
            role = "AddicTV";
        }

        if (name == null || name.isEmpty()) {
            name = "AddicTV";
        }

        Role roleEntity = new Role();
        roleEntity.setRole(role);
        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(name));
        DynamoDBQueryExpression<Role> queryExpression = new DynamoDBQueryExpression<Role>()
                .withHashKeyValues(roleEntity)
                .withRangeKeyCondition("CastName", rangeKeyCondition);

        List<Role> roleList = mapper.query(Role.class, queryExpression);

        if (roleList == null || roleList.isEmpty()) {
            return null;
        }

        return roleList.get(0);
    }

    public List<Role> findByRole(String role) {
        Role roleEntity = new Role();
        roleEntity.setRole(role);
        DynamoDBQueryExpression<Role> queryExpression = new DynamoDBQueryExpression<Role>().
                withHashKeyValues(roleEntity);
        return mapper.query(Role.class, queryExpression);
    }

}
