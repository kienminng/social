package com.social.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.social.api.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User,Integer>{
    @Query(value = "SELECT * FROM user u WHERE u.user_name = :str OR u.email = :email", nativeQuery = true)
    User findByUsernameOrEmail(@Param("str") String username,@Param("email") String email);
    
    @Query(value = "SELECT * FROM user u WHERE u.address LIKE %:address% AND u.user_name LIKE %:username% AND u.phone_number LIKE %:phone% AND u.email LIKE %:email%", nativeQuery = true)
    Slice<User> filter(@Param("username")String username,@Param("phone") String phone,@Param("address") String address,@Param("email")String email, Pageable pageable);
        
    @Query(value = "SELECT * FROM user u WHERE u.user_name = :username", nativeQuery = true)
    Optional<User> loadByUsername(@Param("username") String username);
        
    @Query(value = "SELECT * FROM user u WHERE u.user_name = :username AND u.hash_password = :password", nativeQuery = true)
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
