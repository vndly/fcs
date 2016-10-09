package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.model.Session;
import com.mauriciotogneri.fcs.util.NumberUtil;

public class FirebaseNetwork implements Network
{
    private DatabaseReference accelerometerRef;
    private DatabaseReference rotationRef;
    private DatabaseReference locationRef;

    public FirebaseNetwork(Session session)
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
        ref.child("x").setValue(NumberUtil.asInt(data.x()));
        ref.child("y").setValue(NumberUtil.asInt(data.y()));
        ref.child("z").setValue(NumberUtil.asInt(data.z()));
    }

    @Override
    public void onRotationData(RotationData data)
    {
        DatabaseReference ref = rotationRef.child(String.valueOf(data.timestamp()));
        ref.child("x").setValue(NumberUtil.asInt(data.x()));
        ref.child("y").setValue(NumberUtil.asInt(data.y()));
        ref.child("z").setValue(NumberUtil.asInt(data.z()));
    }

    @Override
    public void onLocationData(LocationData data)
    {
        DatabaseReference ref = locationRef.child(String.valueOf(data.timestamp()));
        ref.child("latitude").setValue(NumberUtil.asInt(data.latitude()));
        ref.child("longitude").setValue(NumberUtil.asInt(data.longitude()));
        ref.child("altitude").setValue(NumberUtil.asInt(data.altitude()));
        ref.child("accuracy").setValue(NumberUtil.asInt(data.accuracy()));
        ref.child("speed").setValue(NumberUtil.asInt(data.speed()));
        ref.child("bearing").setValue(NumberUtil.asInt(data.bearing()));
    }
}