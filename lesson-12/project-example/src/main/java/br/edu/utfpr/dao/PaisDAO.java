package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import br.edu.utfpr.dto.PaisDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.java.Log;

@Log
public class PaisDAO extends DAO{

    public PaisDAO() {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {

            log.info("Criando tabela pais ...");
            conn.createStatement().executeUpdate(
            "CREATE TABLE pais (" +
		"id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_pais_pk PRIMARY KEY," +
		"nome varchar(255)," +
		"sigla varchar(3)," + 
		"codigoTelefone int)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    String sqlIncluir(){
        return "INSERT INTO pais (nome, sigla, codigoTelefone) VALUES (?, ?, ?)";
    }
    
    @Override
    PreparedStatement defineStatementIncluir (Object dto, Connection conn, String sql) throws SQLException{
            PreparedStatement statement = conn.prepareStatement(sql);
            PaisDTO pais = (PaisDTO) dto;
            statement.setString(1, pais.getNome());
            statement.setString(2, pais.getSigla());
            statement.setInt(3, pais.getCodigoTelefone());
        return statement;
    }
    @Override
    String sqlListarTodos(){
        return "SELECT * FROM pais";
    }
    
    
    PaisDTO retornaObjetoListAll(ResultSet result){
        try {
            return  PaisDTO.builder()
                    .codigoTelefone(result.getInt("codigoTelefone"))
                    .id(result.getInt("id"))
                    .nome(result.getString("nome"))
                    .sigla(result.getString("sigla"))
                    .build();
        } catch (SQLException ex) {
            Logger.getLogger(PaisDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    String sqlExcluir(){
       return "DELETE FROM pais WHERE id=?";
    }
    
    @Override
    String sqlAlterar(){
        return "UPDATE pais SET nome=?, sigla=?, codigoTelefone=? WHERE id=?";
    }
    @Override
    PreparedStatement defineStatementAlterar (Object dto, Connection conn, String sql)throws SQLException{
        PaisDTO pais = (PaisDTO) dto;
        PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, pais.getNome());
            statement.setString(2, pais.getSigla());
            statement.setInt(3, pais.getCodigoTelefone());
            return statement;
    }

   

}
