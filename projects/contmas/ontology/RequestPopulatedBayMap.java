package contmas.ontology;

import jade.content.AgentAction;

/**
 * Protege name: RequestPopulatedBayMap
 * 
 * @author ontology bean generator
 * @version 2010/03/3, 11:16:16
 */
public class RequestPopulatedBayMap implements AgentAction{

	/**
	 * 
	 */
	private static final long serialVersionUID=7002894707977660588L;
	/**
	 * Protege name: populate_on
	 */
	private BayMap populate_on;

	public BayMap getPopulate_on(){
		return this.populate_on;
	}

	public void setPopulate_on(BayMap value){
		this.populate_on=value;
	}

}
