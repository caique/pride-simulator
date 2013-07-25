package br.unb.sma.simulators.pride.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.unb.sma.simulators.pride.messages.Messages;
import br.unb.sma.utils.broadcast.agents.ObservableAgent;

public class Referee extends ObservableAgent {

	private static final long serialVersionUID = 1L;

	private String name;
	private List<AID> fighters;

	@Override
	protected void setup() {
		enterTheOctogon();
		findBroadcast();
		lookToFighters();
		startCombat();
		controlFight();
		attentionForKnockout();
	}

	private void enterTheOctogon() {
		Object[] args = getArguments();

		if (args != null && args.length > 0) {
			this.name = (String) args[0];
			new AID(name, AID.ISLOCALNAME);
		} else {
			System.out.println(Messages.REFEREE_WITHOUT_NAME);
		}

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("referee");
		sd.setName(name);
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}
	}

	private void lookToFighters() {
		DFAgentDescription template = new DFAgentDescription();

		ServiceDescription sd = new ServiceDescription();
		sd.setType("ready-to-fight");
		template.addServices(sd);

		try {
			DFAgentDescription[] fightersFounded = DFService.search(this,
					template);

			if (fighters == null)
				fighters = new ArrayList<AID>();

			for (int i = 0; i < fightersFounded.length; i++) {
				fighters.add(fightersFounded[i].getName());
			}

		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}
	}

	private void startCombat() {
		addBehaviour(new OneShotBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				if (fighters.size() > 1) {
					ACLMessage instruction = new ACLMessage(ACLMessage.INFORM);
					instruction.setLanguage("instruction");
					instruction.setContent(Messages.FIGHT);

					for (AID fighter : fighters) {
						instruction.addReceiver(fighter);
					}

					myAgent.send(instruction);
				} else {
					block();
				}
			}
		});

	}

	private void controlFight() {
		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchLanguage("movement"),
						MessageTemplate.MatchPerformative(ACLMessage.INFORM));

				ACLMessage movement = myAgent.receive(template);

				if (movement != null) {
					if (successufullyStrike()) {
						ACLMessage accepted = movement.createReply();
						accepted.setPerformative(ACLMessage.CONFIRM);
						accepted.setLanguage("strike-accepted");
						accepted.setContent("OK!");
						send(accepted);

						AID striker = movement.getSender();

						List<AID> potentialTargets = new ArrayList<AID>();

						for (AID fighter : fighters) {
							if (!striker.equals(fighter)) {
								potentialTargets.add(fighter);
							}
						}

						Collections.shuffle(potentialTargets);

						ACLMessage strike = new ACLMessage(ACLMessage.INFORM);
						strike.addReceiver(potentialTargets.get(0));
						strike.setLanguage("strike");
						strike.setContent(Messages.PUNCH
								+ striker.getLocalName());
						send(strike);

					} else {
						ACLMessage refused = movement.createReply();
						refused.setPerformative(ACLMessage.DISCONFIRM);
						refused.setLanguage("strike-refused");
						refused.setContent("NOT OK!");
						send(refused);
					}
				}
			}

			private boolean successufullyStrike() {
				if ((new Random().nextInt() % 2) != 0) {
					return true;
				}

				return false;
			}
		});
	}

	private void attentionForKnockout() {
		addBehaviour(new CyclicBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchLanguage("knockout"));

				ACLMessage knockout = myAgent.receive(template);

				if (knockout != null) {
					if (broadcaster == null) {
						System.out.println("Broadcaster off!");
					} else {
						System.out
								.println("Devia ter avisado sobre o knockout!");
					}

					reportThat(knockout.getContent());

					fighters.remove(knockout.getSender());

					if (fighters.size() == 1) {
						reportThat(fighters.get(0).getLocalName() + " won!");
						endCombat();
					} else {
						reportThat(fighters.size()
								+ Messages.FIGHTERS_REMAINING);
					}
				}
			}
		});
	}

	private void endCombat() {
		System.out.println("Devia ter avisado sobre o final da luta!");
		reportThat(Messages.OVER);

		ACLMessage itsOver = new ACLMessage(ACLMessage.INFORM);
		itsOver.setLanguage("over");

		for (AID fighter : fighters) {
			itsOver.addReceiver(fighter);
		}

		send(itsOver);
		doDelete();
	}

	protected void takeDown() {
		reportThat(name + Messages.LEFT_THE_OCTOGON);

		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
		}
	}
}
