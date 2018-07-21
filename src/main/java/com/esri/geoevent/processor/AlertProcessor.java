package com.esri.geoevent.processor;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.core.property.Property;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;

public class AlertProcessor extends AsyncGeoEventProcessor
{
  /**
   * Initialize the i18n Bundle Logger
   * 
   * See {@link BundleLogger} for more info.
   */
  	private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(AlertProcessor.class);
  


	protected AlertProcessor(GeoEventProcessorDefinition definition) throws ComponentException
	{
		super(definition);
		//test
	}

	@Override
	public GeoEvent process(GeoEvent geoEvent) throws Exception
	{
		return geoEvent;
	}

	protected GeoEvent processAsync(GeoEvent geoEvent) {

		if (geoEvent!= null) {
			try {
				// Send out the new GeoEvent
				send(geoEvent);
			} catch (Exception e) {
				LOGGER.info("ERROR IN PROCESS ASYNC METHOD");
				LOGGER.info(e.getMessage());
			}
		}
		return null;
	}

}