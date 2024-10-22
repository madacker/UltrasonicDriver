package org.swerve.driver;

import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

@SuppressWarnings({"WeakerAccess", "unused"}) // Ignore access and unused warnings
@I2cDeviceType
@DeviceProperties(name = "Optical Tracking Sensor PAA5100/SC18IS602B", description = "Optical Tracking sensor from Marcel", xmlTag = "OPTICAL_TRACKING_PAA5100", compatibleControlSystems = ControlSystem.REV_HUB)
public class UltrasonicDistanceSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> {

    private final static I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x2F);

    /** @noinspection FieldCanBeLocal*/
    private final int DISTANCE_READ_COMMAND = 0x01;

    public UltrasonicDistanceSensor(I2cDeviceSynch i2cDeviceSynch, boolean deviceClientIsOwned) {
        super(i2cDeviceSynch, deviceClientIsOwned);

        // Register our device at this I2C address:
        this.deviceClient.setI2cAddress(ADDRESS_I2C_DEFAULT);

        // Register callback for USB cables getting unplugged:
        super.registerArmingStateCallback(false);

        // We no longer need to change stuff like the I2C address, so engage:
        this.deviceClient.engage();
    }

    @Override
    protected boolean doInitialize() {
        return true; // Success!
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.SparkFun;
    }

    @Override
    public String getDeviceName() {
        return "Ultrasonic Distance Sensor - SparkFun TCT40";
    }

    // Read the result of the last probe, and trigger a new one:
    public int triggerAndRead() {
        byte[] distance = this.deviceClient.read(DISTANCE_READ_COMMAND, 2);
        return (distance[0] << 8) | distance[1];
    }
}