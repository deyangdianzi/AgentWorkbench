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
import jade.core.behaviours.CyclicBehaviour;
import contmas.agents.ContainerHolderAgent;
import contmas.ontology.Administered;
import contmas.ontology.TransportOrderChain;

public class unload extends CyclicBehaviour{
	private final ContainerHolderAgent myCAgent;
	private static final long serialVersionUID=3933460156486819068L;

	public unload(Agent a){
		super(a);
		this.myCAgent=((ContainerHolderAgent) this.myAgent);
	}

	@Override
	public void action(){
		TransportOrderChain someTOC=this.myCAgent.getSomeTOCOfState(new Administered());
		if(someTOC != null){
			this.myCAgent.releaseContainer(someTOC,null);
		}else{
			this.block();
		}
	}
}