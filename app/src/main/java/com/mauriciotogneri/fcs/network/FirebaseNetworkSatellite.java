package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.BarometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.model.Session;
import com.mauriciotogneri.fcs.network.data.AccelerometerDataEntry;
import com.mauriciotogneri.fcs.network.data.BarometerDataEntry;
import com.mauriciotogneri.fcs.network.data.LocationDataEntry;
import com.mauriciotogneri.fcs.network.data.RotationDataEntry;

public class FirebaseNetworkSatellite implements NetworkSatellite
{
    private NodeWriter accelerometerNode;
    private NodeWriter rotationNode;
    private NodeWriter barometerNode;
    private NodeWriter locationNode;

    public FirebaseNetworkSatellite(Session session)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference indexRef = database.getReference("index");
        indexRef.child(String.valueOf(session.timestamp())).setValue(session.id());

        DatabaseReference sessionRef = database.getReference("sessions").child(session.id());

        this.accelerometerNode = new NodeWriter(sessionRef.child("accelerometer"));
        this.rotationNode = new NodeWriter(sessionRef.child("rotation"));
        this.barometerNode = new NodeWriter(sessionRef.child("barometer"));
        this.locationNode = new NodeWriter(sessionRef.child("location"));
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        accelerometerNode.write(data.timestamp(), new AccelerometerDataEntry(data));
    }

    @Override
    public void onRotationData(RotationData data)
    {
        rotationNode.write(data.timestamp(), new RotationDataEntry(data));
    }

    @Override
    public void onBarometerData(BarometerData data)
    {
        barometerNode.write(data.timestamp(), new BarometerDataEntry(data));
    }

    @Override
    public void onLocationData(LocationData data)
    {
        locationNode.write(data.timestamp(), new LocationDataEntry(data));
    }

    public void stop()
    {
        accelerometerNode.stop();
        rotationNode.stop();
        barometerNode.stop();
        locationNode.stop();
    }
}