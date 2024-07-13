package com.DAOgenerate.dao;

import com.DAOgenerate.entity.Vendas;
import java.util.List;

public interface VendasDao {
    void insert(Vendas obj);
    void update(Vendas obj);
    void delete(Integer id);
    Vendas findById(Integer id);
    List<Vendas> findAll();
}
