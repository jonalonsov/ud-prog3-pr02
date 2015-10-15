package ud.prog3.pr02;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

/** "Mundo" del juego del coche.
 * Incluye las f�sicas para el movimiento y los choques de objetos.
 * Representa un espacio 2D en el que se mueven el coche y los objetos de puntuaci�n.
 * @author Andoni Egu�luz Mor�n
 * Facultad de Ingenier�a - Universidad de Deusto
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
		
		// 2� ley de Newton: F = m*a ---> a = F/m
		return fuerza/masa;
	}
	
	
	
	
//	Por �ltimo en esta clase MundoJuego, a�ade un m�todo para poder aplicar la fuerza al coche. Observa c�mo
//	si no hay fuerza externa, la �nica fuerza que se aplica es la de rozamiento hasta que el coche se para:
	
	public static void aplicarFuerza( double fuerza, Coche coche ) {
		
	double fuerzaRozamiento = calcFuerzaRozamiento( Coche.MASA, Coche.COEF_RZTO_SUELO, Coche.COEF_RZTO_AIRE, coche.getVelocidad() );
	double aceleracion = calcAceleracionConFuerza( fuerza+fuerzaRozamiento, Coche.MASA );
		
		if (fuerza==0) {
			// No hay fuerza, solo se aplica el rozamiento
			double velAntigua = coche.getVelocidad();
			coche.acelera( aceleracion, 0.04 );
			
			if (velAntigua>=0 && coche.getVelocidad()<0	|| velAntigua<=0 && coche.getVelocidad()>0) {
				
			
				coche.setVelocidad(0); // Si se est� frenando, se para (no anda al rev�s)
			}
		} 
		
		else {
			
			coche.acelera( aceleracion, 0.04 );
		}
	}

	/** Crea un coche nuevo y lo a�ade al mundo y al panel visual
	 * @param posX	Posici�n X de pixel del nuevo coche
	 * @param posY	Posici�n Y de p�xel del nuevo coche
	 */
	public void creaCoche( int posX, int posY ) {
		// Crear y a�adir el coche a la ventana
		miCoche = new CocheJuego();
		miCoche.setPosicion( posX, posY );
		panel.add( miCoche.getGrafico() );  // A�ade al panel visual
		miCoche.getGrafico().repaint();  // Refresca el dibujado del coche
	}
	
	
	
	
	
	/** Si han pasado m�s de 1,2 segundos desde la �ltima,
	* crea una estrella nueva en una posici�n aleatoria y la a�ade al mundo y al panel visual */
	public void creaEstrella(int posX, int posY){
		
		Date ahora = new Date();
		unaEstrella = new Estrella();
		ListaEstrellas.add(unaEstrella);
		unaEstrella.setPosicion( posX, posY );
		unaEstrella.setCreacion(ahora);
		panel.add( unaEstrella.getGrafico() );  // A�ade al panel visual
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
	* se tocan las esferas l�gicas del coche y la estrella. Si es as�, las elimina.
	* @return N�mero de estrellas eliminadas
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
//		//Si entre la fecha actual y la fecha de creaci�n han pasado m�s de maxTiempo(6 segundos)
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
		* se tocan las esferas l�gicas del coche y la estrella. Si es as�, las elimina.
		* @return N�mero de estrellas eliminadas
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

		//Si la posici�n del coche y la posici�n de la estrella es la misma
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

	/** Calcula si hay choque en horizontal con los l�mites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posici�n actual
	 * @return	true si hay choque horizontal, false si no lo hay
	 */
	public boolean hayChoqueHorizontal( CocheJuego coche ) {
		return (coche.getPosX() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosX()>panel.getWidth()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}
	
	/** Calcula si hay choque en vertical con los l�mites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posici�n actual
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
		if (dir < 0) dir = 360+dir;  // Correcci�n para mantenerlo en [0,360)
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
	
	/** Calcula y devuelve la posici�n X de un movimiento
	 * @param vel    	Velocidad del movimiento (en p�xels por segundo)
	 * @param dir    	Direcci�n del movimiento en grados (0� = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoX( double vel, double dir, double tiempo ) {
		return vel * Math.cos(dir/180.0*Math.PI) * tiempo;
	}
	
	/** Calcula y devuelve la posici�n X de un movimiento
	 * @param vel    	Velocidad del movimiento (en p�xels por segundo)
	 * @param dir    	Direcci�n del movimiento en grados (0� = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoY( double vel, double dir, double tiempo ) {
		return vel * -Math.sin(dir/180.0*Math.PI) * tiempo;
		// el negativo es porque en pantalla la Y crece hacia abajo y no hacia arriba
	}
	
	/** Calcula el cambio de velocidad en funci�n de la aceleraci�n
	 * @param vel		Velocidad original
	 * @param acel		Aceleraci�n aplicada (puede ser negativa) en pixels/sg2
	 * @param tiempo	Tiempo transcurrido en segundos
	 * @return	Nueva velocidad
	 */
	public static double calcVelocidadConAceleracion( double vel, double acel, double tiempo ) {
		return vel + (acel*tiempo);
	}
	
}
