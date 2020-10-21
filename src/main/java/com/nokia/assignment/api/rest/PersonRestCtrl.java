package com.nokia.assignment.api.rest;

import com.nokia.assignment.model.Person;
import com.nokia.assignment.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/person")
public class PersonRestCtrl {

    @Autowired
    PersonService personService;

    @PostMapping("/add")
    public Boolean add(@RequestBody Person person){
        return personService.addPerson(person);
    }

    @DeleteMapping("/delete")
    public Integer delete(@RequestBody String name){
        Integer deleted = personService.deletePersons(name);
        return deleted;
    }

    @GetMapping("/search")
    public Person[] search(@RequestParam String name){
        Person[] result = personService.searchPersons(name);
        return result;
    }

}
