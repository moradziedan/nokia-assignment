package com.nokia.assignment.service;

import com.nokia.assignment.model.Person;
import com.nokia.assignment.repository.PersonStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonStorage personStorage;

    @Override
    public boolean addPerson(Person person) {
        return personStorage.add(person);
    }

    @Override
    public Integer deletePersons(String name) {
        return personStorage.delete(name);
    }

    @Override
    public Person[] searchPersons(String name) {
        return personStorage.search(name);
    }
}
