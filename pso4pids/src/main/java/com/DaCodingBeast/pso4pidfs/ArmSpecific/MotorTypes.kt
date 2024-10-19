package ArmSpecific

/**
 * A collection of Motor Brands and characteristics
 */
sealed class Hardware {
    /**
     * Yellow Jacket Motors ranging from 84 - 1150 RPM
     */
    object YellowJacket {
        @JvmField val RPM84 = Motor(84.0,	1993.6,93.6)
        @JvmField val RPM117 = Motor(117.0, 1425.1, 68.4)
        @JvmField val RPM223 = Motor(223.0, 751.8, 38.0)
        @JvmField val RPM312 = Motor(312.0, 537.7, 24.3)
        @JvmField val RPM435 = Motor(435.0, 384.5, 18.7)
        @JvmField val RPM1150 = Motor(1150.0, 145.1, 7.9)
    }

    /**
     * Holds the necessary specs needed for this simulation, all which can be found on the vendor's website
     * @param rpm Theoretical rpm
     * @param stallTorque The motors maximum Torque
     * @param customGearRatio Any gear conversions that need to be considered
     * Gear ratio is in the form of a fraction: (Motor gear teeth) / (Arm Gear Teeth)
     */

    data class Motor(var rpm: Double, var encoderTicksPerRotation: Double, var stallTorque: Double, var customGearRatio: Double = 1.0){
        init {
            if(customGearRatio != 0.0){
                rpm *= customGearRatio
                encoderTicksPerRotation *= (1/customGearRatio)
                stallTorque *= (1/customGearRatio)
            }
        }
    }
}
