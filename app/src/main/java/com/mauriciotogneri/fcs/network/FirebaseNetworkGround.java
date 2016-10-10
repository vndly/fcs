package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    public FirebaseNetworkGround(final SensorListener sensorListener)
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
        DatabaseReference accelerometerRef = sessionsRef.child("accelerometer");

        accelerometerRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (s != null)
                {
                    long timestamp = Long.parseLong(dataSnapshot.getKey());
                    AccelerometerDataEntry entry = dataSnapshot.getValue(AccelerometerDataEntry.class);

                    sensorListener.onAccelerometerData(entry.data(timestamp));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private void listenRotationData(DatabaseReference sessionsRef)
    {
        DatabaseReference accelerometerRef = sessionsRef.child("rotation");

        accelerometerRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (s != null)
                {
                    long timestamp = Long.parseLong(dataSnapshot.getKey());
                    RotationDataEntry entry = dataSnapshot.getValue(RotationDataEntry.class);

                    sensorListener.onRotationData(entry.data(timestamp));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private void listenBarometerData(DatabaseReference sessionsRef)
    {
        DatabaseReference accelerometerRef = sessionsRef.child("barometer");

        accelerometerRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (s != null)
                {
                    long timestamp = Long.parseLong(dataSnapshot.getKey());
                    BarometerDataEntry entry = dataSnapshot.getValue(BarometerDataEntry.class);

                    sensorListener.onBarometerData(entry.data(timestamp));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private void listenLocationData(DatabaseReference sessionsRef)
    {
        DatabaseReference accelerometerRef = sessionsRef.child("location");

        accelerometerRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (s != null)
                {
                    long timestamp = Long.parseLong(dataSnapshot.getKey());
                    LocationDataEntry entry = dataSnapshot.getValue(LocationDataEntry.class);

                    sensorListener.onLocationData(entry.data(timestamp));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
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
}