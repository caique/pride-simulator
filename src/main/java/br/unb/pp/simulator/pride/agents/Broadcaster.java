package br.unb.pp.simulator.pride.agents;

import java.util.ArrayList;

import br.unb.pp.simulator.pride.messages.Messages;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Broadcaster extends Agent {

	private static final long serialVersionUID = 1L;

	private String name;
	protected ArrayList<String> events;

	protected void setup() {
		Object[] args = getArguments();

		if (args != null && args.length > 0) {
			this.name = (String) args[0];
			new AID(name, AID.ISLOCALNAME);
		} else {
			System.out.println(Messages.BROADCASTER_WITHOUT_NAME);
		}

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("broadcast");
		sd.setName("broadcaster");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);

			if (events == null) {
				events = new ArrayList<String>();
			}
		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}

		waitForReport();
	}

	private void waitForReport() {

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchLanguage("report"));

				ACLMessage report = myAgent.receive(template);

				if (report != null) {
					events.add(report.getContent());
				} else {
					// block();
				}
			}
		});

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchLanguage("over"));

				ACLMessage over = myAgent.receive(template);

				if (over != null) {
					for (String message : events) {
						System.out.println(message);
					}

					myAgent.doDelete();
				}
			}
		});
	}

	protected void takeDown() {
		System.out.println("Everybody " + Messages.LEFT_THE_OCTOGON + "!");

		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
		}
	}

}
