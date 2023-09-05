package com.smile.petpat.user.repository;

import com.smile.petpat.user.domain.User;
import com.smile.petpat.user.repository.querydsl.ProfileRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User,Long>, ProfileRepositoryQuerydsl {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByNickname(String nickName);

}
