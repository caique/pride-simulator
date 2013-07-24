package br.unb.pp.simulator.pride.components;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Octagon {

	public static final int HITS = 3;

	private Runtime jade;
	private ContainerController container;
	private Manager manager;

	public Octagon() {
		this.jade = Runtime.instance();
		this.container = jade.createMainContainer(new ProfileImpl());

		this.manager = Manager.areYouThere(container);
	}

	public AgentController sees(String name) throws StaleProxyException {
		return manager.present(name);
	}

	public AgentController assumes(String name) throws StaleProxyException {
		return manager.call(name);
	}

	public void hear(String name) throws StaleProxyException {
		manager.show(name);
	}

	public void end() {
		end();
	}
}
