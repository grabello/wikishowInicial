package com.wikishow.service;

import com.wikishow.vo.PersonVO;
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

//    @Autowired
//    PersonRepository personRepository;

    public PersonVO saveLogin(String email, String refreshToken, String accessToken, char type) {
//        Person person = new Person();
//        person.setEmail(email);
//        person.setAccessToken(accessToken);
//        person.setRefreshToken(refreshToken);
//        person.setType(type);

//        personRepository.addPersonData(person);
//        person = personRepository.findByEmail(email);

//        PersonVO personVO = new PersonVO(person.getId(), person.getEmail(), person.getRefreshToken(), person.getAccessToken(), 'G');
        return null;
    }
}
