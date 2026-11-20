package cl.duoc.maquinaria.model;

/**
 * Representa a cualquier máquina que arrienda la empresa.
 * Define los datos comunes y delega en cada tipo de maquinaria el cálculo del
 * costo del servicio.
 */
public abstract class Maquina {

    // agregar atributo id que es nuevo; le sirve a sql para saber qué filas editar:
    private int id;
    private String codigo;
    private int horasUso;
    private double potencia;

    /**
     * Constructor sin parámetros, requerido para reconstruir objetos.
     */
    protected Maquina() {
    }

    // constructor inicial ya dado:
    public Maquina(String codigo, int horasUso, double potencia) {
        setCodigo(codigo);
        setHorasUso(horasUso);
        setPotencia(potencia);
    }

    // segundo constructor sobrecargado para cuando realmente el programa necesite construir con id, ya que el usuario no
    // construye con id y por eso se necesita tambien el primer constructor sin id.
    // puedo generar los parametros con Generate pero el cuerpo lo debo modificar:
    public Maquina(int id, String codigo, int horasUso, double potencia) {
        this(codigo,horasUso,potencia);
        setId(id);
    }

    // getter y setter para id nuevos con Generate:
    public int getId() {
        return id;
    }
    // pero añade la validacion en el cuerpo del setter, usa como ejemplo el setHorasUso:
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero");
        }
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede ser nulo ni vacío.");
        }
        this.codigo = codigo.trim();
    }

    public int getHorasUso() {
        return horasUso;
    }

    public void setHorasUso(int horasUso) {
        if (horasUso < 0 || horasUso > 20000) {
            throw new IllegalArgumentException("Las horas de uso deben estar entre 0 y 20000.");
        }
        this.horasUso = horasUso;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        if (!Double.isFinite(potencia) || potencia <= 0) {
            throw new IllegalArgumentException("La potencia debe ser mayor que cero.");
        }
        this.potencia = potencia;
    }

    /**
     * Calcula el costo del servicio según las reglas propias de cada tipo de maquinaria.
     *
     * @return costo del servicio en pesos
     */
    public abstract double calcularCostoServicio();

    /**
     * NUEVO: Permite mostrar el costo calculado en una tabla JavaFX mediante PropertyValueFactory.
     */
    public double getCostoServicio() {
        return calcularCostoServicio();
    }

    /**
     * Retorna el nombre del tipo de maquinaria (por ejemplo, "Excavadora" o "Cargador Frontal").
     *
     * @return tipo de maquinaria
     */
    public abstract String getTipo();

    // este metodo abstracto no estaba, pero parece que no se necesita realmente:
    public abstract String getDetalle();

    @Override
    public String toString() {
        return "Código: " + codigo + " | Horas de uso: " + horasUso;
    }
}