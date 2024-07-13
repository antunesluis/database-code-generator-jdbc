package com.DAOgenerate.dao;

import com.DAOgenerate.entity.Funcionarios;
import java.util.List;

public interface FuncionariosDao {
    void insert(Funcionarios obj);
    void update(Funcionarios obj);
    void delete(Integer id);
    Funcionarios findById(Integer id);
    List<Funcionarios> findAll();
}
