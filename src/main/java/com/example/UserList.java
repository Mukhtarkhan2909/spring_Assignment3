package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserList {
    private List<Users> users;

    @Autowired
    public UserList() {
        users = new ArrayList<>();
    }

    public void addUser(Users user) {
        users.add(user);
    }
    public void removeUser(Users user) {
        users.remove(user);
    }
    public List<Users> getUsers() {
        return users;
    }
}
