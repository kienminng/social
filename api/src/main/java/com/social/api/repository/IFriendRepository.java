package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.api.entity.Friends;

@Repository
public interface IFriendRepository extends JpaRepository<Friends, Integer>{
    
}
