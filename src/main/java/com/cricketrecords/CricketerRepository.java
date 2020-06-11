package com.cricketrecords;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.stream.Stream;

@EnableMongoRepositories
public interface CricketerRepository extends MongoRepository<Cricketer, String> {
    List<Cricketer> findByName(String name);
    Cricketer findTopByMatchesAfterOrderByMatchesDesc(Integer tmp);
}
