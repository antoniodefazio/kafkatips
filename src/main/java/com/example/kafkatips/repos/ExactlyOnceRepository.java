package com.example.kafkatips.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kafkatips.entities.ExactlyOnce;

public interface ExactlyOnceRepository extends JpaRepository<ExactlyOnce, String> {

}
