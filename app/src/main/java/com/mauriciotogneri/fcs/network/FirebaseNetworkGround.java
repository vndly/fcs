package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mauriciotogneri.fcs.network.NodeReader.OnNewData;
import com.mauriciotogneri.fcs.network.data.AccelerometerDataEntry;
import com.mauriciotogneri.fcs.network.data.BarometerDataEntry;
import com.mauriciotogneri.fcs.network.data.LocationDataEntry;
import com.mauriciotogneri.fcs.network.data.RotationDataEntry;
import com.mauriciotogneri.fcs.satellite.sensors.SensorListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FirebaseNetworkGround
{
    private SensorListener sensorListener;
    private NodeReader<AccelerometerDataEntry> accelerometerNode;
    private NodeReader<RotationDataEntry> rotationNode;
    private NodeReader<BarometerDataEntry> barometerNode;
    private NodeReader<LocationDataEntry> locationNode;

    public FirebaseNetworkGround(final SensorListener sensorListener, final SessionListener sessionListener)
    {
        this.sensorListener = sensorListener;

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference indexRef = database.getReference("index");
        indexRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                indexRef.removeEventListener(this);

                String sessionId = lastSessionId(dataSnapshot);

                sessionListener.onSessionStarted(sessionId);

                DatabaseReference sessionsRef = database.getReference("sessions").child(sessionId);

                listenAccelerometerData(sessionsRef);
                listenRotationData(sessionsRef);
                listenBarometerData(sessionsRef);
                listenLocationData(sessionsRef);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private void listenAccelerometerData(DatabaseReference sessionsRef)
    {
        accelerometerNode = new NodeReader<>(sessionsRef.child("accelerometer"));
        accelerometerNode.read(AccelerometerDataEntry.class, new OnNewData<AccelerometerDataEntry>()
        {
            @Override
            public void onNewData(long timestamp, AccelerometerDataEntry entry)
            {
                sensorListener.onAccelerometerData(entry.data(timestamp));
            }
        });
    }

    private void listenRotationData(DatabaseReference sessionsRef)
    {
        rotationNode = new NodeReader<>(sessionsRef.child("rotation"));
        rotationNode.read(RotationDataEntry.class, new OnNewData<RotationDataEntry>()
        {
            @Override
            public void onNewData(long timestamp, RotationDataEntry entry)
            {
                sensorListener.onRotationData(entry.data(timestamp));
            }
        });
    }

    private void listenBarometerData(DatabaseReference sessionsRef)
    {
        barometerNode = new NodeReader<>(sessionsRef.child("barometer"));
        barometerNode.read(BarometerDataEntry.class, new OnNewData<BarometerDataEntry>()
        {
            @Override
            public void onNewData(long timestamp, BarometerDataEntry entry)
            {
                sensorListener.onBarometerData(entry.data(timestamp));
            }
        });
    }

    private void listenLocationData(DatabaseReference sessionsRef)
    {
        locationNode = new NodeReader<>(sessionsRef.child("location"));
        locationNode.read(LocationDataEntry.class, new OnNewData<LocationDataEntry>()
        {
            @Override
            public void onNewData(long timestamp, LocationDataEntry entry)
            {
                sensorListener.onLocationData(entry.data(timestamp));
            }
        });
    }

    @SuppressWarnings("unchecked")
    private String lastSessionId(DataSnapshot dataSnapshot)
    {
        Map<String, String> sessions = (Map<String, String>) dataSnapshot.getValue();

        List<String> keys = new ArrayList<>(sessions.keySet());

        Collections.sort(keys, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                return o1.compareTo(o2);
            }
        });

        return sessions.get(keys.get(keys.size() - 1));
    }

    public void stop()
    {
        accelerometerNode.close();
        rotationNode.close();
        barometerNode.close();
        locationNode.close();
    }

    public interface SessionListener
    {
        void onSessionStarted(String id);
    }
}