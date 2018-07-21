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

  	// User input
    private String lostDef;
    private String sightingDef;
    private String foundDef;
  


	protected AlertProcessor(GeoEventProcessorDefinition definition) throws ComponentException
	{
		super(definition);
		//test
	}

	@Override
	public GeoEvent process(GeoEvent geoEvent) throws Exception
	{
	    // get geoevent def name

        // Switch on def name
        // case lostDef:

            // query db and get all of the users that have their notifications set to on
            // get their email, lat, and long of the address they provided = usersout

            // get search geometry from the geoevent definition
            // for each user in usersout:
                // create point from lat and long values
                // use geometry engine to see if these two geometries intersect

                // if they do intersect:
                    // create new email alert def with the users email
                    // add email alert geoevent to the async processor blocking queue to send it out asysncrously


            //return null;
        // case sightingdef:

            // get owner email from geoevent and send them an alert first

            // get all users that have notifications set to on (make this a global variable maybe?)
            // get their email, lat, and long of the address they provided = usersout

            // get search geo from geoevent
            // for each user in userout:
                // check if user has already been notified from watching list
                // if not:
                    // see if the points intersect (might be faster to check this first)
                    // if they do intersect:
                        // create new email alert geoevent
                        // send it out


        // case foundDef:

	        // send owner new email alert

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