package com.DAOgenerate.entity;

public class Vendas {
    private Object id;
    private Object id_cliente;
    private Object id_funcionario;
    private java.sql.Date data_venda;
    private Object valor;

    public Vendas() {
        // Construtor vazio
    }

    public Vendas(Object id, Object id_cliente, Object id_funcionario, java.sql.Date data_venda, Object valor) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.id_funcionario = id_funcionario;
        this.data_venda = data_venda;
        this.valor = valor;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Object id_cliente) {
        this.id_cliente = id_cliente;
    }

    public Object getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(Object id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public java.sql.Date getData_venda() {
        return data_venda;
    }

    public void setData_venda(java.sql.Date data_venda) {
        this.data_venda = data_venda;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
}
