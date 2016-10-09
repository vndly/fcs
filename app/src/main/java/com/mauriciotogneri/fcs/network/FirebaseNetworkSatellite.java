package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.model.Session;
import com.mauriciotogneri.fcs.network.data.AccelerometerDataEntry;
import com.mauriciotogneri.fcs.network.data.LocationDataEntry;
import com.mauriciotogneri.fcs.network.data.RotationDataEntry;

public class FirebaseNetworkSatellite implements NetworkSatellite
{
    private DatabaseReference accelerometerRef;
    private DatabaseReference rotationRef;
    private DatabaseReference locationRef;

    public FirebaseNetworkSatellite(Session session)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference indexRef = database.getReference("index");
        indexRef.child(String.valueOf(session.timestamp())).setValue(session.id());

        DatabaseReference sessionRef = database.getReference("sessions").child(session.id());

        this.accelerometerRef = sessionRef.child("accelerometer");
        this.rotationRef = sessionRef.child("rotation");
        this.locationRef = sessionRef.child("location");
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        DatabaseReference ref = accelerometerRef.child(String.valueOf(data.timestamp()));
        ref.setValue(new AccelerometerDataEntry(data));
    }

    @Override
    public void onRotationData(RotationData data)
    {
        DatabaseReference ref = rotationRef.child(String.valueOf(data.timestamp()));
        ref.setValue(new RotationDataEntry(data));
    }

    @Override
    public void onLocationData(LocationData data)
    {
        DatabaseReference ref = locationRef.child(String.valueOf(data.timestamp()));
        ref.setValue(new LocationDataEntry(data));
    }
}