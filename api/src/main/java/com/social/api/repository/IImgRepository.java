package com.social.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.social.api.entity.Img;

@Repository
public interface IImgRepository extends JpaRepository<Img, Integer>{
    
}
