package com.easy.eats.segmento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.segmento.model.Segmento;

@Repository
public interface SegmentoRepository extends JpaRepository<Segmento, Integer> {

}
