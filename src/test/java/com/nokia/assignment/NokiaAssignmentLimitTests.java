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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class NokiaAssignmentLimitTests {

    private ObjectMapper mapper=new ObjectMapper();
    private static final String ADD_PERSON_PATH = "/person/add";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonService personService;

    @Value("${person.storage.max-limit}")
    int maxItemsLimit;


    @Test
    void test_exceedStorageLimit() throws Exception {
        for(int i=1 ; i<=maxItemsLimit ; i++){
            this.mockMvc.perform(post(ADD_PERSON_PATH)
                    .contentType(APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new Person("id"+i,"george-"+i))))
                    .andExpect(status().isOk()).andExpect(content().string("true"));
        }

        this.mockMvc.perform(post(ADD_PERSON_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Person("id-"+(maxItemsLimit+1),"extra-" + (maxItemsLimit+1) ))))
                .andExpect(status().isOk()).andExpect(content().string("false"));
    }
}
