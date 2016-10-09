package com.mauriciotogneri.fcs.satellite.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mauriciotogneri.fcs.model.AccelerometerData;
import com.mauriciotogneri.fcs.model.LocationData;
import com.mauriciotogneri.fcs.model.RotationData;
import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;
import com.mauriciotogneri.fcs.util.DateUtil;
import com.mauriciotogneri.fcs.util.NumberUtil;

public class Database implements AccelerometerListener, RotationListener, LocationListener
{
    private DatabaseReference accelerometer;
    private DatabaseReference rotation;
    private DatabaseReference location;

    public Database()
    {
        long timestamp = System.currentTimeMillis();
        String sessionId = DateUtil.format(timestamp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference index = database.getReference("index");
        index.child(String.valueOf(timestamp)).setValue(sessionId);

        DatabaseReference sessions = database.getReference("sessions");
        DatabaseReference session = sessions.child(sessionId);

        accelerometer = session.child("accelerometer");
        rotation = session.child("rotation");
        location = session.child("location");
    }

    @Override
    public void onAccelerometerData(AccelerometerData data)
    {
        DatabaseReference ref = accelerometer.child(String.valueOf(data.timestamp()));
        ref.child("x").setValue(NumberUtil.asInt(data.x()));
        ref.child("y").setValue(NumberUtil.asInt(data.y()));
        ref.child("z").setValue(NumberUtil.asInt(data.z()));
    }

    @Override
    public void onRotationData(RotationData data)
    {
        DatabaseReference ref = rotation.child(String.valueOf(data.timestamp()));
        ref.child("x").setValue(NumberUtil.asInt(data.x()));
        ref.child("y").setValue(NumberUtil.asInt(data.y()));
        ref.child("z").setValue(NumberUtil.asInt(data.z()));
    }

    @Override
    public void onLocationData(LocationData data)
    {
        DatabaseReference ref = location.child(String.valueOf(data.timestamp()));
        ref.child("latitude").setValue(NumberUtil.asInt(data.latitude()));
        ref.child("longitude").setValue(NumberUtil.asInt(data.longitude()));
        ref.child("altitude").setValue(NumberUtil.asInt(data.altitude()));
        ref.child("accuracy").setValue(NumberUtil.asInt(data.accuracy()));
        ref.child("speed").setValue(NumberUtil.asInt(data.speed()));
        ref.child("bearing").setValue(NumberUtil.asInt(data.bearing()));
    }
}