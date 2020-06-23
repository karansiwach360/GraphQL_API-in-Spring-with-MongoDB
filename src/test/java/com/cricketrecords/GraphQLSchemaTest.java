package com.cricketrecords;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.graphql.spring.boot.test.GraphQLTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.io.IOException;

import static graphql.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@GraphQLTest
public class GraphQLSchemaTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private CricketerMutation cricketerMutation;

    private Cricketer cricketer;

    @MockBean
    private CricketerQuery cricketerQuery;

    @BeforeEach
    public void init() {
        cricketer = new Cricketer("Steve", 31, 0, 0, 0);
        cricketer.setId("ABisTheBest");
    }

    @Test
    public void createCricketer() throws IOException {
        when(cricketerMutation.createCricketer(cricketer.getName(), cricketer.getAge())).thenReturn(cricketer);

        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("name", cricketer.getName());
        variables.put("age", cricketer.getAge());
        GraphQLResponse response = graphQLTestTemplate.perform("graphql/createCricketer.graphql",
                variables);
        assertNotNull(response);
        System.out.println(response.context().jsonString());
        Assertions.assertTrue(response.isOk());
        Cricketer tmp = response.context().read("$.data.cricketer", Cricketer.class);
        verify(cricketerMutation).createCricketer(cricketer.getName(), cricketer.getAge());
        assertEquals(cricketer, tmp);
    }

    @Test
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

        assertEquals(tmp, response.context().read("$.data.cricketer", Cricketer.class));

        variables.put("id", "heywhatsup");
        variables.put("runs", cricketer.getRuns() + 3);
        variables.put("wickets", cricketer.getWickets() + 3);
        System.out.println(variables);
        when(cricketerMutation.updateCricketer("heywhatsup", new Integer(3), new Integer(3)))
                .thenThrow(new Exception());

        response = graphQLTestTemplate.perform("graphql/updateCricketer.graphql",
                variables);
        System.out.println(response.context().read("$.data.cricketer",Cricketer.class));
        assertNull(response.context().read("$.data.cricketer",Cricketer.class));
    }

    @Test
    public void getAllCricketers() throws IOException {
        when(cricketerQuery.getAllCricketers()).thenReturn(Collections.singletonList(cricketer));


        GraphQLResponse response = graphQLTestTemplate.postForResource(
                "graphql/getAllCricketers.graphql");

        System.out.println(response.context().jsonString());
        verify(cricketerQuery).getAllCricketers();
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);

    }

    @Test
    public void getCricketerDetailsViaName() throws IOException {
        when(cricketerQuery.getCricketerDetailsViaName(cricketer.getName())).thenReturn(Collections.singletonList(cricketer));
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("cricketerName", cricketer.getName());

        GraphQLResponse response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);

        System.out.println(response.context().jsonString());
        verify(cricketerQuery).getCricketerDetailsViaName(cricketer.getName());
        Cricketer tmp = response.context().read("$.data.cricketers[0]", Cricketer.class);
        assertEquals(cricketer, tmp);

        when(cricketerQuery.getCricketerDetailsViaName("Virat")).thenReturn(Collections.emptyList());
        variables.put("cricketerName", "Virat");
        response = graphQLTestTemplate.perform(
                "graphql/getCricketerDetailsViaName.graphql", variables);
        System.out.println(response.context().jsonString());
        verify(cricketerQuery).getCricketerDetailsViaName("Virat");
        assertTrue(Integer.valueOf(response.get("$.data.cricketers.length()")) == 0);


    }
}