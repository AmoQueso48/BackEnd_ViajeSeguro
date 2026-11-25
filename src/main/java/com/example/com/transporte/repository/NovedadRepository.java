package com.example.com.transporte.repository;

import com.example.com.transporte.model.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NovedadRepository extends JpaRepository<Novedad, Long> {
    List<Novedad> findByViajeId(Long viajeId);
}