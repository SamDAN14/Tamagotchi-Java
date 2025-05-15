import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Juego {
    private Map<String, Mascota> partidas;
    private static final String ARCHIVO = "partidas.dat";

    public Juego() {
        partidas = new HashMap<>();
        cargarPartidas();
    }

    public void guardarPartida(String nombrePartida, Mascota mascota) {
        if (nombrePartida != null && mascota != null) {
            partidas.put(nombrePartida, mascota);
            guardarEnArchivo();
        }
    }

    public Mascota cargarPartida(String nombrePartida) {
        return partidas.get(nombrePartida);
    }

    public Map<String, Mascota> getPartidas() {
        return new HashMap<>(partidas);
    }

    @SuppressWarnings("unchecked")
    private void cargarPartidas() {
        File file = new File(ARCHIVO);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                partidas = (Map<String, Mascota>) obj;
                // Validar todas las mascotas cargadas
                partidas.values().removeIf(m -> m == null || !m.isEstaViva());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar partidas: " + e.getMessage());
            partidas = new HashMap<>();
        }
    }

    private void guardarEnArchivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(partidas);
        } catch (IOException e) {
            System.err.println("Error al guardar partidas: " + e.getMessage());
        }
    }
}