package gasmas.ontology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ResistanceParameters
* @author ontology bean generator
* @version 2011/03/18, 20:48:41
*/
public class ResistanceParameters implements Concept {

   /**
* Protege name: costs
   */
   private float costs;
   public void setCosts(float value) { 
    this.costs=value;
   }
   public float getCosts() {
     return this.costs;
   }

   /**
* Protege name: pipeFriction
   */
   private float pipeFriction;
   public void setPipeFriction(float value) { 
    this.pipeFriction=value;
   }
   public float getPipeFriction() {
     return this.pipeFriction;
   }

}
