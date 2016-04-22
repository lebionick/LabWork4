package com.example.macbook.labwork4;

import java.io.Serializable;

/**
 * Created by macbook on 22.04.16.
 */
public class Person implements Serializable, Comparable<Person> {

    private long id = -1;
    private Integer age;
    private String name;
    private String surname;
    private Integer grade;

    public Person(String Name, String Surname, Integer Age, Integer Grade){
        name=Name;
        surname=Surname;
        age=Age;
        grade=Grade;
    }

    public String getName(){
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getGrade() {
        return grade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Person p) {
        int result = (name + surname).compareTo(p.getName()+p.getSurname());
        if(result==0)
            return getAge().compareTo(p.getAge());
        return result;
    }
}
