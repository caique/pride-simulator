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

public class Broadcast extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ArrayList<String> messages;

	protected void setup() {
		AID aid = new AID("broadcaster",AID.ISLOCALNAME);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("broadcaster");
		sd.setName("broadcast");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);

		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}

		if (messages == null) {
			messages = new ArrayList<String>();
		}

		waitformessage();

	}

	private void waitformessage() {

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchLanguage("message"),
						MessageTemplate.MatchLanguage("message"));

				ACLMessage message = myAgent.receive(template);

				if (message != null) {
					messages.add(message.getContent());
				} else {
					block();
				}
			}
		});

	}

	private void waitforreport() {
		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchLanguage("over"));

				ACLMessage over = myAgent.receive(template);

				if (over != null) {
					for (String message : messages) {

						System.out.println(message);
					}

					myAgent.doDelete();
				} else {
					block();
				}
			}
		});

	}

	protected void takeDown() {

		System.out.println("=D");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {

		}
	}

}
