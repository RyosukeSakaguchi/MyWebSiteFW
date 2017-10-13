package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Position;

public interface PositionRepository  extends CrudRepository< Position, Long> {

}
