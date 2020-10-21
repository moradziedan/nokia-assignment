package com.nokia.assignment.repository;

import com.nokia.assignment.model.Person;

public interface PersonStorage {

    /**
     * adds a new person to the storage
     * @param person
     * @return true if the person was added successfully and false otherwise
     */
    Boolean add(Person person);

    /**
     * delete persons by name
     * @param name
     * @return number of deleted persons
     */
    Integer delete(String name);

    /**
     * search for persons by names
     * @param name
     * @return array of persons with the specified name
     */
    Person[] search(String name);
}
