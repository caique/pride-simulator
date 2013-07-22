package br.unb.pp.simulator.pride.simulator;

import jade.wrapper.StaleProxyException;
import br.unb.pp.simulator.pride.components.Octagon;

public class SimpleSimulator {

	// Just run as Java class and watch the console
	public static void main(String[] args) throws StaleProxyException {
		Octagon octagon = new Octagon();

		System.out.println("Regular Fight ------------------------");

		octagon.hear("Milton Leite");
		octagon.sees("Anderson Silva");
		octagon.sees("Vitor Belfort");
		octagon.assumes("Herb Dean");
	}
}
