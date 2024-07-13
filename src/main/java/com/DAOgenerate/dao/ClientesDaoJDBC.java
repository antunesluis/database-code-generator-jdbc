package com.DAOgenerate.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.DAOgenerate.entity.Clientes;

public class ClientesDaoJDBC implements ClientesDao {
    private Connection conn;

    public ClientesDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Clientes obj) {
        String sql = "INSERT INTO clientes (id, nome, email, telefone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, obj.getId());
            st.setObject(2, obj.getNome());
            st.setObject(3, obj.getEmail());
            st.setObject(4, obj.getTelefone());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Clientes obj) {
        String sql = "UPDATE clientes SET id = ?, nome = ?, email = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, obj.getId());
            st.setObject(2, obj.getNome());
            st.setObject(3, obj.getEmail());
            st.setObject(4, obj.getTelefone());
            st.setInt(5, (int) obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Clientes findById(Integer id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Clientes obj = new Clientes();
                    obj.setId(rs.getObject("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setEmail(rs.getString("email"));
                    obj.setTelefone(rs.getString("telefone"));
                    return obj;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Clientes> findAll() {
        String sql = "SELECT * FROM clientes";
        List<Clientes> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Clientes obj = new Clientes();
                    obj.setId(rs.getObject("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setEmail(rs.getString("email"));
                    obj.setTelefone(rs.getString("telefone"));
                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
