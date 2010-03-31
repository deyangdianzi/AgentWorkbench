package contmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: TOCHasState
* @author ontology bean generator
* @version 2010/03/30, 14:14:15
*/
public class TOCHasState implements Concept {

   /**
* Protege name: subjected_toc
   */
   private TransportOrderChain subjected_toc;
   public void setSubjected_toc(TransportOrderChain value) { 
    this.subjected_toc=value;
   }
   public TransportOrderChain getSubjected_toc() {
     return this.subjected_toc;
   }

   /**
* Protege name: state
   */
   private TransportOrderChainState state;
   public void setState(TransportOrderChainState value) { 
    this.state=value;
   }
   public TransportOrderChainState getState() {
     return this.state;
   }

}
