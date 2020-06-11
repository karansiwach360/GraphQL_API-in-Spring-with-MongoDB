package com.cricketrecords;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CricketerMutation implements GraphQLMutationResolver {

    private CricketerRepository cricketerRepository;

    @Autowired
    public CricketerMutation(CricketerRepository cricketerRepository) {
        this.cricketerRepository = cricketerRepository;
    }

    public Cricketer createCricketer(final String name, final Integer age) {
        Cricketer cricketer = new Cricketer(name, age, new Integer(0), new Integer(0), new Integer(0));
        return cricketerRepository.save(cricketer);
    }

    public Cricketer updateCricketer (final String id, final Integer runs, final Integer wickets)
            throws Exception {
        Optional<Cricketer> cricketer = cricketerRepository.findById(id);
        if(cricketer.isPresent()) {
            Cricketer withCurrentStats = cricketer.get();
            //cricketerRepository.deleteById(id);
            withCurrentStats.setMatches(withCurrentStats.getMatches()+1);
            withCurrentStats.setRuns(withCurrentStats.getRuns()+runs);
            withCurrentStats.setWickets(withCurrentStats.getWickets()+wickets);
            //Cricketer withUpdatedStats = new Cricketer(withCurrentStats.getName(), withCurrentStats.getAge(), withCurrentStats.getMatches()+1,
             //       withCurrentStats.getRuns()+runs, withCurrentStats.getWickets()+wickets);
            return cricketerRepository.save(withCurrentStats);
        }
        throw new Exception("Mitch Marsh and one with ID " + id + " not found");
    }
}
