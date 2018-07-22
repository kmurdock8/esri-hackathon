package com.esri.geoevent.processor;

import com.esri.core.geometry.*;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.core.geoevent.GeoEventDefinition;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.messaging.*;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;

import java.util.*;

public class WheresFidoProcessor extends GeoEventProcessorBase implements GeoEventProducer, EventUpdatable
{
  /**
   * Initialize the i18n Bundle Logger
   * 
   * See {@link BundleLogger} for more info.
   */
  	private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(WheresFidoProcessor.class);
	private GeoEventDefinitionManager manager;
	private Messaging messaging;
	private GeoEventCreator geoEventCreator;
	private GeoEventProducer geoEventProducer;

	private SpatialReference sr = SpatialReference.create(3857);

  	// User input
    private String lostDef;
    private String sightingDef;
    private String foundDef;

    private WheresFidoDB DBConn;
    private GeoEventDefinition emailAlert;

	protected WheresFidoProcessor(GeoEventProcessorDefinition definition, GeoEventDefinitionManager m) throws ComponentException
	{
		super(definition);
		manager = m;
	}

	@Override
	public GeoEvent process(GeoEvent geoEvent) throws Exception
	{
		try {
			// get definition
			String def = geoEvent.getGeoEventDefinition().getName();

			// get all the users that have notifications on
			List<User> users = DBConn.getUsersWithNotificationsOn();
			LOGGER.info("USERS: " + users.toString());

			// LOST DOG
			if (def.equals(lostDef)) {

				// get geometry from geoevent
				//MapGeometry mapgeo = (MapGeometry) geoEvent.getField("SearchArea");
				//Geometry searchGeo = mapgeo.getGeometry();
				// buffer
				MapGeometry mapGeo = GeometryEngine.jsonToGeometry("{\"rings\":[[[-13044846.342295593,4034804.5146240643]," +
						"[-13044734.95070851,4034800.2245838121],[-13044624.213060807,4034787.3796578567]," +
						"[-13044514.779448444,4034766.0552823106],[-13044407.292303221,4034736.376690553]," +
						"[-13044302.38261731,4034698.5181761361],[-13044200.666235294,4034652.702066957]," +
						"[-13044102.740235623,4034599.197416815],[-13044009.179422822,4034538.3184221219]," +
						"[-13043920.532951137,4034470.4225731408],[-13043837.321099471,4034395.9085507556]," +
						"[-13043760.032216629,4034315.2138811904],[-13043689.119854849,4034228.812362609]," +
						"[-13043625.000108374,4034137.2112788227],[-13043568.049172794,4034040.9484165949]," +
						"[-13043518.60113935,4033940.588904174],[-13043476.946037166,4033836.7218897752]," +
						"[-13043443.328134799,4033729.9570795358],[-13043417.944510994,4033620.9211554816]," +
						"[-13043400.943902956,4033510.2540945313],[-13043392.425838741,4033398.6054102755]," +
						"[-13043392.440058779,4033286.630339596],[-13043400.986229761,4033174.9859966021]," +
						"[-13043418.013952464,4033064.3275164268],[-13043443.423063284,4032955.3042115262]," +
						"[-13043477.064227624,4032848.5557630416],[-13043518.739821495,4032744.7084694901]," +
						"[-13043568.205096036,4032644.371574779],[-13043625.169618007,4032548.1336969663]," +
						"[-13043689.298977707,4032456.5593786999],[-13043760.216754224,4032370.1857793783]," +
						"[-13043837.506726397,4032289.5195284095],[-13043920.715316463,4032215.0337578519]," +
						"[-13044009.354252053,4032147.165331726],[-13044102.903430911,4032086.3122881441]," +
						"[-13044200.813971482,4032032.8315091101],[-13044302.511431588,4031987.0366315064]," +
						"[-13044407.399176285,4031949.1962114861],[-13044514.861875281,4031919.5321528269]," +
						"[-13044624.269109407,4031898.2184084374],[-13044734.979065139,4031885.3799625174]," +
						"[-13044846.342295593,4031881.0920992843],[-13044957.705526046,4031885.3799625169]," +
						"[-13045068.415481778,4031898.2184084374],[-13045177.822715905,4031919.5321528269]," +
						"[-13045285.285414901,4031949.1962114861],[-13045390.173159597,4031987.0366315064]," +
						"[-13045491.870619703,4032032.8315091101],[-13045589.781160275,4032086.3122881441]," +
						"[-13045683.330339132,4032147.165331726],[-13045771.969274722,4032215.0337578519]," +
						"[-13045855.177864788,4032289.5195284095],[-13045932.467836961,4032370.1857793783]," +
						"[-13046003.385613479,4032456.5593786999],[-13046067.514973179,4032548.1336969663]," +
						"[-13046124.479495149,4032644.3715747781],[-13046173.94476969,4032744.7084694901]," +
						"[-13046215.620363561,4032848.5557630416],[-13046249.261527902,4032955.3042115262]," +
						"[-13046274.670638721,4033064.3275164263],[-13046291.698361425,4033174.9859966021]," +
						"[-13046300.244532406,4033286.630339596],[-13046300.258752445,4033398.6054102755]," +
						"[-13046291.740688229,4033510.2540945313],[-13046274.740080191,4033620.9211554816]," +
						"[-13046249.356456386,4033729.9570795358],[-13046215.738554019,4033836.7218897766]," +
						"[-13046174.083451835,4033940.588904174],[-13046124.635418391,4034040.9484165949]," +
						"[-13046067.684482811,4034137.2112788227],[-13046003.564736336,4034228.812362609]," +
						"[-13045932.652374556,4034315.2138811904],[-13045855.363491714,4034395.9085507542]," +
						"[-13045772.151640048,4034470.4225731408]," +
						"[-13045683.505168363,4034538.3184221173]," +
						"[-13045589.944355562,4034599.1974168164]," +
						"[-13045492.018355889,4034652.702066957]," +
						"[-13045390.301973874,4034698.5181761361]," +
						"[-13045285.392287964,4034736.376690553]," +
						"[-13045177.905142741,4034766.0552823106]," +
						"[-13045068.471530378,4034787.3796578567]," +
						"[-13044957.733882675,4034800.2245838121],[-13044846.342295593,4034804.5146240643]]]," +
						"\"spatialReference\":{\"wkid\":102100,\"latestWkid\":3857}}");
				Geometry searchGeo = mapGeo.getGeometry();




				// loop through users
				for (User u: users) {
					if (u.getEmail().equals("wheresfidodemo@gmail.com")) {
						double lat = u.getLat();
						double lon = u.getLong();

						Point userLocation = new Point(lon, lat);

						// See if they intersect
						//boolean userInSearchRadius = GeometryEngine.crosses(searchGeo, userLocation, sr);
						boolean userInSearchRadius = true;
						if (userInSearchRadius) {
							// Send email alert
							String guidStr = emailAlert.getGuid();
							GeoEvent lostGeoEvent = geoEventCreator.create(guidStr);

							String subject = "LOST DOG IN YOUR AREA";
							String messageHTML = "<p><strong>Lost Dog in your area:</strong></p>\n" +
									"<p style=\"padding-left: 30px;\">Dog Name: " + geoEvent.getField("DogName").toString() + "</p>\n" +
									"<p style=\"padding-left: 30px;\">Breed: " + geoEvent.getField("Breed").toString() + "</p>\n" +
									"<p style=\"padding-left: 30px;\">Gender: " + geoEvent.getField("Gender").toString() + "</p>\n" +
									"<p style=\"padding-left: 30px;\">Description: " + geoEvent.getField("Description").toString() + "</p>\n" +
									"<p>&nbsp;</p>\n" +
									"<p>*You are receiving this because you have notification on in the Where's Fido app. " +
									"To Stop receiving these type of notifications, go into the app and update notification " +
									"preferences.</p>";

							lostGeoEvent.setField("userEmail", u.getEmail());
							lostGeoEvent.setField("Subject", subject);
							lostGeoEvent.setField("Message", messageHTML);

							//send(lostGeoEvent);
							return lostGeoEvent;
						}
					}
				}
			}

			// SIGHTING
			if (def.equals(sightingDef)) {
				// Send email alert to owner
				String guidStr = emailAlert.getGuid();
				GeoEvent sightingGeoEvent = geoEventCreator.create(guidStr);

				String subject = "LOST DOG SIGHTING";
				String messageHTML = "<p><strong>Your lost dog has been sighted:</strong></p>\n" +
						"<p style=\"padding-left: 30px;\">Dog Name: " + geoEvent.getField("DogName").toString() + "</p>\n" +
						"<p style=\"padding-left: 30px;\">Message: " + geoEvent.getField("Description").toString() + "</p>\n" +
						"<p style=\"padding-left: 30px;\">User that reported sighting: zack.m.allen@gmail.com </p>\n" +
						"<p>&nbsp;</p>\n" +
						"<p>*You are receiving this because you have reported your dog missing on the Where's Fido app.";

				sightingGeoEvent.setField("userEmail", "kmurdock@esri.com");
				sightingGeoEvent.setField("Subject", subject);
				sightingGeoEvent.setField("Message", messageHTML);

				//send(lostGeoEvent);
				return sightingGeoEvent;
			}

			// FOUND
			if (def.equals(foundDef)) {
				// Send email alert to owner
				String guidStr = emailAlert.getGuid();
				GeoEvent foundGeoEvent = geoEventCreator.create(guidStr);

				String subject = "LOST DOG HAS BEEN FOUND!";
				String messageHTML = "<p><strong>Your lost dog has been found:</strong></p>\n" +
						"<p style=\"padding-left: 30px;\">Message from user: " + geoEvent.getField("MessageToOwner").toString() + "</p>\n" +
						"<p style=\"padding-left: 30px;\">User contact info: wheresfidodemo@gmail.com </p>\n" +
						"<p style=\"padding-left: 30px;\">User contact information: wheresfidodemo@gmail.com </p>\n" +
						"<p>&nbsp;</p>\n" +
						"<p>*You are receiving this because you have reported your dog missing on the Where's Fido app.";

				foundGeoEvent.setField("userEmail", "kmurdock@esri.com");
				foundGeoEvent.setField("Subject", subject);
				foundGeoEvent.setField("Message", messageHTML);

				//send(lostGeoEvent);
				return foundGeoEvent;
			}

			LOGGER.info("GeoEvent Definition did  not match the lost, found, or sighting definitions.");
		} catch (Exception e) {
			LOGGER.info("ERROR: "  + e.getMessage());
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		// Get user input
		this.lostDef = (String) this.properties.get("lostDef").getValue();
		this.sightingDef = (String) this.properties.get("sightingDef").getValue();
		this.foundDef = (String) this.properties.get("foundDef").getValue();

		DBConn = new WheresFidoDB();

		Collection<GeoEventDefinition> defs = manager.searchInRepositoryGeoEventDefinitionByName("email-alert");
		this.emailAlert = defs.iterator().next();
	}

	public void setMessaging(Messaging messaging) {
		this.messaging = messaging;
		this.geoEventCreator = messaging.createGeoEventCreator();
	}

	@Override
	public void setId(String id) {
		super.setId(id);
		geoEventProducer = messaging.createGeoEventProducer(new EventDestination(id + ":event"));
	}

	@Override
	public void send(GeoEvent geoEvent) throws MessagingException {
		if (geoEventProducer != null && geoEvent != null) {
			geoEventProducer.send(geoEvent);
		}
	}

	@Override
	public EventDestination getEventDestination()
	{
		return (geoEventProducer != null) ? geoEventProducer.getEventDestination() : null;
	}

	@Override
	public List<EventDestination> getEventDestinations()
	{
		return (geoEventProducer != null) ? Arrays.asList(geoEventProducer.getEventDestination()) : new ArrayList<EventDestination>();
	}

	@Override
	public void disconnect()
	{
		if (geoEventProducer != null)
			geoEventProducer.disconnect();
	}

	@Override
	public boolean isConnected()
	{
		return (geoEventProducer != null) ? geoEventProducer.isConnected() : false;
	}

	@Override
	public String getStatusDetails()
	{
		return (geoEventProducer != null) ? geoEventProducer.getStatusDetails() : "";
	}

	@Override
	public void setup() throws MessagingException
	{
		;
	}

	@Override
	public void init() throws MessagingException
	{
		;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		;
	}


}