package agentgui.physical2Denvironment.ontology;

import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
   * This class represents active objects, i.e. environment objects modeling agents. ActiveObjects can move on their own.
* Protege name: ActiveObject
* @author ontology bean generator
* @version 2010/11/17, 20:10:06
*/
public class ActiveObject extends Physical2DObject{ 

   /**
   * The active object's maximum speed
* Protege name: maxSpeed
   */
   private float maxSpeed;
   public void setMaxSpeed(float value) { 
    this.maxSpeed=value;
   }
   public float getMaxSpeed() {
     return this.maxSpeed;
   }

   /**
   * Passive objects currently controlled by this active object
* Protege name: payload
   */
   private List payload = new ArrayList();
   public void addPayload(PassiveObject elem) { 
     List oldList = this.payload;
     payload.add(elem);
   }
   public boolean removePayload(PassiveObject elem) {
     List oldList = this.payload;
     boolean result = payload.remove(elem);
     return result;
   }
   public void clearAllPayload() {
     List oldList = this.payload;
     payload.clear();
   }
   public Iterator getAllPayload() {return payload.iterator(); }
   public List getPayload() {return payload; }
   public void setPayload(List l) {payload = l; }

   /**
* Protege name: agentClassName
   */
   private String agentClassName;
   public void setAgentClassName(String value) { 
    this.agentClassName=value;
   }
   public String getAgentClassName() {
     return this.agentClassName;
   }

   /**
   * The ActiveObject's current movement.
* Protege name: movement
   */
   private Movement movement;
   public void setMovement(Movement value) { 
    this.movement=value;
   }
   public Movement getMovement() {
     return this.movement;
   }

}