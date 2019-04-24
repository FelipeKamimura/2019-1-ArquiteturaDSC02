/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dao;

import br.edu.utfpr.dto.PaisDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.java.Log;

/**
 *
 * @author Kamimura
 */
@Log
public abstract class DAO <T> {
      
    abstract String sqlIncluir();
    abstract PreparedStatement defineStatementIncluir (Object dto, Connection conn, String sql)throws SQLException;
    
    public boolean incluir(T dto) {
        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            int rowsInserted = defineStatementIncluir(dto, conn, sqlIncluir()).executeUpdate();

            if (rowsInserted > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    
    abstract String sqlListarTodos();
    abstract T retornaObjetoListAll(ResultSet result);
    
    public List<T> listarTodos() {

        List<T> resultado = new ArrayList<>();

        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            String sql = sqlListarTodos();

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {

                resultado.add(
                        retornaObjetoListAll(result)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    abstract String sqlExcluir();
    
    public boolean excluir(int id) {

        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            String sql = sqlExcluir();

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    abstract String sqlAlterar();
    abstract PreparedStatement defineStatementAlterar (Object dto, Connection conn, String sql)throws SQLException;
    public boolean alterar(T dto) {
        try ( Connection conn = DriverManager.getConnection("jdbc:derby:memory:database")) {

            String sql = sqlAlterar();
   

            int rowsUpdated = defineStatementAlterar(dto, conn, sql).executeUpdate();
            if (rowsUpdated > 0) 
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public T listarPorId (int id) {
        return this.listarTodos().stream().filter(p -> p.getId() == id).findAny().orElseThrow(RuntimeException::new);
    }

}
