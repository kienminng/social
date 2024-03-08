package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.api.entity.Post;

@Repository
public interface IPostRepository extends JpaRepository<Post,Integer>{
    
}
