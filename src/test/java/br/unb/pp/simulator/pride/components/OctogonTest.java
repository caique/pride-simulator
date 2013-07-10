package br.unb.pp.simulator.pride.components;

import jade.wrapper.StaleProxyException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OctogonTest {

	private Octagon octagon;

	@Before
	public void setUp() {
		this.octagon = new Octagon();
	}

	@Test
	public void simulateFight() throws StaleProxyException {
		octagon.sees("WanderleiSilva");
		octagon.sees("Vitor Belfort");
		octagon.assumes("Herb Dean");
	}

	@Test
	public void simulateFightWithThreeFighters() throws StaleProxyException {
		octagon.sees("Wanderlei Silva");
		octagon.sees("Vitor Belfort");
		octagon.sees("Anderson Silva");
		octagon.assumes("Mario Yamasaki");
	}

	@Test
	public void simulateFightWithTenFighters() throws StaleProxyException {
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
	}

	@After
	public void tearDown() {
		this.octagon = null;
	}
}
