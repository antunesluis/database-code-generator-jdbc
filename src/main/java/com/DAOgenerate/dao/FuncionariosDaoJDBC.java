package com.DAOgenerate.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.DAOgenerate.entity.Funcionarios;

public class FuncionariosDaoJDBC implements FuncionariosDao {
    private Connection conn;

    public FuncionariosDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Funcionarios obj) {
        String sql = "INSERT INTO funcionarios (id, nome, cargo, salario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, obj.getId());
            st.setObject(2, obj.getNome());
            st.setObject(3, obj.getCargo());
            st.setObject(4, obj.getSalario());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Funcionarios obj) {
        String sql = "UPDATE funcionarios SET id = ?, nome = ?, cargo = ?, salario = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, obj.getId());
            st.setObject(2, obj.getNome());
            st.setObject(3, obj.getCargo());
            st.setObject(4, obj.getSalario());
            st.setInt(5, (int) obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM funcionarios WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Funcionarios findById(Integer id) {
        String sql = "SELECT * FROM funcionarios WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Funcionarios obj = new Funcionarios();
                    obj.setId(rs.getObject("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setCargo(rs.getString("cargo"));
                    obj.setSalario(rs.getObject("salario"));
                    return obj;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Funcionarios> findAll() {
        String sql = "SELECT * FROM funcionarios";
        List<Funcionarios> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Funcionarios obj = new Funcionarios();
                    obj.setId(rs.getObject("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setCargo(rs.getString("cargo"));
                    obj.setSalario(rs.getObject("salario"));
                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
