package com.ferreteria.persistence;

import com.ferreteria.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsername(String username);

    void deleteByUsername(String username);

    void deleteByIdAndUsername(Long id, String username);
}
