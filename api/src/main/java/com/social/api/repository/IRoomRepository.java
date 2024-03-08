package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.api.entity.Room;

@Repository
public interface IRoomRepository extends JpaRepository<Room,Integer>{
    
}
