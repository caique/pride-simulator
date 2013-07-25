package br.unb.sma.simulators.pride.components;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import br.unb.sma.simulators.pride.messages.Messages;

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
				Messages.FIGHTER_CLASS, argsForFighter);

		fighter.start();

		return fighter;
	}

	public AgentController call(String name) throws StaleProxyException {
		Object[] argsForReferee = new Object[1];
		argsForReferee[0] = name;

		AgentController referee = container.createNewAgent(name,
				Messages.REFEREE_CLASS, argsForReferee);

		referee.start();

		return referee;
	}

	public AgentController show(String name) throws StaleProxyException {
		Object[] argsForBroadcaster = new Object[1];
		argsForBroadcaster[0] = name;

		/*
		 * Broadcaster was initialized from the class contained in the
		 * dependence Broadcast
		 */
		AgentController broadcaster = container.createNewAgent(name,
				Messages.BROADCASTER_CLASS, argsForBroadcaster);

		broadcaster.start();

		return broadcaster;
	}
}
