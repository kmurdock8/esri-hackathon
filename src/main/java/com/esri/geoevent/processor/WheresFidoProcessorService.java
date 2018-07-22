package com.esri.geoevent.processor;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class WheresFidoProcessorService extends GeoEventProcessorServiceBase
{

  private GeoEventDefinitionManager manager;
  private Messaging messaging;

  public WheresFidoProcessorService()
  {
    definition = new WheresFidoProcessorDefinition();
  }

  @Override
  public GeoEventProcessor create() throws ComponentException {
    WheresFidoProcessor processor = new WheresFidoProcessor(definition, manager);
    processor.setMessaging(messaging);
    return processor;
  }

  public void setMessaging(Messaging messaging)
  {
    this.messaging = messaging;
  }

  public void setManager(GeoEventDefinitionManager m) {
    manager = m;
  }

  public void start() {
    WheresFidoProcessorDefinition procDef = (WheresFidoProcessorDefinition) definition;
    procDef.setManager(manager);
  }
}