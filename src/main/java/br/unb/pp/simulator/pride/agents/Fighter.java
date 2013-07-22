package br.unb.pp.simulator.pride.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import br.unb.pp.simulator.pride.components.Octagon;
import br.unb.pp.simulator.pride.messages.Messages;

public class Fighter extends Agent {

	private static final long serialVersionUID = 1L;

	private String name;
	protected AID referee;
	protected AID broadcaster;
	protected int hits;

	@Override
	protected void setup() {
		enterTheOctogon();
		waveToTheAudience();
		beReady();
		fight();
	}

	private void enterTheOctogon() {
		Object[] args = getArguments();

		if (args != null && args.length > 0) {
			this.name = (String) args[0];
			new AID(name, AID.ISLOCALNAME);
		} else {
			System.out.println(Messages.FIGHTER_WITHOUT_NAME);
		}

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());

		ServiceDescription sd = new ServiceDescription();
		sd.setType("ready-to-fight");
		sd.setName(name);
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
			reportThat(name + Messages.ENTER_THE_OCTOGON);

		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}

		this.hits = Octagon.HITS;
	}

	private void waveToTheAudience() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("broadcast");
		template.addServices(sd);

		try {
			DFAgentDescription[] broadcastersFounded = DFService.search(this,
					template);

			if (broadcastersFounded.length > 0) {
				broadcaster = broadcastersFounded[0].getName();
			}

			if (broadcaster != null) {
				reportThat(name + Messages.LOOK_THE_AUDIENCE + "!");
			} else {
				System.out.println(Messages.BROADCASTER_NOT_FOUND);
			}
		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}
	}

	private void beReady() {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("referee");
		template.addServices(sd);

		try {
			DFAgentDescription[] refereesFounded = DFService.search(this,
					template);

			if (refereesFounded.length > 0) {
				referee = refereesFounded[0].getName();
			}

			if (referee != null) {
				reportThat(name + Messages.LOOK_THE_REFEREE
						+ referee.getLocalName() + "!");
			} else {
				reportThat(Messages.REFEREE_NOT_FOUND);
			}

		} catch (FIPAException e) {
			System.out.println(Messages.FINISH_FIGHT);
		}

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchLanguage("instruction"),
						MessageTemplate.MatchContent("Fight!"));

				ACLMessage instruction = myAgent.receive(template);

				if (instruction != null) {
					reportThat(name + Messages.HEAR_INSTRUCTION
							+ instruction.getContent());
				} else {
					block();
				}
			}
		});
	}

	private void fight() {
		addBehaviour(new TickerBehaviour(this, 500) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				ACLMessage movement = new ACLMessage(ACLMessage.INFORM);
				movement.addReceiver(referee);
				movement.setLanguage("movement");
				movement.setContent("Punch!");
				send(movement);
			}
		});

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.INFORM),
						MessageTemplate.MatchLanguage("strike"));

				ACLMessage strike = myAgent.receive(template);

				if (strike != null) {
					hits = hits - 1;
					reportThat(name + strike.getContent());

					if (hits > 0) {
						reportThat(name + " can handle " + hits
								+ " more strikes!");
					} else {
						ACLMessage knockout = new ACLMessage(ACLMessage.INFORM);
						knockout.addReceiver(referee);
						knockout.setLanguage("knockout");
						knockout.setContent(name + " fall! It's a knockout!");
						myAgent.send(knockout);
						doDelete();
					}
				}
			}
		});

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(
						MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
						MessageTemplate.MatchLanguage("strike-accepted"));

				ACLMessage strikeAccepted = myAgent.receive(template);

				if (strikeAccepted != null) {
					reportThat(name + Messages.HIT_PUNCH);
				}
			}
		});

		addBehaviour(new CyclicBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				MessageTemplate template = MessageTemplate.and(MessageTemplate
						.MatchPerformative(ACLMessage.DISCONFIRM),
						MessageTemplate.MatchLanguage("strike-refused"));

				ACLMessage strikeRefused = myAgent.receive(template);

				if (strikeRefused != null) {
					reportThat(name + Messages.MISS_PUNCH);
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

				ACLMessage itsOver = myAgent.receive(template);

				if (itsOver != null) {
					doDelete();
				}
			}
		});
	}

	protected void reportThat(final String content) {
		addBehaviour(new OneShotBehaviour(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage report = new ACLMessage(ACLMessage.INFORM);
				report.addReceiver(broadcaster);
				report.setLanguage("report");
				report.setContent(content);
				myAgent.send(report);
			}
		});
	}

	protected void takeDown() {
		reportThat(name + Messages.LEFT_THE_OCTOGON);

		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
		}
	}

}
