package gasmas.agents.components;

import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class ExitAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 5755894155609484866L;

	@Override
	protected void setup() {
		super.setup();
//		this.addBehaviour(new PassiveNAResponderBehaviour(this));
	}
}
