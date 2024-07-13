package com.DAOgenerate.example;

import java.sql.*;
import java.util.List;
import com.DAOgenerate.dao.*;
import com.DAOgenerate.entity.Funcionarios;

public class FuncionariosExample {
    public static void main(String[] args) {
        // Configurar a conexão com o banco de dados
        try (Connection conn = DriverManager.getConnection("<url>", "<user>", "<password>")) {
            DaoFactory daoFactory = new DaoFactory(conn);
            FuncionariosDao dao = daoFactory.createFuncionariosDao();

            // Criar um novo objeto Funcionarios
            Funcionarios funcionarios = new Funcionarios();
            // Preencher o objeto com dados
            // Exemplo: funcionarios.setField(value);

            // Inserir o objeto no banco de dados
            dao.insert(funcionarios);

            // Buscar um objeto pelo ID
            Funcionarios found = dao.findById(1);
            System.out.println(found);

            // Buscar todos os objetos
            List<Funcionarios> all = dao.findAll();
            System.out.println(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
