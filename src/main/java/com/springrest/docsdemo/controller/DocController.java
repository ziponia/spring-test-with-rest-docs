package com.springrest.docsdemo.controller;

import com.springrest.docsdemo.doc.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class DocController {

    List<User> users = new ArrayList<>();

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().get();
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        user.setIdx(users.size() + 1L);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        users.add(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/user/{idx}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long idx,
            @RequestParam String updateName,
            @RequestParam Integer updateAge
    ) {
        User user = users.stream()
                .map(u -> {
                    if (u.getIdx().equals(idx)) {
                        if (updateName != null) {
                            u.setUsername(updateName);
                        }
                        if (updateAge != null) {
                            u.setAge(updateAge);
                        }

                        u.setUpdateDate(new Date());
                        return u;
                    }
                    return u;
                })
                .filter(u -> u.getIdx().equals(idx)).findFirst().get();

        System.out.println(user);

        return ResponseEntity.ok(user);
    }
}
