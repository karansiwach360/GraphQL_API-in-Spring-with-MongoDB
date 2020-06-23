package com.cricketrecords;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.graphql.spring.boot.test.GraphQLTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@GraphQLTest
public class ApplicationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private CricketerRepository cricketerRepository;

    private Cricketer cricketer;

    @BeforeEach
    public void init() {
        cricketer = new Cricketer("Steve", 31, 0, 0, 0);
        cricketer.setId("ABisTheBest");
    }

    @Test
    public void createCricketer() throws IOException {
        when(cricketerRepository.save(any(Cricketer.class))).thenReturn(cricketer);

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", cricketer.getName());
        variables.put("age", cricketer.getAge());

        GraphQLResponse response = graphQLTestTemplate.perform(
                "graphql/createCricketer.graphql", variables);

        System.out.println(response.context().jsonString());


        verify(cricketerRepository).save(any(Cricketer.class));
        Cricketer tmp = response.context().read("$.data.cricketer", Cricketer.class);

        System.out.println(tmp.getId());

        assertEquals(cricketer, tmp);

    }

    @Test
    public void getAllCricketers() throws IOException {
        when(cricketerRepository.findAll()).thenReturn(Collections.singletonList(cricketer));


        GraphQLResponse response = graphQLTestTemplate.postForResource(
                "graphql/getAllCricketers.graphql");

        System.out.println(response.context().jsonString());
        verify(cricketerRepository).findAll();
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);

    }

    @Test
    public void getCricketerDetailsViaName() throws IOException {
        when(cricketerRepository.findByName(cricketer.getName())).thenReturn(Collections.singletonList(cricketer));
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("cricketerName", cricketer.getName());

        GraphQLResponse response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);

        System.out.println(response.context().jsonString());
        verify(cricketerRepository).findByName(cricketer.getName());
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);


        when(cricketerRepository.findByName("Virat")).thenReturn(Collections.emptyList());
        variables.put("cricketerName", "Virat");
        response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);
        System.out.println(response.context().jsonString());
        verify(cricketerRepository).findByName("Virat");
        assertTrue(Integer.valueOf(response.get("$.data.cricketers.length()")) == 0);

    }
}