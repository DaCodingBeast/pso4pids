package com.DaCodingBeast.pso4pidfs.ArmSpecific

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
        pso4Arms.Constants = this
    }
}

/**
 * The Constants of a quadratic function that models gravity's effective torque on the Arm
 */
data class GravityModelConstants(val a: Double, val b: Double, val k: Double)