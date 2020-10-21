package com.nokia.assignment.service;

import com.nokia.assignment.model.Person;

public interface PersonService {

    boolean addPerson(Person person);

    Integer deletePersons(String name);

    Person[] searchPersons(String name);
}
