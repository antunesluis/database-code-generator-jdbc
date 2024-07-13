package com.DAOgenerate;

import com.DAOgenerate.codegen.CodeGenerator;
import com.DAOgenerate.util.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DB.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            List<String> tableNames = new ArrayList<>();

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                tableNames.add(tableName); // Adiciona o nome da tabela à lista
            }

            // Chama a função generateClasses com a lista de nomes de tabelas
            CodeGenerator.generateClasses(tableNames, metaData);

        } catch (Exception e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}