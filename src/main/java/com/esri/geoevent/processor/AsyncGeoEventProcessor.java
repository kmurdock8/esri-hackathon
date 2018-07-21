package com.esri.geoevent.processor;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.core.geoevent.GeoEventPropertyName;
import com.esri.ges.framework.i18n.BundleLogger;
import com.esri.ges.framework.i18n.BundleLoggerFactory;
import com.esri.ges.messaging.*;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.*;

/**
 * Abstract base class for an Asynchronous GeoEvent Processor. Asynchronous processors create zero or more
 * GeoEvents without blocking the flow of the service they belong to. Incoming GeoEvents are added to
 * a private queue to make them available to the processor.
 */
public abstract class AsyncGeoEventProcessor extends GeoEventProcessorBase
    implements GeoEventProducer, EventUpdatable
{

    protected Messaging messaging;
    private final ExecutorService executorService;
    protected GeoEventProducer geoEventProducer;
    protected GeoEventCreator geoEventCreator;

    protected final BlockingQueue<GeoEvent> blockingQueue = new LinkedBlockingQueue<>(1000);

    private Future<?> eventLoop;

    private boolean isPassthroughEnabled = false;

    /**
     * Initializes an AsyncGeoEventProcessorBase instance with a single thread.
     * @throws ComponentException
     */
    public AsyncGeoEventProcessor(GeoEventProcessorDefinition definition) throws ComponentException {
        this(definition, Executors.newSingleThreadScheduledExecutor());
    }

    public AsyncGeoEventProcessor(GeoEventProcessorDefinition definition,
                                      ExecutorService executorService) throws ComponentException {
        super(definition);
        geoEventMutator = true;
        this.executorService = executorService;
    }

    public void setMessaging(Messaging messaging)
    {
        this.messaging = messaging;
        this.geoEventCreator = messaging.createGeoEventCreator();
    }

    /**
     * Initialize the i18n Bundle Logger
     *
     * See {@link BundleLogger} for more info.
     */
    private static final BundleLogger LOGGER = BundleLoggerFactory.getLogger(AsyncGeoEventProcessor.class);


    @Override
    public void setId(String id)
    {
        super.setId(id);
        geoEventProducer = messaging.createGeoEventProducer(new EventDestination(id + ":event"));
    }


    @Override
    public GeoEvent process(GeoEvent geoEvent) throws Exception
    {
        if(geoEvent != null)
        {
            LOGGER.info(Thread.currentThread().getId() + "- Adding to async queue: " + geoEvent);

            // This will block if queue has no room, to avoid dropping data. This will ensure correct calculations at
            // the expense of slowing down the GeoEvent service. This can be moved to a configurable option in the
            // future.
            blockingQueue.put(geoEvent);

            if(isPassthroughEnabled) {
                LOGGER.info(Thread.currentThread().getId() + "- Passing through GeoEvent: " + geoEvent);
                return geoEvent;
            }
        }
        return null;
    }


    @Override
    public void onServiceStart() {
        super.onServiceStart();

        if (hasProperty("passthroughMode"))
        {
            isPassthroughEnabled = (Boolean) this.properties.get("passthroughMode").getValue();
        }

        if(eventLoop != null) {
            this.eventLoop.cancel(true);
        }
        this.eventLoop = executorService.submit(constructEventLoop(this.blockingQueue));
    }

    @Override
    public void onServiceStop() {
        super.onServiceStop();
        if(eventLoop != null) {
            this.eventLoop.cancel(true);
        }
    }

    protected Runnable constructEventLoop(BlockingQueue<GeoEvent> inboundQueue) {
        return () -> {
            try {
                while (!Thread.currentThread().isInterrupted())
                {
                    // Take one GeoEvent
                    GeoEvent inputGeoEvent = inboundQueue.take();

                    LOGGER.debug(Thread.currentThread().getId() + " - Processing GeoEvent: " + inputGeoEvent);

                    processAsync(inputGeoEvent);
                }
            }
            catch(InterruptedException ie) {
                LOGGER.info("Thread interrupted:" + ie.getMessage());
            }
        };
    }

    /**
     * Process a GeoEvent asynchronously.
     * The returned GeoEvent does not need to be the same GeoEvent definition as the input.
     * @param geoEvent
     * @return
     */
    protected abstract GeoEvent processAsync(GeoEvent geoEvent);

    @Override
    public void send(GeoEvent geoEvent) throws MessagingException {
        if (geoEventProducer != null && geoEvent != null)
        {
            geoEvent.setProperty(GeoEventPropertyName.TYPE, "event");
            geoEvent.setProperty(GeoEventPropertyName.OWNER_ID, getId());
            geoEvent.setProperty(GeoEventPropertyName.OWNER_URI,definition.getUri());
            geoEventProducer.send(geoEvent);
        }
    }

    @Override
    public String getStatusDetails() {
        return (geoEventProducer != null) ? geoEventProducer.getStatusDetails()
            : "";
    }

    @Override
    public boolean isGeoEventMutator() {
        return true;
    }

    @Override
    public EventDestination getEventDestination() {
        return (geoEventProducer != null) ? geoEventProducer
            .getEventDestination() : null;
    }

    @Override
    public List<EventDestination> getEventDestinations() {
        return (geoEventProducer != null) ? Collections.singletonList(geoEventProducer
            .getEventDestination()) : new ArrayList<>();
    }

    @Override
    public void disconnect() {
        if (geoEventProducer != null)
            geoEventProducer.disconnect();
    }

    @Override
    public boolean isConnected() {
        return (geoEventProducer != null) && geoEventProducer.isConnected();
    }

    @Override
    public void setup() throws MessagingException {
        ;
    }

    @Override
    public void init() throws MessagingException {
        ;
    }

    @Override
    public void update(Observable o, Object arg) {
        ;
    }
}
