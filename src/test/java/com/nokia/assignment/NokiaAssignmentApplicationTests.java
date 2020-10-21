package com.nokia.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nokia.assignment.model.Person;
import com.nokia.assignment.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class NokiaAssignmentApplicationTests {
    private static final String ADD_PERSON_PATH = "/person/add";
    private static final String DELETE_PERSON_PATH = "/person/delete";
    private static final String SEARCH_PERSON_PATH = "/person/search";

    ObjectMapper mapper=new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonService personService;


    @Test
    void testRestLayerOperations() throws Exception {
        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Person("123","morad zeidan"))))
                .andExpect(status().isOk()).andExpect(content().string("true"));

        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Person("124","morad zeidan"))))
                .andExpect(status().isOk()).andExpect(content().string("true"));

        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Person("124","david david"))))
                .andExpect(status().isOk()).andExpect(content().string("false"));

        MvcResult result = this.mockMvc.perform(get(SEARCH_PERSON_PATH)
                .param("name","morad zeidan"))
                .andExpect(status().isOk()).andReturn();
        Person[] persons = mapper.readValue(result.getResponse().getContentAsString(), Person[].class);
        assertTrue(persons.length==2);

        this.mockMvc.perform(delete(DELETE_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content("morad zeidan"))
                .andExpect(status().isOk()).andExpect(content().string("2"));
    }

    @Test
    void test_addExistingPersonId() throws Exception {
        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Person("100","daniel cohen"))))
                .andExpect(status().isOk()).andExpect(content().string("true"));

        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Person("100","david david"))))
                .andExpect(status().isOk()).andExpect(content().string("false"));
    }

    @Test
    void test_addInvalidPerson() throws Exception {
        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_delteNonExistingPersonName() throws Exception {
        this.mockMvc.perform(delete(DELETE_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content("dana"))
                .andExpect(status().isOk()).andExpect(content().string("0"));
    }

    @Test
    void test_multithreadsAdding() throws Exception {
        Thread t1 = new Thread(()-> {
            for(int i=1 ; i<=100 ; i++) {
                personService.addPerson(new Person(String.valueOf(i), "person-name"+i));
            }
        });

        Thread t2 = new Thread(()-> {
            for(int i=1 ; i<=100 ; i++) {
                personService.addPerson(new Person(String.valueOf(100+i), "person-name"+i));
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        for(int i=1 ; i<=100 ; i++) {
            assertEquals(personService.deletePersons("person-name"+i) , 2);
        }
    }
}
