package com.mauriciotogneri.fcs.satellite.sensors;

import com.mauriciotogneri.fcs.satellite.sensors.AccelerometerSensor.AccelerometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.BarometerSensor.BarometerListener;
import com.mauriciotogneri.fcs.satellite.sensors.LocationSensor.LocationListener;
import com.mauriciotogneri.fcs.satellite.sensors.RotationSensor.RotationListener;

public interface SensorListener extends AccelerometerListener, RotationListener, BarometerListener, LocationListener
{
}