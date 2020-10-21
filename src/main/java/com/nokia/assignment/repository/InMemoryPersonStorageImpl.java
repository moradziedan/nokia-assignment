package com.nokia.assignment.repository;

import com.nokia.assignment.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class InMemoryPersonStorageImpl implements PersonStorage {

    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    @Value("${person.storage.max-limit}")
    private int maxItemsLimit;
    private Map<String, Person> personsStorage = new HashMap<>();


    @Override
    public Boolean add(Person person) {
        lock.writeLock().lock();
        try{
            if(personsStorage.size() < maxItemsLimit) {
                if (personsStorage.containsKey(person.getId()))
                    return Boolean.FALSE;
                personsStorage.put(person.getId(), person);
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Integer delete(String name) {
        lock.writeLock().lock();
        try{
            int count = 0;
            Person[] persons = getPersonsByName(name);
            if(persons != null){
                count = persons.length;
                for(Person p:persons) {
                    personsStorage.remove(p.getId());
                }
            }
            return count;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Person[] search(String name) {
        lock.readLock().lock();
        try {
            return getPersonsByName(name);
        }finally {
            lock.readLock().unlock();
        }
    }

    /**
     * returns array of persons with the specified name
     * @param name
     * @return array of persons with the specified name
     */
    private Person[] getPersonsByName(String name){
        return personsStorage.values().stream()
                    .filter(person -> name.equals(person.getName()))
                    .toArray(Person[]::new);
    }
}
