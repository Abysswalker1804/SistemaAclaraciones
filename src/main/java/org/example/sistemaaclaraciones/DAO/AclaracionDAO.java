package org.example.sistemaaclaraciones.DAO;

import org.example.sistemaaclaraciones.Conexion.Conexion;

import java.sql.Statement;

public class AclaracionDAO {
    private int id;
    private String aclaracion;
    private String no_control;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAclaracion() {
        return aclaracion;
    }

    public void setAclaracion(String aclaracion) {
        this.aclaracion = aclaracion;
    }

    public String getNo_control() {
        return no_control;
    }

    public void setNo_control(String no_control) {
        this.no_control = no_control;
    }
    public static void mandarAclaracion(String no_control, String aclaracion){
        String query="insert into aclaracion(no_control, aclaracion) values ('"+no_control+"','"+aclaracion+"')";
        try{
            Statement stmt= Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch (Exception e){
            System.out.println("Error en Mandar Aclaración");
        }
    }
}
