package com.cricketrecords;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CricketerQuery implements GraphQLQueryResolver {

    private CricketerRepository cricketerRepository;

    @Autowired
    public CricketerQuery(CricketerRepository cricketerRepository) {
        this.cricketerRepository = cricketerRepository;
    }

    public List<Cricketer> getAllCricketers() {
        return cricketerRepository.findAll();
    }

    public List<Cricketer> getCricketerDetailsViaName(final String cricketerName) {
        List<Cricketer> cricketers = cricketerRepository.findByName(cricketerName);
        return cricketers;
    }

    public Cricketer getMostExperienced() {
        Cricketer cricketer = cricketerRepository.findTopByMatchesAfterOrderByMatchesDesc(0);
        //List<Cricketer> result = cricketers.collect(Collectors.toList());
        //System.out.println(cricketer.getName());
        return cricketer;
    }

    public Long getTotalCricketersCount() {
        return cricketerRepository.count();
    }
}

