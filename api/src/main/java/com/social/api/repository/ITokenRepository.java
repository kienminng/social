package com.social.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.social.api.entity.Token;

public interface ITokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);
    
    @Query(value = "select * form Token t where t.token = :token",nativeQuery = true)
    Optional<Token> findByToken(@Param("token") String token);
}
