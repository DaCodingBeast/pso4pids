package ArmSpecific

import CommonUtilities.Models

/**
 * The Constants needed to Simulate the Arm Mechanism
 * @param RPM The Motors Actual RPM, accounting for the affect of friction
 * @param gravityConstants [gravityConstants]
 * @param motor [motor]
 * @param Inertia The Inertia of the System, measured in the Inertia OpMode
 * @see GravityModelConstants
 * @see Hardware.Motor
 */
data class SystemConstants(
    val RPM: Double,
    val motor: Hardware.Motor,
    val gravityConstants: GravityModelConstants,
    val Inertia: Double
) {
    init {
        require(RPM in (motor.rpm / 2).toDouble()..motor.rpm.toDouble()) {
            println("Friction Test: RPM is incorrect - Redo Friction Opmode")
        }
        pso4Arms.Constants.Constants = this
        val maxMotorTorque = Models.calculateTmotor(1.0, motor, RPM)
        val maxGravityTorque = Models.gravityTorque(gravityConstants.b)
        require(maxGravityTorque in (.0005* maxMotorTorque).. (.9* maxMotorTorque)){
            println("Torque of Gravity $maxGravityTorque is too high/low - Redo GravityTest Opmode")
        }
    }
}

/**
 * The Constants of a quadratic function that models gravity's effective torque on the Arm
 */
data class GravityModelConstants(val a: Double, val b: Double, val k: Double)