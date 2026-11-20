package cl.duoc.maquinaria.model;

/**
 * Cargador frontal que arrienda la empresa.
 * Costo del servicio: base $120.000; si la capacidad del balde supera los 3 metros cúbicos, recargo del 20%.
 */
public class CargadorFrontal extends Maquina {

    private static final double COSTO_BASE = 120000;
    private static final double RECARGO_CAPACIDAD_BALDE = 0.20;
    private static final double LIMITE_CAPACIDAD = 3.0;

    private double capacidadBalde;

    /**
     * Constructor sin parámetros, requerido para reconstruir objetos.
     */
    public CargadorFrontal() {
    }

    public CargadorFrontal(String codigo, int horasUso, double potencia, double capacidadBalde) {
        super(codigo, horasUso, potencia);
        setCapacidadBalde(capacidadBalde);
    }

    // tambien añadir constructor sobrecargado con la id que hereda de la superclase.
    // generate y despues editar el cuerpo:
    public CargadorFrontal(int id, String codigo, int horasUso, double potencia, double capacidadBalde) {
        // autocompletar desde el this:
        this(codigo,horasUso,potencia,capacidadBalde);
        // escribir:
        setId(id);
        // dejar asi:
        this.capacidadBalde = capacidadBalde;
    }

    public double getCapacidadBalde() {
        return capacidadBalde;
    }

    public void setCapacidadBalde(double capacidadBalde) {
        if (!Double.isFinite(capacidadBalde) || capacidadBalde <= 0) {
            throw new IllegalArgumentException("La capacidad del balde debe ser un número mayor que cero.");
        }
        this.capacidadBalde = capacidadBalde;
    }

    @Override
    public double calcularCostoServicio() {
        if (capacidadBalde > LIMITE_CAPACIDAD) {
            return COSTO_BASE + COSTO_BASE * RECARGO_CAPACIDAD_BALDE;
        }
        return COSTO_BASE;
    }

    // puedes intentar agregar un setter con Generate aca pero no funcionará porque no lo necesitas,
    // asi sabras cual clase lleva el setter nuevo y cual no.

    @Override
    public String getTipo() {
        return "Cargador Frontal";
    }

    // metodo de la superclase que no estoy ocupando:
    @Override
    public String getDetalle() {
        return "Balde " + capacidadBalde + " m³";
    }
}