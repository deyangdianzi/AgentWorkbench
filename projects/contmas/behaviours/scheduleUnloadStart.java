/**
 * @author Hanno - Felix Wagner, 28.03.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.TransportOrderOfferer;
import contmas.ontology.Administered;

public class scheduleUnloadStart extends SimpleBehaviour{
	/**
	 * 
	 */
	Boolean done=false;

	public scheduleUnloadStart(Agent a){
		super(a);
	}

	@Override
	public void action(){
		if( ((ContainerHolderAgent) myAgent).getSomeTOCOfState(new Administered())!=null && !((ContainerHolderAgent) myAgent).determineContractors().isEmpty()){
			((TransportOrderOfferer) myAgent).offerTransportOrder();
			done=true;
		}
	}

	@Override
	public boolean done(){
		return done;
	}
}