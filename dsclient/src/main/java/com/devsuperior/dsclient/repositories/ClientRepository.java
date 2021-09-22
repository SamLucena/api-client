package com.devsuperior.dsclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dsclient.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
}
