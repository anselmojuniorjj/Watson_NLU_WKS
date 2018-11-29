package com.aj.nlu.repository;

import com.aj.nlu.model.Dado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Dados extends JpaRepository<Dado, Long> {
}
