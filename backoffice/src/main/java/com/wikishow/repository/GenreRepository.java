package com.wikishow.repository;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/19/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.wikishow.entity.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenreRepository extends DefaultRepository{

    public void addGenreData(Genre genreEntity) {
        if (findByGenre(genreEntity.getGenre()) == null) {
            mapper.save(genreEntity);
        }
    }

    public List<Genre> listAllGenres() {
        getMapper();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition("Genre",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.NOT_NULL));
        List<Genre> scanResult = mapper.scan(Genre.class, scanExpression);

        if (scanResult == null || scanResult.size() == 0) {
            return null;
        }

        return scanResult;
    }

    public Genre findByGenre(String id) {
        getMapper();
        return mapper.load(Genre.class, id);
    }
}
