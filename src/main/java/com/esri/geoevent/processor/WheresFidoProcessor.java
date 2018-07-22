package com.esri.geoevent.processor;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;

import java.util.List;

public class WheresFidoProcessor extends GeoEventProcessorBase
{
  /**
   * Initialize the i18n Bundle Logger
   * 
   * See {@link BundleLogger} for more info.
   */
  	private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(WheresFidoProcessor.class);

  	// User input
    private String lostDef;
    private String sightingDef;
    private String foundDef;

    // DB connection
	private WheresFidoDB DBConn;

	protected WheresFidoProcessor(GeoEventProcessorDefinition definition) throws ComponentException
	{
		super(definition);
	}

	@Override
	public GeoEvent process(GeoEvent geoEvent) throws Exception
	{
		try {
			// get definition
			String def = geoEvent.getGeoEventDefinition().getName();

			// get all the users that have notifications on
			List<User> users = DBConn.getUsersWithNotificationsOn();

			// LOST DOG
			if (def.equals(lostDef)) {

				return null;
			}

			// SIGHTING
			if (def.equals(sightingDef)) {

				return null;
			}

			// FOUND
			if (def.equals(foundDef)) {

				return null;
			}

			LOGGER.info("GeoEvent Definition did  not match the lost, found, or sighting definitions.");
		} catch (Exception e) {
			LOGGER.info("ERROR: "  + e.getMessage());
		}
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

		return null;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		// Get user input
		this.lostDef = (String) this.properties.get("lostDef").getValue();
		this.sightingDef = (String) this.properties.get("sightingDef").getValue();
		this.foundDef = (String) this.properties.get("foundDef").getValue();

		// Initialize db connection object
		DBConn = new WheresFidoDB();
	}
}