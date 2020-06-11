package com.cricketrecords;


//import com.cricketrecords.annotations.QueryTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.graphql.spring.boot.test.GraphQLTest;
import org.junit.Ignore;
//import com.cricketrecords.annotations.MutationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@GraphQLTest
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({Cricketer.class})
//@SpringBootTest
public class ApplicationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    //@MockBean
    //private CricketerMutation cricketerMutation;

    @MockBean
    private CricketerRepository cricketerRepository;

    //@MockBean
    //private Cricketer important;

    private Cricketer cricketer;
    //private GraphQLResponse response;


    //@MockBean
    //private CricketerQuery cricketerQuery;

    @BeforeEach
    public void init() {
        cricketer = new Cricketer("Steve", 31, 0, 0, 0);
        cricketer.setId("ABisTheBest");
    }

    @Test
    //@QueryTest
    public void getAllCricketers() throws IOException {
        when(cricketerRepository.findAll()).thenReturn(Collections.singletonList(cricketer));
        //when(cricketerQuery.getAllCricketers()).thenReturn(Collections.singletonList(cricketer));


        GraphQLResponse response = graphQLTestTemplate.postForResource(
                "graphql/getAllCricketers.graphql");

        System.out.println(response.context().jsonString());
        verify(cricketerRepository).findAll();
        //verify(cricketerQuery).getAllCricketers();
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);

    }

    @Test
    //@QueryTest
    public void getCricketerDetailsViaName() throws IOException {
        when(cricketerRepository.findByName(cricketer.getName())).thenReturn(Collections.singletonList(cricketer));
        //when(cricketerQuery.getCricketerDetailsViaName(cricketer.getName())).thenReturn(Collections.singletonList(cricketer));
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("cricketerName", cricketer.getName());

        GraphQLResponse response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);

        System.out.println(response.context().jsonString());
        //verify(cricketerQuery).getCricketerDetailsViaName(cricketer.getName());
        verify(cricketerRepository).findByName(cricketer.getName());
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);


        when(cricketerRepository.findByName("Virat")).thenReturn(Collections.emptyList());
        //when(cricketerQuery.getCricketerDetailsViaName("Virat")).thenReturn(Collections.emptyList());
        variables.put("cricketerName", "Virat");
        response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);
        System.out.println(response.context().jsonString());
        verify(cricketerRepository).findByName("Virat");
        //verify(cricketerQuery).getCricketerDetailsViaName("Virat");
        assertTrue(Integer.valueOf(response.get("$.data.cricketers.length()")) == 0);
        //tmp = response.context().read("$.data.cricketers[0]",Cricketer.class);


    }
}