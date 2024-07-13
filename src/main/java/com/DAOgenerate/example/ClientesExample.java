package com.DAOgenerate.example;

import java.sql.*;
import java.util.List;
import com.DAOgenerate.dao.*;
import com.DAOgenerate.entity.Clientes;

public class ClientesExample {
    public static void main(String[] args) {
        // Configurar a conexão com o banco de dados
        try (Connection conn = DriverManager.getConnection("<url>", "<user>", "<password>")) {
            DaoFactory daoFactory = new DaoFactory(conn);
            ClientesDao dao = daoFactory.createClientesDao();

            // Criar um novo objeto Clientes
            Clientes clientes = new Clientes();
            // Preencher o objeto com dados
            // Exemplo: clientes.setField(value);

            // Inserir o objeto no banco de dados
            dao.insert(clientes);

            // Buscar um objeto pelo ID
            Clientes found = dao.findById(1);
            System.out.println(found);

            // Buscar todos os objetos
            List<Clientes> all = dao.findAll();
            System.out.println(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
