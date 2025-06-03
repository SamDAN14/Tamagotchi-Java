import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class InterfazUsuario {
    private JFrame frame;
    private Mascota mascota;
    private Juego juego;
    private JLabel lblMascota;
    private JProgressBar barraHambre, barraFelicidad, barraSuciedad, barraEnergia;
    private Timer timerEstado;
    private int nivelAn = 0;

    public InterfazUsuario() {
        juego = new Juego();
        mostrarMenuInicial();
    }

    private void mostrarMenuInicial() {
        Object[] opciones = {"Nueva Partida", "Cargar Partida"};
        UIManager.put("OptionPane.background", new Color(194,202,194));  
        UIManager.put("Panel.background", new Color(194, 202, 194));
        UIManager.put("Button.foreground", new Color(255,255,255));
        UIManager.put("Button.background", new Color(23,80,53));
        UIManager.put("ComboBox.background", new Color(194, 202, 194));   
        UIManager.put("ComboBox.selectionForeground", new Color(255,255,255));      
        UIManager.put("ComboBox.selectionBackground", new Color(23, 80, 53));    

        int eleccion = JOptionPane.showOptionDialog(
            null,
            "¡Bienvenido al Tamagotchi!",
            "Menú Inicial",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        if (eleccion != JOptionPane.CLOSED_OPTION) {
            Musica.ReproEfect("musica/coin-8bit.wav");
        }
        if (eleccion == 0) {
            crearNuevaPartida();
        } else {
            cargarPartida();
        }
    }

    private void crearNuevaPartida() {
        String nombre = JOptionPane.showInputDialog("Nombre de tu mascota:");
        if (nombre != null) {
            Musica.ReproEfect("musica/coin-8bit.wav");
            mascota = new Mascota(nombre);
            crearInterfaz();
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "Tamagotchi";
        }
    }

    private void cargarPartida() {
        Map<String, Mascota> partidas = juego.getPartidas();
        if (partidas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay partidas guardadas. Creando nueva...");
            crearNuevaPartida();
            return;
        }

        String[] nombres = partidas.keySet().toArray(new String[0]);
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            "Selecciona partida:",
            "Cargar Partida",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nombres,
            nombres[0]
        );

        if (seleccion != null) {
            Musica.ReproEfect("musica/coin-8bit.wav");
            mascota = juego.cargarPartida(seleccion);
            if (mascota != null) {
                crearInterfaz();
            } else {
                JOptionPane.showMessageDialog(null, "Error al cargar partida. Creando nueva...");
                crearNuevaPartida();
            }
        } else {
            mostrarMenuInicial();
        }
    }

    private void crearInterfaz() {
        frame = new JFrame("Tamagotchi - " + mascota.getNombre());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 439);
        frame.setLayout(new BorderLayout());
        frame.setBackground(new Color(194,202,194));

        // Panel superior: Medidores
        UIManager.put("ProgressBar.foreground", (new Color(5,40,23)));
        UIManager.put("ProgressBar.background", (new Color(23,80,53)));
        UIManager.put("ProgressBar.selectionForeground", (new Color(255,255,255)));
        UIManager.put("ProgressBar.selectionBackground", (new Color(255,255,255)));
        UIManager.put("Button.foreground", (new Color(255,255,255)));
        UIManager.put("Button.background", (new Color(23,80,53)));

        JPanel panelMedidores = new JPanel(new GridLayout(4, 1));
        barraHambre = crearBarra("Hambre");
        barraFelicidad = crearBarra("Felicidad");
        barraSuciedad = crearBarra("Suciedad");
        barraEnergia = crearBarra("Energia");

        panelMedidores.add(barraHambre);
        panelMedidores.add(barraFelicidad);
        panelMedidores.add(barraSuciedad);
        panelMedidores.add(barraEnergia);

        // Panel central: Imagen de la mascota
        lblMascota = new JLabel(new ImageIcon("sprites/mascota_normal.png"), SwingConstants.CENTER);
        JPanel panelMascota = new JPanel();
        panelMascota.add(lblMascota);

        // Panel inferior: Botones
        JPanel panelBotones = new JPanel(new GridLayout(2, 3));
        agregarBoton(panelBotones, "🍽️", this::alimentar);
        agregarBoton(panelBotones, "🎯", this::entrenar);
        agregarBoton(panelBotones, "🛁", this::bañar);
        agregarBoton(panelBotones, "💤", this::dormir);
        agregarBoton(panelBotones, "💾", this::guardarPartida);
        agregarBoton(panelBotones, "❎", this::salir);

        frame.add(panelMedidores, BorderLayout.NORTH);
        frame.add(panelMascota, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);

        // Timer para actualizar la mascota
        timerEstado = new Timer(3000, e -> {
            mascota.actualizarEstado_Muerte();
            actualizarInterfaz();
            verificarEstadoMascota();
            mascota.subirNivel();
        });
        timerEstado.start();

        actualizarInterfaz();
        frame.setVisible(true);
    }

    private JProgressBar crearBarra(String texto) {
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setString(texto + ": 50");
        barra.setStringPainted(true);
        return barra;
    }

    private void agregarBoton(JPanel panel, String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);
        panel.add(boton);
    }
    
    // Declarar imagenes de las acciones

    private final ImageIcon m_normal = new ImageIcon("sprites/mascota_normal.png");
    private final ImageIcon m_comiendo = new ImageIcon("sprites/mascota_comiendo.png");
    private final ImageIcon m_entrenando = new ImageIcon("sprites/mascota_entrenando.png");
    private final ImageIcon m_bañando = new ImageIcon("sprites/mascota_bañando.png");
    private final ImageIcon m_durmiendo = new ImageIcon("sprites/mascota_durmiendo.png");
    private final ImageIcon m_muerta = new ImageIcon("sprites/mascota_muerta.png");
    private final ImageIcon m_hambrienta = new ImageIcon("sprites/mascota_hambrienta.png");
    private final ImageIcon m_sucia = new ImageIcon("sprites/mascota_sucia.png");
    private final ImageIcon m_cansada = new ImageIcon("sprites/mascota_cansada.png");

    private void actualizarInterfaz() {
        barraHambre.setValue(mascota.getHambre());
        barraHambre.setString("🍕: " + mascota.getHambre());
        barraFelicidad.setValue(mascota.getFelicidad());
        barraFelicidad.setString("😀: " + mascota.getFelicidad());
        barraSuciedad.setValue(mascota.getSuciedad());
        barraSuciedad.setString("🗑️: " + mascota.getSuciedad());
        barraEnergia.setValue(mascota.getEnergia());
        barraEnergia.setString("⚡: " + mascota.getEnergia());
        
        // Cambiar imagen según estado
        if (!mascota.isEstaViva()) {
            lblMascota.setIcon(m_muerta);
            return;
        } 
        if (mascota.getHambre() > 70) {
            lblMascota.setIcon(m_hambrienta);
        } else if (mascota.getSuciedad() > 80) {
            lblMascota.setIcon(m_sucia);
        } else if (mascota.getEnergia() < 30) {
            lblMascota.setIcon(m_cansada);
        } else if (mascota.MasNormal()) {
            lblMascota.setIcon(m_normal);
        }   
    }

    // Cambiar imagen segun la accion

    private void cambiarImTemp(ImageIcon imagenTemporal, int duracionMs, ImageIcon imagenOriginal) {
        lblMascota.setIcon(imagenTemporal);
        Timer timer = new Timer(duracionMs, e -> {
            if (mascota.isEstaViva()) {
                lblMascota.setIcon(imagenOriginal);
            }
            ((Timer)e.getSource()).stop(); // Detener el timer después de ejecutarse
        });
        timer.setRepeats(false); // Para que solo se ejecute una vez
        timer.start();
    }

    private void verificarEstadoMascota() {
        int nivelAc = mascota.getNivel();
        if (!mascota.isEstaViva()) {
            timerEstado.stop();
            Musica.ReproEfect("musica/game-over-8bit.wav");
            JOptionPane.showMessageDialog(frame, "¡" + mascota.getNombre() +" muerto! 😵", "Fin del Juego", JOptionPane.ERROR_MESSAGE);
        } else if (nivelAc > nivelAn) {
            Musica.ReproEfect("musica/level-up-8bit.wav");
            JOptionPane.showMessageDialog(frame, "¡" + mascota.getNombre() +" ha subido al nivel " + nivelAc + "!", "¡Felicidades!", JOptionPane.INFORMATION_MESSAGE);
            nivelAn = nivelAc;
        }
    }

    // Acciones de los botones

    private void alimentar(ActionEvent e) {
        if (!mascota.isEstaViva()) return;
        Musica.ReproEfect("musica/coin-8bit.wav");
        actualizarInterfaz();
        mascota.alimentar();
        actualizarInterfaz();
        cambiarImTemp(m_comiendo, 1000, m_normal);
    }

    private void entrenar(ActionEvent e) {
        if (!mascota.isEstaViva()) return;
        Musica.ReproEfect("musica/coin-8bit.wav");
        actualizarInterfaz();
        mascota.entrenar();
        actualizarInterfaz();
        cambiarImTemp(m_entrenando, 1000, m_normal);
    }

    private void bañar(ActionEvent e) {
        if (!mascota.isEstaViva()) return;
        Musica.ReproEfect("musica/coin-8bit.wav");
        actualizarInterfaz();
        mascota.bañar();
        actualizarInterfaz();
        cambiarImTemp(m_bañando, 1000, m_normal);
    }

    private void dormir(ActionEvent e) {
        if (!mascota.isEstaViva()) return;
        Musica.ReproEfect("musica/coin-8bit.wav");
        actualizarInterfaz();
        mascota.dormir();
        actualizarInterfaz();
        cambiarImTemp(m_durmiendo, 2000, m_normal);
    }

    private void guardarPartida(ActionEvent e) {
        Musica.ReproEfect("musica/coin-8bit.wav");
        String nombrePartida = JOptionPane.showInputDialog(frame, "Nombre para guardar la partida:", mascota.getNombre());
        if (nombrePartida != null && !nombrePartida.trim().isEmpty()) {
            juego.guardarPartida(nombrePartida, mascota);
            JOptionPane.showMessageDialog(frame, "Partida guardada como: " + nombrePartida);
        }
    }

    private void salir(ActionEvent e) {
        timerEstado.stop();
        frame.dispose();
        System.exit(0);
        Musica musica = new Musica();
        musica.DetMusica();
    }

    public static void main(String[] args) {
        Musica musica = new Musica();
        musica.ReproMusica("musica/melody-8bit.wav");
        SwingUtilities.invokeLater(() -> new InterfazUsuario());
    }
}