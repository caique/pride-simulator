package br.unb.sma.simulators.pride.components;

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

	public AgentController hear(String name) throws StaleProxyException {
		return manager.show(name);
	}
}
