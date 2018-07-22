package com.esri.geoevent.processor;

import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.core.property.PropertyType;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class WheresFidoProcessorDefinition extends GeoEventProcessorDefinitionBase
{
	private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(WheresFidoProcessor.class);
	private GeoEventDefinitionManager manager;

	public WheresFidoProcessorDefinition()
	{
		try {
			propertyDefinitions.put("lostDef",
					new PropertyDefinition("lostDef",
							PropertyType.GeoEventDefinition, "",
							"Lost Dog GeoEvent Definition",
							"Select the GeoEvent Definition that corresponds to a lost dog event.",
							true,
							false));
			propertyDefinitions.put("sightingDef",
					new PropertyDefinition("sightingDef",
							PropertyType.GeoEventDefinition, "",
							"Dog Sighting GeoEvent Definition",
							"Select the GeoEvent Definition that corresponds to a sighting of a lost dog.",
							true,
							false));
			propertyDefinitions.put("foundDef",
					new PropertyDefinition("foundDef",
							PropertyType.GeoEventDefinition, "",
							"Found Dog GeoEvent Definition",
							"Select the GeoEvent Definition that corresponds to a found dog event.",
							true,
							false));
		} catch (PropertyException e) {
			LOGGER.info("ERROR: property exception thrown in processor definition class");
		} catch (Exception e) {
			LOGGER.info("ERROR: " + e.getMessage());
		}
	}

	public void setManager(GeoEventDefinitionManager m) {
		this.manager = m;
	}

	@Override
	public String getName()
	{
		return "WheresFidoProcessor";
	}

	@Override
	public String getDomain()
	{
		return "com.esri.geoevent.processor";
	}

	@Override
	public String getVersion()
	{
		return "10.6.1";
	}

}
