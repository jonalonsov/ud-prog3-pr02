package ud.prog3.pr02;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

/** "Mundo" del juego del coche.
 * Incluye las físicas para el movimiento y los choques de objetos.
 * Representa un espacio 2D en el que se mueven el coche y los objetos de puntuación.
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class MundoJuego {
	private JPanel panel;  // panel visual del juego
	CocheJuego miCoche;    // Coche del juego
	Estrella unaEstrella;
	ArrayList<Estrella> ListaEstrellas = new ArrayList<Estrella>();
	int numEstrellasQuitadas=0;
	int numEstrellasComidas=0;
	
	
	
	
	/** Construye un mundo de juego
	 * @param panel	Panel visual del juego
	 */
	public MundoJuego( JPanel panel ) {
		this.panel = panel;
	}
	
	public static double calcFuerzaRozamiento( double masa, double coefRozSuelo, double coefRozAire, double vel ) {
		
			double fuerzaRozamientoAire = coefRozAire * (-vel); // En contra del movimiento
			double fuerzaRozamientoSuelo = masa * coefRozSuelo * ((vel>0)?(-1):1); // Contra mvto
			return fuerzaRozamientoAire + fuerzaRozamientoSuelo;
	
	}
	
	public static double calcAceleracionConFuerza( double fuerza, double masa ) {
		
		// 2ª ley de Newton: F = m*a ---> a = F/m
		return fuerza/masa;
	}
	
	
	
	
//	Por último en esta clase MundoJuego, añade un método para poder aplicar la fuerza al coche. Observa cómo
//	si no hay fuerza externa, la única fuerza que se aplica es la de rozamiento hasta que el coche se para:
	
	public static void aplicarFuerza( double fuerza, Coche coche ) {
		
	double fuerzaRozamiento = calcFuerzaRozamiento( Coche.MASA, Coche.COEF_RZTO_SUELO, Coche.COEF_RZTO_AIRE, coche.getVelocidad() );
	double aceleracion = calcAceleracionConFuerza( fuerza+fuerzaRozamiento, Coche.MASA );
		
		if (fuerza==0) {
			// No hay fuerza, solo se aplica el rozamiento
			double velAntigua = coche.getVelocidad();
			coche.acelera( aceleracion, 0.04 );
			
			if (velAntigua>=0 && coche.getVelocidad()<0	|| velAntigua<=0 && coche.getVelocidad()>0) {
				
			
				coche.setVelocidad(0); // Si se está frenando, se para (no anda al revés)
			}
		} 
		
		else {
			
			coche.acelera( aceleracion, 0.04 );
		}
	}

	/** Crea un coche nuevo y lo añade al mundo y al panel visual
	 * @param posX	Posición X de pixel del nuevo coche
	 * @param posY	Posición Y de píxel del nuevo coche
	 */
	public void creaCoche( int posX, int posY ) {
		// Crear y añadir el coche a la ventana
		miCoche = new CocheJuego();
		miCoche.setPosicion( posX, posY );
		panel.add( miCoche.getGrafico() );  // Añade al panel visual
		miCoche.getGrafico().repaint();  // Refresca el dibujado del coche
	}
	
	
	
	
	
	/** Si han pasado más de 1,2 segundos desde la última,
	* crea una estrella nueva en una posición aleatoria y la añade al mundo y al panel visual */
	public void creaEstrella(int posX, int posY){
		
		Date ahora = new Date();
		unaEstrella = new Estrella();
		ListaEstrellas.add(unaEstrella);
		unaEstrella.setPosicion( posX, posY );
		unaEstrella.setCreacion(ahora);
		panel.add( unaEstrella.getGrafico() );  // Añade al panel visual
		unaEstrella.setPos(posX, posY);
		unaEstrella.getGrafico().repaint();  // Refresca el dibujado de la estrella
		
		
		
		
		
	}
	
	public int quitaYRotaEstrellas( long maxTiempo ){
		
		if(ListaEstrellas.size()>0){
		for(int i=0; i<ListaEstrellas.size(); i++){
			
			Date ahora = new Date();
			long tiempoTranscurrido = ahora.getTime() - ListaEstrellas.get(i).getCreacion().getTime();
			
			if (tiempoTranscurrido > maxTiempo){
			
				ListaEstrellas.remove(i);
				System.out.println("posicion " + i + " eliminado");
				panel.remove(ListaEstrellas.get(i).getGrafico());
				ListaEstrellas.get(i).getGrafico().repaint();
				
				numEstrellasQuitadas ++;
			}else{
				
				ListaEstrellas.get(i).getGrafico().setGiro(10);
				ListaEstrellas.get(i).getGrafico().repaint();
			}
	
			
			
		}
		}
		
		return numEstrellasQuitadas;
	}
	
	/** Calcula si hay choques del coche con alguna estrella (o varias). Se considera el choque si
	* se tocan las esferas lógicas del coche y la estrella. Si es así, las elimina.
	* @return Número de estrellas eliminadas
	*/
	
//	public int choquesConEstrellas(){
//		
//		
//		
//		
//		
//		return numEstrellasComidas;
//		
//	}	
	
//	public int quitaYRotaEstrellas( long maxTiempo ){
//
//		int numEstrellasQuitadas=0;
//
//		for(int i=0 ; i <listaEstrellas.size(); i++){
//
//		EstrellaJuego objetoEstrella= new EstrellaJuego();
//		objetoEstrella= listaEstrellas.get(i);
//
//		//Fecha en el que se ha guardado el objeto de esa estrella
//		Date fecha = objetoEstrella.getAhora();
//		long milliseconds = fecha.getTime();
//
//		//Fecha del momento
//		Date fechaAhora=new Date();
//		long milliseconds2 = fechaAhora.getTime();
//
//		//Si entre la fecha actual y la fecha de creación han pasado más de maxTiempo(6 segundos)
//		if(milliseconds2-milliseconds >=maxTiempo){
//
//		//Borrar la estrella del array
//		listaEstrellas.remove(objetoEstrella);
//
//		//Borrar la estrella del panel
//		panel.remove(objetoEstrella.getGrafico());
//
//		//Actualizar el panel
//		panel.repaint();
//
//		numEstrellasQuitadas ++;
//
//		} else {
//		//Codificar para que gire la estrella
//		//objetoEstrella.setGiro(10);
//
//		}
//		}
//
//		return numEstrellasQuitadas;
//
//		}

		/** Calcula si hay choques del coche con alguna estrella (o varias). Se considera el choque si
		* se tocan las esferas lógicas del coche y la estrella. Si es así, las elimina.
		* @return Número de estrellas eliminadas
		*/
		public int choquesConEstrellas(){
		int numChoques=0;

		for(int i=0 ; i <ListaEstrellas.size(); i++){

		Estrella objetoEstrella= new Estrella();
		objetoEstrella= ListaEstrellas.get(i);

		//Obtener las posiciones del objeto de Estrella
		double posXEstrella = objetoEstrella.getPosX();
		double posYEstrella = objetoEstrella.getPosY();

		//Obtener las posiciones del objeto de Coche
		double posXCoche = miCoche.getPosX();
		double posYCoche = miCoche.getPosY();

		//Si la posición del coche y la posición de la estrella es la misma
		if(posXCoche -posXEstrella >=-35 && posXCoche -posXEstrella <=35 && posYCoche -posYEstrella >=-35 && posYCoche -posYEstrella <=35  ){

		//Borrar la estrella del array
		ListaEstrellas.remove(objetoEstrella);

		//Borrar la estrella del panel
		panel.remove(objetoEstrella.getGrafico());

		//Actualizar el panel
		panel.repaint();

		numChoques ++;

		} 
		}
		return numChoques;
		}

		
		

	
	
	/** Devuelve el coche del mundo
	 * @return	Coche en el mundo. Si no lo hay, devuelve null
	 */
	public CocheJuego getCoche() {
		return miCoche;
	}

	/** Calcula si hay choque en horizontal con los límites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posición actual
	 * @return	true si hay choque horizontal, false si no lo hay
	 */
	public boolean hayChoqueHorizontal( CocheJuego coche ) {
		return (coche.getPosX() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosX()>panel.getWidth()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}
	
	/** Calcula si hay choque en vertical con los límites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posición actual
	 * @return	true si hay choque vertical, false si no lo hay
	 */
	public boolean hayChoqueVertical( CocheJuego coche ) {
		return (coche.getPosY() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosY()>panel.getHeight()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}

	/** Realiza un rebote en horizontal del objeto de juego indicado
	 * @param coche	Objeto que rebota en horizontal
	 */
	public void rebotaHorizontal( CocheJuego coche ) {
		// System.out.println( "Choca X");
		double dir = coche.getDireccionActual();
		dir = 180-dir;   // Rebote espejo sobre OY (complementario de 180)
		if (dir < 0) dir = 360+dir;  // Corrección para mantenerlo en [0,360)
		coche.setDireccionActual( dir );
	}
	
	/** Realiza un rebote en vertical del objeto de juego indicado
	 * @param coche	Objeto que rebota en vertical
	 */
	public void rebotaVertical( CocheJuego coche ) {
		// System.out.println( "Choca Y");
		double dir = miCoche.getDireccionActual();
		dir = 360 - dir;  // Rebote espejo sobre OX (complementario de 360)
		miCoche.setDireccionActual( dir );
	}
	
	/** Calcula y devuelve la posición X de un movimiento
	 * @param vel    	Velocidad del movimiento (en píxels por segundo)
	 * @param dir    	Dirección del movimiento en grados (0º = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoX( double vel, double dir, double tiempo ) {
		return vel * Math.cos(dir/180.0*Math.PI) * tiempo;
	}
	
	/** Calcula y devuelve la posición X de un movimiento
	 * @param vel    	Velocidad del movimiento (en píxels por segundo)
	 * @param dir    	Dirección del movimiento en grados (0º = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoY( double vel, double dir, double tiempo ) {
		return vel * -Math.sin(dir/180.0*Math.PI) * tiempo;
		// el negativo es porque en pantalla la Y crece hacia abajo y no hacia arriba
	}
	
	/** Calcula el cambio de velocidad en función de la aceleración
	 * @param vel		Velocidad original
	 * @param acel		Aceleración aplicada (puede ser negativa) en pixels/sg2
	 * @param tiempo	Tiempo transcurrido en segundos
	 * @return	Nueva velocidad
	 */
	public static double calcVelocidadConAceleracion( double vel, double acel, double tiempo ) {
		return vel + (acel*tiempo);
	}
	
}
