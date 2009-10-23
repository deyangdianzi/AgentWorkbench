// file: ContainerTerminalOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package mas.projects.contmas.ontology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: ContainerTerminalOntology.java
 * @author ontology bean generator
 * @version 2009/10/20, 22:25:24
 */
public class ContainerTerminalOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "ContainerTerminal";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new ContainerTerminalOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String ASSIGNHARBORQUAY_ASSIGNED_QUAY="assigned_quay";
    public static final String ASSIGNHARBORQUAY="AssignHarborQuay";
    public static final String ENROLLATHARBOR_SHIP_LENGTH="ship_length";
    public static final String ENROLLATHARBOR="EnrollAtHarbor";
    public static final String REJECTLOADOFFER_LOAD_OFFER="load_offer";
    public static final String REJECTLOADOFFER="RejectLoadOffer";
    public static final String ACCEPTLOADOFFER_LOAD_OFFER="load_offer";
    public static final String ACCEPTLOADOFFER="AcceptLoadOffer";
    public static final String ASKFORDESTINATION_TO_BE_STORED="to_be_stored";
    public static final String ASKFORDESTINATION="AskForDestination";
    public static final String ANNOUNCELOADSTATUS_LOAD_STATUS="load_status";
    public static final String ANNOUNCELOADSTATUS_LOAD_OFFER="load_offer";
    public static final String ANNOUNCELOADSTATUS="AnnounceLoadStatus";
    public static final String PROVIDECRANELIST_AVAILABLE_CRANES="available_cranes";
    public static final String PROVIDECRANELIST="ProvideCraneList";
    public static final String GETCRANELIST_REQUIRED_TURNOVER_CAPACITY="required_turnover_capacity";
    public static final String GETCRANELIST_ASSIGNED_QUAY="assigned_quay";
    public static final String GETCRANELIST="GetCraneList";
    public static final String PROPOSELOADOFFER_LOAD_OFFER="load_offer";
    public static final String PROPOSELOADOFFER="ProposeLoadOffer";
    public static final String CALLFORPROPOSALSONLOADSTAGE_REQUIRED_TURNOVER_CAPACITY="required_turnover_capacity";
    public static final String CALLFORPROPOSALSONLOADSTAGE="CallForProposalsOnLoadStage";
    public static final String PROVIDEBAYMAP_PROVIDES="provides";
    public static final String PROVIDEBAYMAP="ProvideBayMap";
    public static final String REQUESTPOPULATEDBAYMAP_POPULATE_ON="populate_on";
    public static final String REQUESTPOPULATEDBAYMAP="RequestPopulatedBayMap";
    public static final String PROVIDEPOPULATEDBAYMAP_PROVIDES="provides";
    public static final String PROVIDEPOPULATEDBAYMAP="ProvidePopulatedBayMap";
    public static final String ASSIGNBLOCKADDRESS_STORE_IN="store_in";
    public static final String ASSIGNBLOCKADDRESS="AssignBlockAddress";
    public static final String LOOKFORCONTAINER_SEARCHED_AFTER="searched_after";
    public static final String LOOKFORCONTAINER="LookForContainer";
    public static final String INDICATECONTAINER_FOUND="found";
    public static final String INDICATECONTAINER="IndicateContainer";
    public static final String REQUESTRANDOMBAYMAP_Y_DIMENSION="y_dimension";
    public static final String REQUESTRANDOMBAYMAP_X_DIMENSION="x_dimension";
    public static final String REQUESTRANDOMBAYMAP_Z_DIMENSION="z_dimension";
    public static final String REQUESTRANDOMBAYMAP="RequestRandomBayMap";
    public static final String CONTAINER_LENGTH="length";
    public static final String CONTAINER_ID="id";
    public static final String CONTAINER_HEIGHT="height";
    public static final String CONTAINER_IS_COOLABLE="is_coolable";
    public static final String CONTAINER_WIDTH="width";
    public static final String CONTAINER_WEIGHT="weight";
    public static final String CONTAINER_CARRIES_DANGEROUS_GOODS="carries_dangerous_goods";
    public static final String CONTAINER="Container";
    public static final String LAND="Land";
    public static final String SEA="Sea";
    public static final String RMG="RMG";
    public static final String BAYMAP_Y_DIMENSION="y_dimension";
    public static final String BAYMAP_X_DIMENSION="x_dimension";
    public static final String BAYMAP_Z_DIMENSION="z_dimension";
    public static final String BAYMAP_IS_FILLED_WITH="is_filled_with";
    public static final String BAYMAP="BayMap";
    public static final String CRANE="Crane";
    public static final String STREET="Street";
    public static final String REACHSTACKER="ReachStacker";
    public static final String DOMAIN_LIES_IN="lies_in";
    public static final String DOMAIN_HAS_SUBDOMAINS="has_subdomains";
    public static final String DOMAIN="Domain";
    public static final String TRANSPORTORDERCHAIN_TRANSPORTS="transports";
    public static final String TRANSPORTORDERCHAIN_TERMINATES_AT="terminates_at";
    public static final String TRANSPORTORDERCHAIN_IS_LINKED_BY="is_linked_by";
    public static final String TRANSPORTORDERCHAIN="TransportOrderChain";
    public static final String BLOCKADDRESS_LOCATES="locates";
    public static final String BLOCKADDRESS_Y_DIMENSION="y_dimension";
    public static final String BLOCKADDRESS_X_DIMENSION="x_dimension";
    public static final String BLOCKADDRESS_Z_DIMENSION="z_dimension";
    public static final String BLOCKADDRESS="BlockAddress";
    public static final String DESIGNATOR_CONCRETE_DESIGNATION="concrete_designation";
    public static final String DESIGNATOR_TYPE="type";
    public static final String DESIGNATOR_ABSTRACT_DESIGNATION="abstract_designation";
    public static final String DESIGNATOR="Designator";
    public static final String CONTAINERHOLDER_CONTAINS="contains";
    public static final String CONTAINERHOLDER_LIVES_IN="lives_in";
    public static final String CONTAINERHOLDER_ADMINISTERS="administers";
    public static final String CONTAINERHOLDER="ContainerHolder";
    public static final String DOMAINAREA_TO="to";
    public static final String DOMAINAREA_FROM="from";
    public static final String DOMAINAREA_PART_OF="part_of";
    public static final String DOMAINAREA="DomainArea";
    public static final String TWENTYFOOTCONTAINER="TwentyFootContainer";
    public static final String TRANSPORTORDER_ENDS_AT="ends_at";
    public static final String TRANSPORTORDER_STARTS_AT="starts_at";
    public static final String TRANSPORTORDER_TAKES="takes";
    public static final String TRANSPORTORDER="TransportOrder";
    public static final String RAIL="Rail";
    public static final String SHIP_LENGTH="length";
    public static final String SHIP="Ship";
    public static final String AGV="AGV";
    public static final String QUAY="Quay";
    public static final String ACTIVECONTAINERHOLDER_CAPABLE_OF="capable_of";
    public static final String ACTIVECONTAINERHOLDER_STOWAGE_CAPABILITY="stowage_capability";
    public static final String ACTIVECONTAINERHOLDER_TONNAGE_CAPACITY="tonnage_capacity";
    public static final String ACTIVECONTAINERHOLDER="ActiveContainerHolder";
    public static final String STATICCONTAINERHOLDER="StaticContainerHolder";
    public static final String YARD="Yard";
    public static final String PASSIVECONTAINERHOLDER="PassiveContainerHolder";
    public static final String LOADLIST_CONSISTS_OF="consists_of";
    public static final String LOADLIST="LoadList";
    public static final String TRAIN="Train";

  /**
   * Constructor
  */
  private ContainerTerminalOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema trainSchema = new ConceptSchema(TRAIN);
    add(trainSchema, mas.projects.contmas.ontology.Train.class);
    ConceptSchema loadListSchema = new ConceptSchema(LOADLIST);
    add(loadListSchema, mas.projects.contmas.ontology.LoadList.class);
    ConceptSchema passiveContainerHolderSchema = new ConceptSchema(PASSIVECONTAINERHOLDER);
    add(passiveContainerHolderSchema, mas.projects.contmas.ontology.PassiveContainerHolder.class);
    ConceptSchema yardSchema = new ConceptSchema(YARD);
    add(yardSchema, mas.projects.contmas.ontology.Yard.class);
    ConceptSchema staticContainerHolderSchema = new ConceptSchema(STATICCONTAINERHOLDER);
    add(staticContainerHolderSchema, mas.projects.contmas.ontology.StaticContainerHolder.class);
    ConceptSchema activeContainerHolderSchema = new ConceptSchema(ACTIVECONTAINERHOLDER);
    add(activeContainerHolderSchema, mas.projects.contmas.ontology.ActiveContainerHolder.class);
    ConceptSchema quaySchema = new ConceptSchema(QUAY);
    add(quaySchema, mas.projects.contmas.ontology.Quay.class);
    ConceptSchema agvSchema = new ConceptSchema(AGV);
    add(agvSchema, mas.projects.contmas.ontology.AGV.class);
    ConceptSchema shipSchema = new ConceptSchema(SHIP);
    add(shipSchema, mas.projects.contmas.ontology.Ship.class);
    ConceptSchema railSchema = new ConceptSchema(RAIL);
    add(railSchema, mas.projects.contmas.ontology.Rail.class);
    ConceptSchema transportOrderSchema = new ConceptSchema(TRANSPORTORDER);
    add(transportOrderSchema, mas.projects.contmas.ontology.TransportOrder.class);
    ConceptSchema twentyFootContainerSchema = new ConceptSchema(TWENTYFOOTCONTAINER);
    add(twentyFootContainerSchema, mas.projects.contmas.ontology.TwentyFootContainer.class);
    ConceptSchema domainAreaSchema = new ConceptSchema(DOMAINAREA);
    add(domainAreaSchema, mas.projects.contmas.ontology.DomainArea.class);
    ConceptSchema containerHolderSchema = new ConceptSchema(CONTAINERHOLDER);
    add(containerHolderSchema, mas.projects.contmas.ontology.ContainerHolder.class);
    ConceptSchema designatorSchema = new ConceptSchema(DESIGNATOR);
    add(designatorSchema, mas.projects.contmas.ontology.Designator.class);
    ConceptSchema blockAddressSchema = new ConceptSchema(BLOCKADDRESS);
    add(blockAddressSchema, mas.projects.contmas.ontology.BlockAddress.class);
    ConceptSchema transportOrderChainSchema = new ConceptSchema(TRANSPORTORDERCHAIN);
    add(transportOrderChainSchema, mas.projects.contmas.ontology.TransportOrderChain.class);
    ConceptSchema domainSchema = new ConceptSchema(DOMAIN);
    add(domainSchema, mas.projects.contmas.ontology.Domain.class);
    ConceptSchema reachStackerSchema = new ConceptSchema(REACHSTACKER);
    add(reachStackerSchema, mas.projects.contmas.ontology.ReachStacker.class);
    ConceptSchema streetSchema = new ConceptSchema(STREET);
    add(streetSchema, mas.projects.contmas.ontology.Street.class);
    ConceptSchema craneSchema = new ConceptSchema(CRANE);
    add(craneSchema, mas.projects.contmas.ontology.Crane.class);
    ConceptSchema bayMapSchema = new ConceptSchema(BAYMAP);
    add(bayMapSchema, mas.projects.contmas.ontology.BayMap.class);
    ConceptSchema rmgSchema = new ConceptSchema(RMG);
    add(rmgSchema, mas.projects.contmas.ontology.RMG.class);
    ConceptSchema seaSchema = new ConceptSchema(SEA);
    add(seaSchema, mas.projects.contmas.ontology.Sea.class);
    ConceptSchema landSchema = new ConceptSchema(LAND);
    add(landSchema, mas.projects.contmas.ontology.Land.class);
    ConceptSchema containerSchema = new ConceptSchema(CONTAINER);
    add(containerSchema, mas.projects.contmas.ontology.Container.class);

    // adding AgentAction(s)
    AgentActionSchema requestRandomBayMapSchema = new AgentActionSchema(REQUESTRANDOMBAYMAP);
    add(requestRandomBayMapSchema, mas.projects.contmas.ontology.RequestRandomBayMap.class);
    AgentActionSchema indicateContainerSchema = new AgentActionSchema(INDICATECONTAINER);
    add(indicateContainerSchema, mas.projects.contmas.ontology.IndicateContainer.class);
    AgentActionSchema lookForContainerSchema = new AgentActionSchema(LOOKFORCONTAINER);
    add(lookForContainerSchema, mas.projects.contmas.ontology.LookForContainer.class);
    AgentActionSchema assignBlockAddressSchema = new AgentActionSchema(ASSIGNBLOCKADDRESS);
    add(assignBlockAddressSchema, mas.projects.contmas.ontology.AssignBlockAddress.class);
    AgentActionSchema providePopulatedBayMapSchema = new AgentActionSchema(PROVIDEPOPULATEDBAYMAP);
    add(providePopulatedBayMapSchema, mas.projects.contmas.ontology.ProvidePopulatedBayMap.class);
    AgentActionSchema requestPopulatedBayMapSchema = new AgentActionSchema(REQUESTPOPULATEDBAYMAP);
    add(requestPopulatedBayMapSchema, mas.projects.contmas.ontology.RequestPopulatedBayMap.class);
    AgentActionSchema provideBayMapSchema = new AgentActionSchema(PROVIDEBAYMAP);
    add(provideBayMapSchema, mas.projects.contmas.ontology.ProvideBayMap.class);
    AgentActionSchema callForProposalsOnLoadStageSchema = new AgentActionSchema(CALLFORPROPOSALSONLOADSTAGE);
    add(callForProposalsOnLoadStageSchema, mas.projects.contmas.ontology.CallForProposalsOnLoadStage.class);
    AgentActionSchema proposeLoadOfferSchema = new AgentActionSchema(PROPOSELOADOFFER);
    add(proposeLoadOfferSchema, mas.projects.contmas.ontology.ProposeLoadOffer.class);
    AgentActionSchema getCraneListSchema = new AgentActionSchema(GETCRANELIST);
    add(getCraneListSchema, mas.projects.contmas.ontology.GetCraneList.class);
    AgentActionSchema provideCraneListSchema = new AgentActionSchema(PROVIDECRANELIST);
    add(provideCraneListSchema, mas.projects.contmas.ontology.ProvideCraneList.class);
    AgentActionSchema announceLoadStatusSchema = new AgentActionSchema(ANNOUNCELOADSTATUS);
    add(announceLoadStatusSchema, mas.projects.contmas.ontology.AnnounceLoadStatus.class);
    AgentActionSchema askForDestinationSchema = new AgentActionSchema(ASKFORDESTINATION);
    add(askForDestinationSchema, mas.projects.contmas.ontology.AskForDestination.class);
    AgentActionSchema acceptLoadOfferSchema = new AgentActionSchema(ACCEPTLOADOFFER);
    add(acceptLoadOfferSchema, mas.projects.contmas.ontology.AcceptLoadOffer.class);
    AgentActionSchema rejectLoadOfferSchema = new AgentActionSchema(REJECTLOADOFFER);
    add(rejectLoadOfferSchema, mas.projects.contmas.ontology.RejectLoadOffer.class);
    AgentActionSchema enrollAtHarborSchema = new AgentActionSchema(ENROLLATHARBOR);
    add(enrollAtHarborSchema, mas.projects.contmas.ontology.EnrollAtHarbor.class);
    AgentActionSchema assignHarborQuaySchema = new AgentActionSchema(ASSIGNHARBORQUAY);
    add(assignHarborQuaySchema, mas.projects.contmas.ontology.AssignHarborQuay.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    loadListSchema.add(LOADLIST_CONSISTS_OF, transportOrderChainSchema, 1, ObjectSchema.UNLIMITED);
    activeContainerHolderSchema.add(ACTIVECONTAINERHOLDER_TONNAGE_CAPACITY, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    activeContainerHolderSchema.add(ACTIVECONTAINERHOLDER_STOWAGE_CAPABILITY, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    activeContainerHolderSchema.add(ACTIVECONTAINERHOLDER_CAPABLE_OF, domainSchema, 1, ObjectSchema.UNLIMITED);
    shipSchema.add(SHIP_LENGTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    transportOrderSchema.add(TRANSPORTORDER_TAKES, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    transportOrderSchema.add(TRANSPORTORDER_STARTS_AT, designatorSchema, ObjectSchema.MANDATORY);
    transportOrderSchema.add(TRANSPORTORDER_ENDS_AT, designatorSchema, ObjectSchema.OPTIONAL);
    domainAreaSchema.add(DOMAINAREA_PART_OF, domainAreaSchema, ObjectSchema.MANDATORY);
    domainAreaSchema.add(DOMAINAREA_FROM, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    domainAreaSchema.add(DOMAINAREA_TO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    containerHolderSchema.add(CONTAINERHOLDER_ADMINISTERS, loadListSchema, ObjectSchema.OPTIONAL);
    containerHolderSchema.add(CONTAINERHOLDER_LIVES_IN, domainSchema, ObjectSchema.MANDATORY);
    containerHolderSchema.add(CONTAINERHOLDER_CONTAINS, bayMapSchema, ObjectSchema.OPTIONAL);
    designatorSchema.add(DESIGNATOR_ABSTRACT_DESIGNATION, domainSchema, ObjectSchema.OPTIONAL);
    designatorSchema.add(DESIGNATOR_TYPE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    designatorSchema.add(DESIGNATOR_CONCRETE_DESIGNATION, (ConceptSchema)getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_Z_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_X_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_Y_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    blockAddressSchema.add(BLOCKADDRESS_LOCATES, containerSchema, ObjectSchema.OPTIONAL);
    transportOrderChainSchema.add(TRANSPORTORDERCHAIN_IS_LINKED_BY, transportOrderSchema, 1, ObjectSchema.UNLIMITED);
    transportOrderChainSchema.add(TRANSPORTORDERCHAIN_TERMINATES_AT, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    transportOrderChainSchema.add(TRANSPORTORDERCHAIN_TRANSPORTS, containerSchema, ObjectSchema.MANDATORY);
    domainSchema.add(DOMAIN_HAS_SUBDOMAINS, domainSchema, 0, ObjectSchema.UNLIMITED);
    domainSchema.add(DOMAIN_LIES_IN, domainSchema, ObjectSchema.OPTIONAL);
    bayMapSchema.add(BAYMAP_IS_FILLED_WITH, blockAddressSchema, 0, ObjectSchema.UNLIMITED);
    bayMapSchema.add(BAYMAP_Z_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    bayMapSchema.add(BAYMAP_X_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    bayMapSchema.add(BAYMAP_Y_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_CARRIES_DANGEROUS_GOODS, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_WEIGHT, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_WIDTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_IS_COOLABLE, (TermSchema)getSchema(BasicOntology.BOOLEAN), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_HEIGHT, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_ID, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    containerSchema.add(CONTAINER_LENGTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    requestRandomBayMapSchema.add(REQUESTRANDOMBAYMAP_Z_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    requestRandomBayMapSchema.add(REQUESTRANDOMBAYMAP_X_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    requestRandomBayMapSchema.add(REQUESTRANDOMBAYMAP_Y_DIMENSION, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);
    indicateContainerSchema.add(INDICATECONTAINER_FOUND, containerSchema, 1, ObjectSchema.UNLIMITED);
    lookForContainerSchema.add(LOOKFORCONTAINER_SEARCHED_AFTER, containerSchema, ObjectSchema.MANDATORY);
    assignBlockAddressSchema.add(ASSIGNBLOCKADDRESS_STORE_IN, blockAddressSchema, ObjectSchema.MANDATORY);
    providePopulatedBayMapSchema.add(PROVIDEPOPULATEDBAYMAP_PROVIDES, bayMapSchema, ObjectSchema.MANDATORY);
    requestPopulatedBayMapSchema.add(REQUESTPOPULATEDBAYMAP_POPULATE_ON, bayMapSchema, ObjectSchema.MANDATORY);
    provideBayMapSchema.add(PROVIDEBAYMAP_PROVIDES, bayMapSchema, ObjectSchema.MANDATORY);
    callForProposalsOnLoadStageSchema.add(CALLFORPROPOSALSONLOADSTAGE_REQUIRED_TURNOVER_CAPACITY, loadListSchema, ObjectSchema.OPTIONAL);
    proposeLoadOfferSchema.add(PROPOSELOADOFFER_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    getCraneListSchema.add(GETCRANELIST_ASSIGNED_QUAY, quaySchema, ObjectSchema.OPTIONAL);
    getCraneListSchema.add(GETCRANELIST_REQUIRED_TURNOVER_CAPACITY, loadListSchema, ObjectSchema.OPTIONAL);
    provideCraneListSchema.add(PROVIDECRANELIST_AVAILABLE_CRANES, (ConceptSchema)getSchema(BasicOntology.AID), 0, ObjectSchema.UNLIMITED);
    announceLoadStatusSchema.add(ANNOUNCELOADSTATUS_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    announceLoadStatusSchema.add(ANNOUNCELOADSTATUS_LOAD_STATUS, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    askForDestinationSchema.add(ASKFORDESTINATION_TO_BE_STORED, transportOrderChainSchema, ObjectSchema.MANDATORY);
    acceptLoadOfferSchema.add(ACCEPTLOADOFFER_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    rejectLoadOfferSchema.add(REJECTLOADOFFER_LOAD_OFFER, transportOrderChainSchema, ObjectSchema.OPTIONAL);
    enrollAtHarborSchema.add(ENROLLATHARBOR_SHIP_LENGTH, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    assignHarborQuaySchema.add(ASSIGNHARBORQUAY_ASSIGNED_QUAY, quaySchema, ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance
    trainSchema.addSuperSchema(passiveContainerHolderSchema);
    passiveContainerHolderSchema.addSuperSchema(containerHolderSchema);
    yardSchema.addSuperSchema(staticContainerHolderSchema);
    staticContainerHolderSchema.addSuperSchema(containerHolderSchema);
    activeContainerHolderSchema.addSuperSchema(containerHolderSchema);
    quaySchema.addSuperSchema(domainSchema);
    agvSchema.addSuperSchema(passiveContainerHolderSchema);
    shipSchema.addSuperSchema(passiveContainerHolderSchema);
    railSchema.addSuperSchema(domainSchema);
    twentyFootContainerSchema.addSuperSchema(containerSchema);
    reachStackerSchema.addSuperSchema(activeContainerHolderSchema);
    streetSchema.addSuperSchema(domainSchema);
    craneSchema.addSuperSchema(activeContainerHolderSchema);
    rmgSchema.addSuperSchema(activeContainerHolderSchema);
    seaSchema.addSuperSchema(domainSchema);
    landSchema.addSuperSchema(domainSchema);

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
