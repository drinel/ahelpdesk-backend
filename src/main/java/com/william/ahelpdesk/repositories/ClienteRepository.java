package com.william.ahelpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.william.ahelpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
