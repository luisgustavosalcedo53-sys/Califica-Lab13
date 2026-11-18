package com.tecsup.calificaciones.infrastructure.adapter.out.persistence.repository;

import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity.StreamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamJpaRepository extends JpaRepository<StreamEntity, Long> {}
