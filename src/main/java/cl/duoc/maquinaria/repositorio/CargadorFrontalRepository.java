package cl.duoc.maquinaria.repositorio;

import cl.duoc.maquinaria.conexion.ConexionSQL;
// cambiar esto
import cl.duoc.maquinaria.model.CargadorFrontal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

// cambiar esto
public class CargadorFrontalRepository {

    // metodo obtener todos:
    public ObservableList<CargadorFrontal> obtenerTodos() throws SQLException {
        // cambiar clase y nombre lista
        ObservableList<CargadorFrontal> listaCargadores = FXCollections.observableArrayList();
        //  cambiar db del FROM y en atributos poner los propios mas lo de superclase:
        // ojo con que el nombre de los atributos es el de la DB no de java:
        String sql = "SELECT id, codigo, horas_uso, potencia, capacidad_balde FROM cargador_frontal";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                // cambiar clases y nombre objeto
                CargadorFrontal cargadorFrontal = new CargadorFrontal(
                        // cambiar atributos sin id:
                        resultado.getString("codigo"),
                        resultado.getInt("horas_uso"),
                        resultado.getDouble("potencia"),
                        resultado.getDouble("capacidad_balde"));
                // cambiar nombre objeto:
                cargadorFrontal.setId(resultado.getInt("id"));
                // borrar las certificacion porque no la tiene
                listaCargadores.add(cargadorFrontal);
            }
        }
        // cambiar nombre lista:
        return listaCargadores;
    }

    // metodo agregar. cambiar clase y nombre objeto:
    public void agregar(CargadorFrontal cargadorFrontal) throws SQLException {
        // cambiar nombre objeto y atributos solo los de superclase + 1 de esta clase:
        String sql = "INSERT INTO cargador_frontal (codigo, horas_uso, potencia, capacidad_balde) " + "VALUES (?,?,?,?)";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // reemplazar cada ? con el valor real del objeto cargadorFrontal + los heredados de Maquina menos la id:
            sentencia.setString(1, cargadorFrontal.getCodigo());
            sentencia.setInt(2, cargadorFrontal.getHorasUso());
            sentencia.setDouble(3, cargadorFrontal.getPotencia());
            sentencia.setDouble(4, cargadorFrontal.getCapacidadBalde());
            sentencia.executeUpdate();
            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                if (claves.next()) {
                    // cambiar nombre objeto
                    cargadorFrontal.setId(claves.getInt(1));
                }
            }
        }
    }

    // metodo actualizar. cambiar nombre clase y nombre objeto
    public void actualizar(CargadorFrontal cargadorFrontal) throws SQLException {
        // cambiar nombre objeto y atributos quedan los de superclase mas los de esta clase y la id
        String sql = "UPDATE cargador_frontal SET codigo=?, horas_uso=?, potencia=?, capacidad_balde=? WHERE id=?";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // copiar las sentencias del metodo agregar
            sentencia.setString(1, cargadorFrontal.getCodigo());
            sentencia.setInt(2, cargadorFrontal.getHorasUso());
            sentencia.setDouble(3, cargadorFrontal.getPotencia());
            sentencia.setDouble(4, cargadorFrontal.getCapacidadBalde());
            // el id se mantiene pero cambia el nombre objeto y su numero index al siguiente
            sentencia.setInt(5, cargadorFrontal.getId());
            sentencia.executeUpdate();
        }
    }

    // metodo eliminar
    public void eliminar(int id) throws SQLException {
        // cambiar nombre objeto
        String sql = "DELETE FROM cargador_frontal WHERE id=?";
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }
}