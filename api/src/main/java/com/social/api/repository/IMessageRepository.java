package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.api.entity.Message;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Integer>{
    
}
