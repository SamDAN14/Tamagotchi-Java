import java.io.Serializable;

public class Mascota implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private int hambre;
    private int felicidad;
    private int suciedad;
    private int energia;
    private int nivel;
    private int nivelMaxiAl; 
    private boolean estaViva;

    public Mascota(String nombre) {
        this.nombre = nombre;
        this.hambre = 50;
        this.felicidad = 50;
        this.suciedad = 50;
        this.energia = 50;
        this.nivel = 1;
        this.nivelMaxiAl = 1;
        this.estaViva = true;
    }

    public boolean MasNormal() {
        return 
            isEstaViva() && getHambre() <= 80 && getSuciedad() <= 80 && getEnergia() >= 20;
    }
    // Métodos de acciones
    public void alimentar() {
        if (estaViva) {
            hambre = Math.max(0, hambre - 30);
            felicidad = Math.min(100, felicidad + 10);
            suciedad = Math.min(100, suciedad + 5);
        }
    }

    public void entrenar() {
        if (estaViva) {
            energia = Math.max(0, energia - 20);
            hambre = Math.min(100, hambre + 15);
            felicidad = Math.min(100, felicidad + 25);
        }
    }

    public void bañar() {
        if (estaViva) {
            suciedad = Math.max(0, suciedad - 40);
            felicidad = Math.min(100, felicidad + 10);
            energia = Math.max(0, energia - 10);
        }
    }

    public void dormir() {
        if (estaViva) {
            energia = Math.min(100, energia + 40);
            hambre = Math.min(100, hambre + 20);
            felicidad = Math.min(100, felicidad + 10);
            suciedad = Math.min(100, suciedad + 5);
        }
    }

    public void actualizarEstado_Muerte() {
        if (!estaViva) return;
        
        hambre = Math.min(100, hambre + 10);
        energia = Math.max(0, energia - 5);
        suciedad = Math.min(100, suciedad + 5);
        felicidad = Math.max(0, felicidad - 15);

        if (hambre >= 80 && energia <= 0 && suciedad >= 80) 
            estaViva = false;
    }

    public void subirNivel() {
        if (hambre <= 20 && felicidad >= 80 && suciedad <= 20){
            nivel++;
            felicidad = 50;
            if (nivel > nivelMaxiAl) {
                nivelMaxiAl = nivel;
            }
        }
    }
    // Getters y Setters
    public String getNombre() { return nombre; }
    public int getHambre() { return hambre; }
    public int getFelicidad() { return felicidad; }
    public int getSuciedad() { return suciedad; }
    public int getEnergia() { return energia; }
    public int getNivel() { return nivel; }
    public int getNivelMaxAL() { return nivelMaxiAl; }
    public boolean isEstaViva() { return estaViva; }
}