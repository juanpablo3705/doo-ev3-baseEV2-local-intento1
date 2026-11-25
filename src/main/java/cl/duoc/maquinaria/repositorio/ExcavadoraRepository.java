package cl.duoc.maquinaria.repositorio;

import cl.duoc.maquinaria.conexion.ConexionSQL;
import cl.duoc.maquinaria.model.Excavadora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ExcavadoraRepository {

    // metodo obtener todos. retorna observablelist:
    public ObservableList<Excavadora> obtenerTodos() throws SQLException {
        // crear lista vacía de excavadoras que vienen desde la base de datos:
        ObservableList<Excavadora> listaExcavadoras = FXCollections.observableArrayList();
        //  definir consulta sql; sacar nombres atributos de create table del archivo .sql del proyecto:
        String sql = "SELECT id, codigo, horas_uso, potencia, peso_toneladas, mantencion_al_dia, certificacion_seguridad FROM excavadora";
        // try with resources: todo lo que se declara de los parantesis se cierra al terminar el try.
        // la primera linea llama a obtenerConexion para abrir el nexo con MySQL.
        // acá sale el throw de la firma signature del metodo cuando se hace click:
        try (Connection conexion = ConexionSQL.obtenerConexion();
             // crear objeto para enviar sentencias SQL:
             Statement sentencia = conexion.createStatement();
             // ejecutar el SELECT en la BD y guardar tabla en resultado:
             ResultSet resultado = sentencia.executeQuery(sql)) {
            // recorrer con while todos los registros encontrados:
            while (resultado.next()) {
                // instanciar un objeto usando el constructor simple de Excavadora:
                Excavadora excavadora = new Excavadora(
                        // se usa el metodo getTipo segun el tipo de cada columna en SQL:
                        resultado.getString("codigo"),
                        resultado.getInt("horas_uso"),
                        resultado.getDouble("potencia"),
                        resultado.getDouble("peso_toneladas"),
                        resultado.getBoolean("mantencion_al_dia"));
                // pero el constructor simple no abarca id ni certificacion_seguridad, asi que estas dos lineas
                // van a completar el objeto (son sus setters porque no usé el constructor sobrecargado de Excavadora),
                // tambien podria usar el constructor sobrecargado de Excavadora si es que existe y ahi pondria
                // resultado.getInt("id") al inicio y resultado.getboolean("certificacion_seguridad") al final:
                excavadora.setId(resultado.getInt("id"));
                excavadora.setCertificacionSeguridad(resultado.getBoolean("certificacion_seguridad"));
                // y finalmente agregar el objeto excavadora ya construido a la lista de excavadoras:
                listaExcavadoras.add(excavadora);
            }
        }
    // devolver la listaExcavadoras con todo:
        return listaExcavadoras;
    }

    // metodo agregar. no retorna nada y recibe el objeto excavadora a agregar:
    public void agregar(Excavadora excavadora) throws SQLException {
        // definir la consulta INSERT sin incluir id porque es auto_increment:
        String sql = "INSERT INTO excavadora (codigo, horas_uso, potencia, peso_toneladas, mantencion_al_dia, certificacion_seguridad) " + "VALUES (?,?,?,?,?,?)"; // dejar espacio antes del +
        // try with resources. primero abrir la conexion. se añade la excepcion a signature aqui al hacer click:
        try (Connection conexion = ConexionSQL.obtenerConexion();
             // preparar la consulta. el generated keys devuelve la id:
             // "sentencia" es el objeto PreparedStatement que contiene la plantilla SQL con los ?:
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // vienen las lineas para rellenar los signos ? de la consulta sql preparada con PreparedStatement.
            // reemplazar cada ? con el valor real del objeto de Excavadora + los heredados de Maquina menos la id:
            sentencia.setString(1, excavadora.getCodigo());
            sentencia.setInt(2, excavadora.getHorasUso());
            sentencia.setDouble(3, excavadora.getPotencia());
            sentencia.setDouble(4, excavadora.getPesoToneladas());
            sentencia.setBoolean(5, excavadora.isMantencionAlDia());
            sentencia.setBoolean(6, excavadora.tieneCertificacionSeguridad());
            // ejecutar el INSERT en la DB:
            sentencia.executeUpdate();
            // guardar el nuevo id en el objeto java mediante setID:
            try (ResultSet claves = sentencia.getGeneratedKeys()) { // llamar a las ids
                if (claves.next()) { // avanzar en la fila con if porque hay 1 sola fila
                    // guardarla en el objeto:
                    excavadora.setId(claves.getInt(1));
                }
            }
        }
    }

    // metodo actualizar. guarda los cambios de un objeto ya existente en la DB:
    // metodo sin retorno y recibe el objeto excavadora:
    public void actualizar(Excavadora excavadora) throws SQLException {
        // definir la sentencia SQL, indica la tabla y las columnas que se van a reemplazar.
        // toma todos los atributos incluyendo la id que es el where:
        String sql = "UPDATE excavadora SET codigo=?, horas_uso=?, potencia=?, peso_toneladas=?, mantencion_al_dia=?, certificacion_seguridad=? WHERE id=?";
        // abrir la conexion y hacer el preparedstatement
        // añadir exception con click en al signature no con el catch:
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // reemplazar los primeros 6 signos ? con valores del objeto de la subclase y superclase
            // tienen que ir en el mismo orden que aparecen en la consulta SQL.
            // copia y pega los de arriba:
            sentencia.setString(1, excavadora.getCodigo());
            sentencia.setInt(2, excavadora.getHorasUso());
            sentencia.setDouble(3, excavadora.getPotencia());
            sentencia.setDouble(4, excavadora.getPesoToneladas());
            sentencia.setBoolean(5, excavadora.isMantencionAlDia());
            sentencia.setBoolean(6, excavadora.tieneCertificacionSeguridad());
            // y luego agrega el 7mo que es el id porque es el del where id=? que indoca qué registro modificar:
            sentencia.setInt(7, excavadora.getId());
            // ejecutar el UPDATE:
            sentencia.executeUpdate();
        }
    }

    // metodo eliminar usando la id solamente sin pasar todo el objeto:
    // metodo sin retorno que recibe solo el int id de lo que quiero borrar:
    public void eliminar(int id) throws SQLException {
        // definir la sentencia SQL DELETE, con el where id=?:
        String sql = "DELETE FROM excavadora WHERE id=?";
        // abrir la conexion con SQL y preparar la sentencia. añade la excepcion con click:
        try (Connection conexion = ConexionSQL.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // reemplazar el unico signo ? en la posicion 1 por el valor id recibido por parametro:
            sentencia.setInt(1, id);
            // ejecutar el DELETE:
            sentencia.executeUpdate();
        }
    }
}