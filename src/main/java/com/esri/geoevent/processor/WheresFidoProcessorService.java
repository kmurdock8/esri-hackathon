package com.esri.geoevent.processor;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class WheresFidoProcessorService extends GeoEventProcessorServiceBase
{
  public WheresFidoProcessorService()
  {
    definition = new WheresFidoProcessorDefinition();
  }

  @Override
  public GeoEventProcessor create() throws ComponentException
  {
    return new WheresFidoProcessor(definition);
  }
}