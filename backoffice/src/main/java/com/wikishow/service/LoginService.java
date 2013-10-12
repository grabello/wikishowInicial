package com.wikishow.service;

import com.wikishow.entity.Person;
import com.wikishow.repository.PersonRepository;
import com.wikishow.vo.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 10/6/13
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class LoginService {

    @Autowired
    PersonRepository personRepository;

    public PersonVO saveLogin(String email, String refreshToken, String accessToken, String type) {
        Person person = person = personRepository.findByEmail(email);
        if (person == null) {
            person = new Person();
            person.setId(personRepository.getNextId());
            person.setEmail(email);
            person.setType(type);
        }
        person.setAccessToken(accessToken);
        person.setRefreshToken(refreshToken);
        personRepository.addPersonData(person);
        person = personRepository.findByEmail(email);

        if (person != null) {
            PersonVO personVO = new PersonVO(person.getId(), person.getEmail(), person.getRefreshToken(), person.getAccessToken(), 'G');
            return personVO;
        }
        return null;
    }

    public PersonVO getUser(Integer id) {
        Person person = personRepository.findById(id);

        if (person != null) {
            PersonVO personVO = new PersonVO(person.getId(), person.getEmail(), person.getRefreshToken(), person.getAccessToken(), 'G');
            return personVO;
        }
        return null;
    }
}
