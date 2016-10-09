package com.mauriciotogneri.fcs.network;

import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;

public interface Network extends AccelerometerListener, RotationListener, LocationListener
{
}