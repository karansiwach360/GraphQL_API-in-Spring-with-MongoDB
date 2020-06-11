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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


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
//@SpringBootTest
public class GraphQLSchemaTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private CricketerMutation cricketerMutation;

    @MockBean
    private CricketerRepository cricketerRepository;

    private Cricketer cricketer;
    //private GraphQLResponse response;

    @MockBean
    private CricketerQuery cricketerQuery;

    @BeforeEach
    public void init() {
        cricketer = new Cricketer("Steve", 31, 0, 0, 0);
        cricketer.setId("ABisTheBest");
    }

    @Test
    //@MutationTest
    public void createCricketer() throws IOException {
        //when(cricketerRepository.save(cricketer)).thenReturn(cricketer);
        when(cricketerMutation.createCricketer(cricketer.getName(), cricketer.getAge())).thenReturn(cricketer);
        //Cricketer tmp = cricketerRepository.save(cricketer);

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", cricketer.getName());
        variables.put("age", cricketer.getAge());
        //when(cricketerRepository.save(cricketer)).thenReturn(cricketer);
        GraphQLResponse response = graphQLTestTemplate.perform("graphql/createCricketer.graphql",
                variables);
        assertNotNull(response);
        //assertNotNull(response.context().read("$.data.createCricketer"));
        System.out.println(response.context().jsonString());
        Assertions.assertTrue(response.isOk());
        Cricketer tmp = response.context().read("$.data.cricketer", Cricketer.class);
        //System.out.println(tmp.getName());
        //assertThat(tmp, samePropertyValuesAs(cricketer));
        verify(cricketerMutation).createCricketer(cricketer.getName(), cricketer.getAge());
        assertEquals(cricketer, tmp);
        //assertEquals(tmp,cricketer);
    }

    @Test
    //@MutationTest
    public void updateCricketer() throws IOException, Exception {

        Cricketer tmp = new Cricketer(cricketer.getName(), cricketer.getAge(), cricketer.getMatches() + 1,
                cricketer.getRuns() + 3, cricketer.getWickets() + 3);
        tmp.setId(cricketer.getId());
        System.out.println(tmp.getWickets());
        when(cricketerMutation.updateCricketer(cricketer.getId(), new Integer(3), new Integer(3)))
                .thenReturn(tmp);

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("id", cricketer.getId());
        variables.put("runs", cricketer.getRuns() + 3);
        variables.put("wickets", cricketer.getWickets() + 3);

        System.out.println(variables);

        GraphQLResponse response = graphQLTestTemplate.perform("graphql/updateCricketer.graphql",
                variables);

        verify(cricketerMutation).updateCricketer(cricketer.getId(), new Integer(3), new Integer(3));
        System.out.println(response.context().jsonString());

        //System.out.println(response.context().read("$.data.cricketer",Cricketer.class).getRuns());
        assertEquals(tmp, response.context().read("$.data.cricketer", Cricketer.class));


        ///tmp.setId("heywhatsup");
        variables.put("id", "heywhatsup");
        variables.put("runs", cricketer.getRuns() + 3);
        variables.put("wickets", cricketer.getWickets() + 3);
        System.out.println(variables);
        when(cricketerMutation.updateCricketer("heywhatsup", new Integer(3), new Integer(3)))
                .thenThrow(new Exception());
        //response = graphQLTestTemplate.perform("graphql/updateCricketer.graphql", variables);
        //verify(cricketerMutation).updateCricketer("heywhatsup",new Integer(3),new Integer(3));

    }

    @Test
    //@QueryTest
    public void getAllCricketers() throws IOException {
        //when(cricketerRepository.findAll()).thenReturn(Collections.singletonList(cricketer));
        when(cricketerQuery.getAllCricketers()).thenReturn(Collections.singletonList(cricketer));


        GraphQLResponse response = graphQLTestTemplate.postForResource(
                "graphql/getAllCricketers.graphql");

        System.out.println(response.context().jsonString());
        //verify(cricketerRepository).findAll();
        verify(cricketerQuery).getAllCricketers();
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);

    }

    @Test
    //@QueryTest
    public void getCricketerDetailsViaName() throws IOException {
        //when(cricketerRepository.findByName(cricketer.getName())).thenReturn(Collections.singletonList(cricketer));
        when(cricketerQuery.getCricketerDetailsViaName(cricketer.getName())).thenReturn(Collections.singletonList(cricketer));
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("cricketerName", cricketer.getName());

        GraphQLResponse response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);

        System.out.println(response.context().jsonString());
        verify(cricketerQuery).getCricketerDetailsViaName(cricketer.getName());
        //verify(cricketerRepository).findByName(cricketer.getName());
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);


        //when(cricketerRepository.findByName("Virat")).thenReturn(Collections.emptyList());
        when(cricketerQuery.getCricketerDetailsViaName("Virat")).thenReturn(Collections.emptyList());
        variables.put("cricketerName", "Virat");
        response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);
        System.out.println(response.context().jsonString());
        //verify(cricketerRepository).findByName("Virat");
        verify(cricketerQuery).getCricketerDetailsViaName("Virat");
        assertTrue(Integer.valueOf(response.get("$.data.cricketers.length()")) == 0);
        //tmp = response.context().read("$.data.cricketers[0]",Cricketer.class);


    }
}