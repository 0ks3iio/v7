package net.zdsoft.remote.openapi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.zdsoft.remote.openapi.entity.OpenApiEntityTicket;

public interface OpenApiEntityTicketJpaDao extends JpaRepository<OpenApiEntityTicket, String> {
    public List<OpenApiEntityTicket> findByTicketKeyAndTypeIn(String ticketKey, String... types);
    
}
