package com.william.ahelpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.william.ahelpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer>{

}
