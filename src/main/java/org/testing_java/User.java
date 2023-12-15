package org.testing_java;

public class User {
    int id;
    String name;

    public User(int id, String name){
        this.id = id;
        this.name = name;
    }

    public User(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
