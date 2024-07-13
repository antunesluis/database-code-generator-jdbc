package com.DAOgenerate.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.DAOgenerate.entity.Vendas;

public class VendasDaoJDBC implements VendasDao {
    private Connection conn;

    public VendasDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Vendas obj) {
        String sql = "INSERT INTO vendas (id, id_cliente, id_funcionario, data_venda, valor) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, obj.getId());
            st.setObject(2, obj.getId_cliente());
            st.setObject(3, obj.getId_funcionario());
            st.setObject(4, obj.getData_venda());
            st.setObject(5, obj.getValor());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Vendas obj) {
        String sql = "UPDATE vendas SET id = ?, id_cliente = ?, id_funcionario = ?, data_venda = ?, valor = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, obj.getId());
            st.setObject(2, obj.getId_cliente());
            st.setObject(3, obj.getId_funcionario());
            st.setObject(4, obj.getData_venda());
            st.setObject(5, obj.getValor());
            st.setInt(6, (int) obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM vendas WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vendas findById(Integer id) {
        String sql = "SELECT * FROM vendas WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Vendas obj = new Vendas();
                    obj.setId(rs.getObject("id"));
                    obj.setId_cliente(rs.getObject("id_cliente"));
                    obj.setId_funcionario(rs.getObject("id_funcionario"));
                    obj.setData_venda(rs.getTimestamp("data_venda"));
                    obj.setValor(rs.getObject("valor"));
                    return obj;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vendas> findAll() {
        String sql = "SELECT * FROM vendas";
        List<Vendas> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Vendas obj = new Vendas();
                    obj.setId(rs.getObject("id"));
                    obj.setId_cliente(rs.getObject("id_cliente"));
                    obj.setId_funcionario(rs.getObject("id_funcionario"));
                    obj.setData_venda(rs.getTimestamp("data_venda"));
                    obj.setValor(rs.getObject("valor"));
                    list.add(obj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
