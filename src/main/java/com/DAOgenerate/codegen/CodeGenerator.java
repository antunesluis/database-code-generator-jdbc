package com.DAOgenerate.codegen;

import java.io.PrintWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CodeGenerator {
    public static void generateClasses(List<String> tableNames, DatabaseMetaData metaData) {
        try {
            for (String tableName : tableNames) {
                ResultSet columns = metaData.getColumns(null, null, tableName, null);
                String packageName = "com.DAOgenerate.entity";

                // Geração do modelo
                StringBuilder modelClass = new StringBuilder();
                generateModelClass(modelClass, tableName, columns, packageName);
                saveToFile("src/main/java/com/DAOgenerate/entity/" + capitalize(tableName) + ".java", modelClass.toString());

                // Geração do DAO
                StringBuilder daoClass = new StringBuilder();
                generateDaoClass(daoClass, tableName);
                saveToFile("src/main/java/com/DAOgenerate/dao/" + capitalize(tableName) + "Dao.java", daoClass.toString());

                // Geração da implementação do DAO
                StringBuilder daoImplClass = new StringBuilder();
                generateDaoImplClass(daoImplClass, tableName, columns);
                saveToFile("src/main/java/com/DAOgenerate/dao/" + capitalize(tableName) + "DaoJDBC.java", daoImplClass.toString());

                // Geração da classe de exemplo
                generateExampleClass(tableName);
            }

            // Geração da fábrica de DAOs com todos os nomes de tabelas
            StringBuilder factoryClass = new StringBuilder();
            generateDaoFactory(factoryClass, tableNames);
            saveToFile("src/main/java/com/DAOgenerate/dao/DaoFactory.java", factoryClass.toString());

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateModelClass(StringBuilder modelClass, String tableName, ResultSet columns, String packageName) throws SQLException {
        modelClass.append("package ").append(packageName).append(";\n\n");
        modelClass.append("public class ").append(capitalize(tableName)).append(" {\n");

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");

            modelClass.append("    private ").append(mapSqlTypeToJava(columnType)).append(" ")
                    .append(lowerCamelCase(columnName)).append(";\n");
        }

        // Chamada para gerar o construtor
        columns.beforeFirst(); // Reinicia o cursor para o uso posterior
        generateConstructor(modelClass, tableName, columns);

        // Geração de getters e setters
        columns.beforeFirst(); // Reinicia o cursor novamente
        generateGettersAndSetters(modelClass, columns);
        modelClass.append("}\n");
    }

    private static void generateConstructor(StringBuilder modelClass, String tableName, ResultSet columns) throws SQLException {
        // Construtor vazio
        modelClass.append("\n    public ").append(capitalize(tableName)).append("() {\n");
        modelClass.append("        // Construtor vazio\n");
        modelClass.append("    }\n");

        // Construtor com parâmetros
        modelClass.append("\n    public ").append(capitalize(tableName)).append("(");
        boolean first = true;

        while (columns.next()) {
            if (!first) modelClass.append(", ");
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            modelClass.append(mapSqlTypeToJava(columnType)).append(" ").append(lowerCamelCase(columnName));
            first = false;
        }
        modelClass.append(") {\n");

        columns.beforeFirst(); // Reinicia o cursor novamente

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            modelClass.append("        this.").append(lowerCamelCase(columnName)).append(" = ")
                    .append(lowerCamelCase(columnName)).append(";\n");
        }
        modelClass.append("    }\n");
    }

    private static void generateGettersAndSetters(StringBuilder modelClass, ResultSet columns) throws SQLException {
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");

            // Getter
            modelClass.append("\n    public ").append(mapSqlTypeToJava(columnType)).append(" get")
                    .append(capitalize(lowerCamelCase(columnName))).append("() {\n");
            modelClass.append("        return ").append(lowerCamelCase(columnName)).append(";\n    }\n");

            // Setter
            modelClass.append("\n    public void set").append(capitalize(lowerCamelCase(columnName)))
                    .append("(").append(mapSqlTypeToJava(columnType)).append(" ").append(lowerCamelCase(columnName)).append(") {\n");
            modelClass.append("        this.").append(lowerCamelCase(columnName)).append(" = ")
                    .append(lowerCamelCase(columnName)).append(";\n    }\n");
        }
    }

    private static void generateDaoClass(StringBuilder daoClass, String tableName) {
        String className = capitalize(tableName);
        daoClass.append("package com.DAOgenerate.dao;\n\n");
        daoClass.append("import com.DAOgenerate.entity.").append(className).append(";\n");
        daoClass.append("import java.util.List;\n\n");

        daoClass.append("public interface ").append(className).append("Dao {\n");
        daoClass.append("    void insert(").append(className).append(" obj);\n");
        daoClass.append("    void update(").append(className).append(" obj);\n");
        daoClass.append("    void delete(Integer id);\n");
        daoClass.append("    ").append(className).append(" findById(Integer id);\n");
        daoClass.append("    List<").append(className).append("> findAll();\n");
        daoClass.append("}\n");
    }

    private static void generateDaoImplClass(StringBuilder daoImplClass, String tableName, ResultSet columns) throws SQLException {
        String className = capitalize(tableName);
        String variableName = lowerCamelCase(tableName);

        daoImplClass.append("package com.DAOgenerate.dao;\n\n");
        daoImplClass.append("import java.sql.*;\n");
        daoImplClass.append("import java.util.ArrayList;\n");
        daoImplClass.append("import java.util.List;\n");
        daoImplClass.append("import com.DAOgenerate.entity.").append(className).append(";\n\n");

        daoImplClass.append("public class ").append(className).append("DaoJDBC implements ").append(className).append("Dao {\n");
        daoImplClass.append("    private Connection conn;\n\n");
        daoImplClass.append("    public ").append(className).append("DaoJDBC(Connection conn) {\n");
        daoImplClass.append("        this.conn = conn;\n");
        daoImplClass.append("    }\n\n");

        generateInsertMethod(daoImplClass, className, tableName, columns);
        generateUpdateMethod(daoImplClass, className, tableName, columns);
        generateDeleteMethod(daoImplClass, tableName);
        generateFindByIdMethod(daoImplClass, className, tableName, columns);
        generateFindAllMethod(daoImplClass, className, tableName, columns);

        daoImplClass.append("}\n");
    }

    private static void generateInsertMethod(StringBuilder daoImplClass, String className, String tableName, ResultSet columns) throws SQLException {
        daoImplClass.append("    @Override\n");
        daoImplClass.append("    public void insert(").append(className).append(" obj) {\n");
        daoImplClass.append("        String sql = \"INSERT INTO ").append(tableName).append(" (");

        columns.beforeFirst();
        boolean first = true;
        while (columns.next()) {
            if (!first) {
                daoImplClass.append(", ");
            }
            daoImplClass.append(columns.getString("COLUMN_NAME"));
            first = false;
        }
        daoImplClass.append(") VALUES (");

        columns.beforeFirst();
        first = true;
        while (columns.next()) {
            if (!first) {
                daoImplClass.append(", ");
            }
            daoImplClass.append("?");
            first = false;
        }
        daoImplClass.append(")\";\n");
        daoImplClass.append("        try (PreparedStatement st = conn.prepareStatement(sql)) {\n");

        columns.beforeFirst();
        int index = 1;
        while (columns.next()) {
            daoImplClass.append("            st.setObject(").append(index).append(", obj.get")
                    .append(capitalize(lowerCamelCase(columns.getString("COLUMN_NAME")))).append("());\n");
            index++;
        }
        daoImplClass.append("            st.executeUpdate();\n");
        daoImplClass.append("        } catch (SQLException e) {\n");
        daoImplClass.append("            e.printStackTrace();\n");
        daoImplClass.append("        }\n");
        daoImplClass.append("    }\n\n");
    }

    private static void generateUpdateMethod(StringBuilder daoImplClass, String className, String tableName, ResultSet columns) throws SQLException {
        daoImplClass.append("    @Override\n");
        daoImplClass.append("    public void update(").append(className).append(" obj) {\n");
        daoImplClass.append("        String sql = \"UPDATE ").append(tableName).append(" SET ");

        columns.beforeFirst();
        boolean first = true;
        while (columns.next()) {
            if (!first) {
                daoImplClass.append(", ");
            }
            daoImplClass.append(columns.getString("COLUMN_NAME")).append(" = ?");
            first = false;
        }
        daoImplClass.append(" WHERE id = ?\";\n");
        daoImplClass.append("        try (PreparedStatement st = conn.prepareStatement(sql)) {\n");

        columns.beforeFirst();
        int index = 1;
        while (columns.next()) {
            daoImplClass.append("            st.setObject(").append(index).append(", obj.get")
                    .append(capitalize(lowerCamelCase(columns.getString("COLUMN_NAME")))).append("());\n");
            index++;
        }
        daoImplClass.append("            st.setInt(").append(index).append(", (int) obj.getId());\n");
        daoImplClass.append("            st.executeUpdate();\n");
        daoImplClass.append("        } catch (SQLException e) {\n");
        daoImplClass.append("            e.printStackTrace();\n");
        daoImplClass.append("        }\n");
        daoImplClass.append("    }\n\n");
    }

    private static void generateDeleteMethod(StringBuilder daoImplClass, String tableName) {
        daoImplClass.append("    @Override\n");
        daoImplClass.append("    public void delete(Integer id) {\n");
        daoImplClass.append("        String sql = \"DELETE FROM ").append(tableName).append(" WHERE id = ?\";\n");
        daoImplClass.append("        try (PreparedStatement st = conn.prepareStatement(sql)) {\n");
        daoImplClass.append("            st.setInt(1, id);\n");
        daoImplClass.append("            st.executeUpdate();\n");
        daoImplClass.append("        } catch (SQLException e) {\n");
        daoImplClass.append("            e.printStackTrace();\n");
        daoImplClass.append("        }\n");
        daoImplClass.append("    }\n\n");
    }

    private static void generateFindByIdMethod(StringBuilder daoImplClass, String className, String tableName, ResultSet columns) throws SQLException {
        daoImplClass.append("    @Override\n");
        daoImplClass.append("    public ").append(className).append(" findById(Integer id) {\n");
        daoImplClass.append("        String sql = \"SELECT * FROM ").append(tableName).append(" WHERE id = ?\";\n");
        daoImplClass.append("        try (PreparedStatement st = conn.prepareStatement(sql)) {\n");
        daoImplClass.append("            st.setInt(1, id);\n");
        daoImplClass.append("            try (ResultSet rs = st.executeQuery()) {\n");
        daoImplClass.append("                if (rs.next()) {\n");
        daoImplClass.append("                    ").append(className).append(" obj = new ").append(className).append("();\n");

        columns.beforeFirst();
        while (columns.next()) {
            daoImplClass.append("                    obj.set").append(capitalize(lowerCamelCase(columns.getString("COLUMN_NAME"))))
                    .append("(rs.get").append(getResultSetGetter(columns.getString("TYPE_NAME"))).append("(\"")
                    .append(columns.getString("COLUMN_NAME")).append("\"));\n");
        }

        daoImplClass.append("                    return obj;\n");
        daoImplClass.append("                }\n");
        daoImplClass.append("            }\n");
        daoImplClass.append("        } catch (SQLException e) {\n");
        daoImplClass.append("            e.printStackTrace();\n");
        daoImplClass.append("        }\n");
        daoImplClass.append("        return null;\n");
        daoImplClass.append("    }\n\n");
    }

    private static void generateFindAllMethod(StringBuilder daoImplClass, String className, String tableName, ResultSet columns) throws SQLException {
        daoImplClass.append("    @Override\n");
        daoImplClass.append("    public List<").append(className).append("> findAll() {\n");
        daoImplClass.append("        String sql = \"SELECT * FROM ").append(tableName).append("\";\n");
        daoImplClass.append("        List<").append(className).append("> list = new ArrayList<>();\n");
        daoImplClass.append("        try (PreparedStatement st = conn.prepareStatement(sql)) {\n");
        daoImplClass.append("            try (ResultSet rs = st.executeQuery()) {\n");
        daoImplClass.append("                while (rs.next()) {\n");
        daoImplClass.append("                    ").append(className).append(" obj = new ").append(className).append("();\n");

        columns.beforeFirst();
        while (columns.next()) {
            daoImplClass.append("                    obj.set").append(capitalize(lowerCamelCase(columns.getString("COLUMN_NAME"))))
                    .append("(rs.get").append(getResultSetGetter(columns.getString("TYPE_NAME"))).append("(\"")
                    .append(columns.getString("COLUMN_NAME")).append("\"));\n");
        }

        daoImplClass.append("                    list.add(obj);\n");
        daoImplClass.append("                }\n");
        daoImplClass.append("            }\n");
        daoImplClass.append("        } catch (SQLException e) {\n");
        daoImplClass.append("            e.printStackTrace();\n");
        daoImplClass.append("        }\n");
        daoImplClass.append("        return list;\n");
        daoImplClass.append("    }\n\n");
    }

    private static void generateDaoFactory(StringBuilder factoryClass, List<String> tableNames) {
        factoryClass.append("package com.DAOgenerate.dao;\n\n");
        factoryClass.append("import java.sql.Connection;\n\n");
        factoryClass.append("public class DaoFactory {\n");
        factoryClass.append("    private Connection conn;\n\n");
        factoryClass.append("    public DaoFactory(Connection conn) {\n");
        factoryClass.append("        this.conn = conn;\n");
        factoryClass.append("    }\n\n");

        for (String tableName : tableNames) {
            String className = capitalize(tableName);
            factoryClass.append("    public ").append(className).append("Dao create").append(className).append("Dao() {\n");
            factoryClass.append("        return new ").append(className).append("DaoJDBC(conn);\n");
            factoryClass.append("    }\n\n");
        }
        factoryClass.append("}\n");
    }
    private static void generateExampleClass(String tableName) {
        String className = capitalize(tableName);
        String variableName = lowerCamelCase(tableName);

        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/java/com/DAOgenerate/example/" + className + "Example.java"))) {
            writer.println("package com.DAOgenerate.example;\n");
            writer.println("import java.sql.*;");
            writer.println("import java.util.List;");
            writer.println("import com.DAOgenerate.dao.*;");
            writer.println("import com.DAOgenerate.entity." + className + ";\n");
            writer.println("public class " + className + "Example {");
            writer.println("    public static void main(String[] args) {");
            writer.println("        // Configurar a conexão com o banco de dados");
            writer.println("        try (Connection conn = DriverManager.getConnection(\"<url>\", \"<user>\", \"<password>\")) {");
            writer.println("            DaoFactory daoFactory = new DaoFactory(conn);");
            writer.println("            " + className + "Dao dao = daoFactory.create" + className + "Dao();");
            writer.println();
            writer.println("            // Criar um novo objeto " + className);
            writer.println("            " + className + " " + variableName + " = new " + className + "();");
            writer.println("            // Preencher o objeto com dados");
            writer.println("            // Exemplo: " + variableName + ".setField(value);");
            writer.println();
            writer.println("            // Inserir o objeto no banco de dados");
            writer.println("            dao.insert(" + variableName + ");");
            writer.println();
            writer.println("            // Buscar um objeto pelo ID");
            writer.println("            " + className + " found = dao.findById(1);");
            writer.println("            System.out.println(found);");
            writer.println();
            writer.println("            // Buscar todos os objetos");
            writer.println("            List<" + className + "> all = dao.findAll();");
            writer.println("            System.out.println(all);");
            writer.println("        } catch (SQLException e) {");
            writer.println("            e.printStackTrace();");
            writer.println("        }");
            writer.println("    }");
            writer.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile(String filePath, String content) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private static String lowerCamelCase(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private static String mapSqlTypeToJava(String sqlType) {
        return switch (sqlType.toUpperCase()) {
            case "VARCHAR", "TEXT" -> "String";
            case "INT" -> "Integer";
            case "FLOAT", "DOUBLE" -> "Double";
            case "BOOLEAN" -> "Boolean";
            case "DATE" -> "java.sql.Date";
            default -> "Object"; // Tipo genérico
        };
    }

    private static String getResultSetGetter(String sqlType) {
        return switch (sqlType.toUpperCase()) {
            case "VARCHAR", "CHAR", "TEXT" -> "String";
            case "INT", "INTEGER" -> "Int";
            case "DOUBLE", "FLOAT" -> "Double";
            case "DATE", "TIMESTAMP" -> "Timestamp";
            default -> "Object";
        };
    }
}