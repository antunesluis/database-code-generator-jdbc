package com.DAOgenerate.dao;

import com.DAOgenerate.entity.Clientes;
import java.util.List;

public interface ClientesDao {
    void insert(Clientes obj);
    void update(Clientes obj);
    void delete(Integer id);
    Clientes findById(Integer id);
    List<Clientes> findAll();
}
