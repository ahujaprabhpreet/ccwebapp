package com.neu.cloud.webapp.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findUsersByUsername (String username);

    public User findUsersByUsernameAndPassword(String username, String Password);

}
