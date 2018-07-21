package com.esri.geoevent.processor;

import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class AlertProcessorDefinition extends GeoEventProcessorDefinitionBase
{
	public AlertProcessorDefinition()
	{
		// TODO: add property defs here
		;
	}

	@Override
	public String getName()
	{
		return "AlertProcessor";
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
