package com.ferreteria.persistence;

import com.ferreteria.entity.MovimientoStock;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
	
	@Query("SELECT m FROM MovimientoStock m WHERE m.fecha BETWEEN :desde AND :hasta")
	List<MovimientoStock> buscarEntreFechas(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);



}