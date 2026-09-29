package com.mifel.test.repository.auth;

import org.springframework.stereotype.Repository;

import com.mifel.test.entity.auth.User;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
}
