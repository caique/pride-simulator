package br.unb.pp.simulator.pride.simulator;

import jade.wrapper.StaleProxyException;
import br.unb.pp.simulator.pride.components.Octagon;

public class TenFightersSimulator {

	// Just run as Java class and watch the console
	public static void main(String[] args) throws StaleProxyException {
		Octagon octagon = new Octagon();

		System.out.println("Ten Fighters Fight ------------------------");

		octagon.hear("Milton Leite");
		octagon.sees("José Aldo");
		octagon.sees("Anderson Silva");
		octagon.sees("Vitor Belfort");
		octagon.sees("Junior Cigano");
		octagon.sees("Cezar Mutante");
		octagon.sees("Serginho");
		octagon.sees("Lyoto Machida");
		octagon.sees("Antonio Minotauro");
		octagon.sees("Rogerio Minotouro");
		octagon.sees("Wanderlei Silva");
		octagon.assumes("Herb Dean");
		octagon.assumes("Mario Yamasaki");
	}

}
