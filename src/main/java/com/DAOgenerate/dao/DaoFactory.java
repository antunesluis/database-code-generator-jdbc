package com.DAOgenerate.dao;

import java.sql.Connection;

public class DaoFactory {
    private Connection conn;

    public DaoFactory(Connection conn) {
        this.conn = conn;
    }

    public ClientesDao createClientesDao() {
        return new ClientesDaoJDBC(conn);
    }

    public FuncionariosDao createFuncionariosDao() {
        return new FuncionariosDaoJDBC(conn);
    }

    public VendasDao createVendasDao() {
        return new VendasDaoJDBC(conn);
    }

}
