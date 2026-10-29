package org.example.sistemaaclaraciones.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.sistemaaclaraciones.Conexion.Conexion;

import java.sql.ResultSet;
import java.sql.Statement;

public class PrerequisitoDAO {
    private String materia_cursar;
    private String materia_requisito;

    public String getMateria_cursar() {
        return materia_cursar;
    }

    public void setMateria_cursar(String materia_cursar) {
        this.materia_cursar = materia_cursar;
    }

    public String getMateria_requisito() {
        return materia_requisito;
    }

    public void setMateria_requisito(String materia_requisito) {
        this.materia_requisito = materia_requisito;
    }
    public static ObservableList<String> getMateriasRequisito(String clave){
        ObservableList<String> lista= FXCollections.observableArrayList();
        String query="select materia_requisito as requisito from prerequisito where materia_cursar='"+clave+"'";
        try{
            Statement stmt= Conexion.connection.createStatement();
            ResultSet res= stmt.executeQuery(query);
            while(res.next()){
                lista.add(res.getString("requisito"));
            }
        }catch(Exception e){System.out.println("Error en PrerequisitoDAO");}
        return lista;
    }
}
