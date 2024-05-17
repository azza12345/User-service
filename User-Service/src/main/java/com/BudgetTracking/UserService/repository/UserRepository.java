package com.BudgetTracking.UserService.repository;

import com.BudgetTracking.UserService.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends CrudRepository <User,Long>, JpaRepository< User, Long> {
   //User findByEmail(String email);


    Optional < User > findByEmail(String email);
}


