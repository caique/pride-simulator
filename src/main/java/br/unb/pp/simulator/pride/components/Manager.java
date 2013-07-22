package br.unb.pp.simulator.pride.components;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Manager {

	private static Manager me;
	private ContainerController container;

	private Manager(ContainerController container) {
		this.container = container;
	}

	public static Manager areYouThere(ContainerController container) {
		if (me == null) {
			me = new Manager(container);
		}

		return me;
	}

	public AgentController present(String name) throws StaleProxyException {
		Object[] argsForFighter = new Object[1];
		argsForFighter[0] = name;

		AgentController fighter = container.createNewAgent(name,
				"br.unb.pp.simulator.pride.agents.Fighter", argsForFighter);

		fighter.start();

		return fighter;
	}

	public AgentController call(String name) throws StaleProxyException {
		Object[] argsForReferee = new Object[1];
		argsForReferee[0] = name;

		AgentController referee = container.createNewAgent(name,
				"br.unb.pp.simulator.pride.agents.Referee", argsForReferee);

		referee.start();

		return referee;
	}

	public AgentController show(String name) throws StaleProxyException {
		Object[] argsForBroadcaster = new Object[1];
		argsForBroadcaster[0] = name;

		AgentController broadcaster = container.createNewAgent(name,
				"br.unb.pp.simulator.pride.agents.Broadcaster",
				argsForBroadcaster);

		broadcaster.start();

		return broadcaster;
	}

}
