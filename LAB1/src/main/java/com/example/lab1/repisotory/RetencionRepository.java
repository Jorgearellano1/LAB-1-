package com.example.lab1.repisotory;
import com.example.lab1.model.Retencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetencionRepository extends JpaRepository<Retencion, Long> {
    List<Retencion> findByTrabajadorId(Long trabajadorId);
}


