package br.unb.pp.simulator.pride.messages;

public class Messages {

	/**
	 * Agents paths
	 */
	public static final String FIGHTER_CLASS = "br.unb.pp.simulator.pride.agents.Fighter";
	public static final String REFEREE_CLASS = "br.unb.pp.simulator.pride.agents.Referee";

	/**
	 * Error messages
	 */
	public static final String FIGHTER_WITHOUT_NAME = "This fighter doesn't have a name!";
	public static final String REFEREE_WITHOUT_NAME = "This referee doesn't have a name!";
	public static final String COULDNT_ENTER = " couldn't enter the octagon!";
	public static final String REFEREE_NOT_FOUND = "This fight cannot continue without a referee!";
	public static final String FINISH_FIGHT = "This fight was interrupted!";

	/**
	 * Strike messages
	 */
	public static final String PUNCH = " was punched by ";
	public static final String HIT_PUNCH = " hit the opponent's face!";
	public static final String MISS_PUNCH = " miss!";

	/**
	 * Event messages
	 */
	public static final String ENTER_THE_OCTOGON = " enter the octagon!";
	public static final String LEFT_THE_OCTOGON = " left the octagon!";
	public static final String LOOK_THE_REFEREE = " look the referee ";
	public static final String FIGHT = "Fight!";
	public static final String OVER = "It's over!";
	public static final String HEAR_INSTRUCTION = " hear the instruction of the referee: ";
	public static final String FIGHTERS_REMAINING = " fighters in the octagon!";

	private Messages() {
	}
}
