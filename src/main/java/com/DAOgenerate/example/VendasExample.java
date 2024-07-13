package com.DAOgenerate.example;

import java.sql.*;
import java.util.List;
import com.DAOgenerate.dao.*;
import com.DAOgenerate.entity.Vendas;

public class VendasExample {
    public static void main(String[] args) {
        // Configurar a conexão com o banco de dados
        try (Connection conn = DriverManager.getConnection("<url>", "<user>", "<password>")) {
            DaoFactory daoFactory = new DaoFactory(conn);
            VendasDao dao = daoFactory.createVendasDao();

            // Criar um novo objeto Vendas
            Vendas vendas = new Vendas();
            // Preencher o objeto com dados
            // Exemplo: vendas.setField(value);

            // Inserir o objeto no banco de dados
            dao.insert(vendas);

            // Buscar um objeto pelo ID
            Vendas found = dao.findById(1);
            System.out.println(found);

            // Buscar todos os objetos
            List<Vendas> all = dao.findAll();
            System.out.println(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
