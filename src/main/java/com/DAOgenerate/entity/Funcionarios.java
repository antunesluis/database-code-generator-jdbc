package com.DAOgenerate.entity;

public class Funcionarios {
    private Object id;
    private String nome;
    private String cargo;
    private Object salario;

    public Funcionarios() {
        // Construtor vazio
    }

    public Funcionarios(Object id, String nome, String cargo, Object salario) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Object getSalario() {
        return salario;
    }

    public void setSalario(Object salario) {
        this.salario = salario;
    }
}
