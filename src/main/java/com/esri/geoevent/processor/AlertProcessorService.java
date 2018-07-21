package com.esri.geoevent.processor;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class AlertProcessorService extends GeoEventProcessorServiceBase
{
  public AlertProcessorService()
  {
    definition = new AlertProcessorDefinition();
  }

  @Override
  public GeoEventProcessor create() throws ComponentException
  {
    return new AlertProcessor(definition);
  }
}