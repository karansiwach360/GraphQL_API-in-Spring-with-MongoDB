package com.cricketrecords;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    public Cricketer getMostExperienced() throws Exception{
        Optional<Cricketer> cricketer = cricketerRepository.findTopByMatchesAfterOrderByMatchesDesc(-1);
        if(cricketer.isPresent()) {
            Cricketer result = cricketer.get();
            System.out.println(result.getName());
            return result;
        }
        throw new Exception("Empty List");
    }

    public Long getTotalCricketersCount() {
        return cricketerRepository.count();
    }
}

