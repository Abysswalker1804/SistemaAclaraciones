package org.example.sistemaaclaraciones.DAO;

import org.example.sistemaaclaraciones.Conexion.Conexion;

import java.sql.ResultSet;
import java.sql.Statement;

public class InscripcionDAO {
    private short id;
    private String no_control;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getNo_control() {
        return no_control;
    }

    public void setNo_control(String no_control) {
        this.no_control = no_control;
    }
    public static void INSCRIBIR(String no_control, String id){
        String query="insert into inscripcion (no_control,id) values ('"+no_control+"',"+id+")";
        try{
            Statement stmt= Conexion.connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }catch(Exception e){
            //throw new IllegalArgumentException("Inscripción no válida");
            System.out.println(e.getMessage());
            System.out.println("Problema en InscripcionDAO");
        }
    }
    public static void DESINSCRIBIR(String no_control, String id){
        String query="delete from inscripcion where no_control='"+no_control+"' and id='"+id+"'";
        try{
            Statement stmt= Conexion.connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }catch(Exception e){
            //throw new IllegalArgumentException("Inscripción no válida");
            System.out.println(e.getMessage());
            System.out.println("Problema en DesinscripcionDAO");
        }
    }
}
