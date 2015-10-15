package ud.prog3.pr02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

/** Clase principal de minijuego de coche para Práctica 02 - Prog III
 * Ventana del minijuego.
 * @author Andoni Eguíluz
 * Facultad de Ingeniería - Universidad de Deusto (2014)
 */
public class VentanaJuego extends JFrame {
	private static final long serialVersionUID = 1L;  // Para serialización
	JPanel pPrincipal;         // Panel del juego (layout nulo)
	MundoJuego miMundo;        // Mundo del juego
	CocheJuego miCoche;        // Coche del juego
	MiRunnable miHilo = null;  // Hilo del bucle principal de juego	
	boolean[] arrayBoolean;
	JLabel numEstrellasCogidas;
	JLabel numEstrellasFalladas;
	int numFallidos=0;
	int numCogidos=0;

	/** Constructor de la ventana de juego. Crea y devuelve la ventana inicializada
	 * sin coches dentro
	 */
	public VentanaJuego() {
		// Liberación de la ventana por defecto al cerrar
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Creación contenedores y componentes
		pPrincipal = new JPanel();
		JPanel pMensajera = new JPanel();
		numEstrellasCogidas = new JLabel();
		numEstrellasFalladas = new JLabel();
		arrayBoolean = new boolean [4];
		
		
		// Formato y layouts
		pPrincipal.setLayout( null );
		pPrincipal.setBackground( Color.white );
		// Añadido de componentes a contenedores
		add( pPrincipal, BorderLayout.CENTER );
		pMensajera.add(numEstrellasCogidas);
		pMensajera.add(numEstrellasFalladas);
		add( pMensajera, BorderLayout.SOUTH );
		// Formato de ventana
		setSize( 1000, 750 );
		setResizable( false );
		// Escuchadores de botones
		
				
		// Añadido para que también se gestione por teclado con el KeyListener
		pPrincipal.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: {
						
						arrayBoolean[0]=true;
						
						//miCoche.acelera( +5, 1 );
						break;
					}
					case KeyEvent.VK_DOWN: {
						
						arrayBoolean[1]=true;
						
						//miCoche.acelera( -5, 1 );
						break;
					}
					case KeyEvent.VK_LEFT: {
						
						arrayBoolean[2]=true;
						
						//miCoche.gira( +10 );
						break;
					}
					case KeyEvent.VK_RIGHT: {
						
						arrayBoolean[3]=true;
						
						//miCoche.gira( -10 );
						break;
					}
				}
			}
			
			public void keyReleased(KeyEvent e) {
				
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: {
						
						arrayBoolean[0]=false;
						break;
					}
					case KeyEvent.VK_DOWN: {
						
						arrayBoolean[1]=false;
						break;
					}
					case KeyEvent.VK_LEFT: {
						
						arrayBoolean[2]=false;
						break;
					}
					case KeyEvent.VK_RIGHT: {
						
						arrayBoolean[3]=false;
						break;
					}
				}
			}
		});
		pPrincipal.setFocusable(true);
		pPrincipal.requestFocus();
		pPrincipal.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				pPrincipal.requestFocus();
			}
		});
		// Cierre del hilo al cierre de la ventana
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (miHilo!=null) miHilo.acaba();
			}
		});
	}
	
	/** Programa principal de la ventana de juego
	 * @param args
	 */
	public static void main(String[] args) {
		// Crea y visibiliza la ventana con el coche
		try {
			final VentanaJuego miVentana = new VentanaJuego();
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					miVentana.setVisible( true );
				}
			});
			miVentana.miMundo = new MundoJuego( miVentana.pPrincipal );
			miVentana.miMundo.creaCoche( 150, 100 );
			miVentana.miCoche = miVentana.miMundo.getCoche();
			miVentana.miCoche.setPiloto( "Fernando Alonso" );
			// Crea el hilo de movimiento del coche y lo lanza
			miVentana.miHilo = miVentana.new MiRunnable();  // Sintaxis de new para clase interna
			Thread nuevoHilo = new Thread( miVentana.miHilo );
			nuevoHilo.start();
		} catch (Exception e) {
			System.exit(1);  // Error anormal
		}
	}
	
	/** Clase interna para implementación de bucle principal del juego como un hilo
	 * @author Andoni Eguíluz
	 * Facultad de Ingeniería - Universidad de Deusto (2014)
	 */
	
	
	class MiRunnable implements Runnable {
		boolean sigo = true;
		double segundos;
		@Override
		public void run() {
			// Bucle principal forever hasta que se pare el juego...
			while (sigo) {
				// Mover coche
				
				
				
				if(numFallidos==10){
					sigo=false;
					//custom title, custom icon
					JOptionPane.showMessageDialog(
					    pPrincipal, "GAME OVER",
					    "Has fallado 10 estrellas",
					    JOptionPane.INFORMATION_MESSAGE);
					pPrincipal.setVisible(false);
										
				}
				
				
				if (segundos>=1.2){
					Random r = new Random();
					miMundo.creaEstrella(r.nextInt(1000), r.nextInt(750));
					//miMundo.quitaYRotaEstrellas(6000);
					segundos=0.0;
				} else{
					
					segundos = segundos + 0.040;
				}				
				
				double fuerzaRoz= miMundo.calcFuerzaRozamiento(miCoche.getMASA(), miCoche.getCOEF_RZTO_SUELO(), miCoche.getCOEF_RZTO_AIRE(), miCoche.getVelocidad());
				
				if (arrayBoolean[0]==true){
					
					
					miMundo.calcAceleracionConFuerza(miCoche.fuerzaAceleracionAdelante(), miCoche.getMASA());
					miMundo.aplicarFuerza(miCoche.fuerzaAceleracionAdelante(), miCoche);
					
					
					
					//miCoche.acelera( +5, 1 );
				}
				
				if (arrayBoolean[0]==false){
					
					
					//miMundo.calcAceleracionConFuerza(miCoche.fuerzaAceleracionAdelante(), miCoche.getMASA());
					miMundo.aplicarFuerza(fuerzaRoz, miCoche);
					
				}

				
				
				if (arrayBoolean[1]==true){
					
					miMundo.calcAceleracionConFuerza(-miCoche.fuerzaAceleracionAtras(), miCoche.getMASA());
					miMundo.aplicarFuerza(-miCoche.fuerzaAceleracionAtras(), miCoche);
					
					//miCoche.acelera( -5, 1 );
				}
				
				if (arrayBoolean[1]==false){
					
					//miMundo.calcAceleracionConFuerza(-miCoche.fuerzaAceleracionAtras(), miCoche.getMASA());
					miMundo.aplicarFuerza(fuerzaRoz, miCoche);
					
				}
				if (arrayBoolean[2]==true){
					miCoche.gira( +10 );
				}
				if (arrayBoolean[3]==true){
					miCoche.gira( -10 );
				}

				
				numFallidos=miMundo.quitaYRotaEstrellas(6000);
				numCogidos = miMundo.choquesConEstrellas();
				numEstrellasFalladas.setText(numEstrellasFalladas.getText() + "FALLOS = " + numFallidos );
				numEstrellasCogidas.setText(numEstrellasCogidas.getText() + "PUNTUACION = " + numFallidos );
				
				miCoche.mueve( 0.040 );
				// Chequear choques			
				// (se comprueba tanto X como Y porque podría a la vez chocar en las dos direcciones (esquinas)
				if (miMundo.hayChoqueHorizontal(miCoche)) // Espejo horizontal si choca en X
					miMundo.rebotaHorizontal(miCoche);
				if (miMundo.hayChoqueVertical(miCoche)) // Espejo vertical si choca en Y
					miMundo.rebotaVertical(miCoche);
				// Dormir el hilo 40 milisegundos
				
				
				
				try {
					Thread.sleep( 40 );
				} catch (Exception e) {
				}
				
				
			}
			
		}
		/** Ordena al hilo detenerse en cuanto sea posible
		 */
		public void acaba() {
			sigo = false;
		}
		
	};
	
	
	
}
