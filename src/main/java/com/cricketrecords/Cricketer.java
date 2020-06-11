package com.cricketrecords;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "cricketers")
public class Cricketer {
    @Id
    private String id;
    private String name;
    private Integer age;
    @Indexed
    private Integer matches;
    private Integer runs;
    private Integer wickets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Integer getRuns() {
        return runs;
    }

    public void setRuns(Integer runs) {
        this.runs = runs;
    }

    public Integer getWickets() {
        return wickets;
    }

    public void setWickets(Integer wickets) {
        this.wickets = wickets;
    }

    public Cricketer() {

    }

    public Cricketer(String name, Integer age, Integer matches, Integer runs, Integer wickets) {
        setName(name);
        setAge(age);
        setMatches(matches);
        setRuns(runs);
        setWickets(wickets);
    }

    @Override
    public boolean equals(Object tmp) {
        return ((Cricketer) tmp).getId().equals(this.getId())
                && ((Cricketer) tmp).getAge().equals(this.getAge())
                && ((Cricketer) tmp).getName().equals(this.getName())
                && ((Cricketer) tmp).getMatches().equals(this.getMatches())
                && ((Cricketer) tmp).getRuns().equals(this.getRuns())
                && ((Cricketer) tmp).getWickets().equals(this.getWickets());
    }

    @Override
    public String toString() {
        return "Cricketer [id=" + id + ", name=" + name + ", age=" + age + ", matches=" + matches + ", age=" + runs
                + ", wickets=" + wickets + "]";
    }

}
