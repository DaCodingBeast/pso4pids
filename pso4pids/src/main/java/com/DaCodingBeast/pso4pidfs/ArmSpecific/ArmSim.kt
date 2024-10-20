package ArmSpecific

import ArmSpecific.pso4Arms.Constants.Constants
import CommonUtilities.AngleRange
import CommonUtilities.Models
import CommonUtilities.PIDFParams
import CommonUtilities.PIDFcontroller
import kotlin.math.abs

/**
 * An Enum Class Containing the two Directions the Arm can run in
 */
enum class Direction {
    Clockwise, CounterClockWise
}

/**
 *The simulation that translates the effect of [PIDFcontroller] on the Arm Angle
 */
class ArmSim(
    private var angleRange: AngleRange,
    private val badAngleRange: AngleRange?,
    params: PIDFParams
) {
    var angularVelocity = 0.0
    private var angularAcceleration = 0.0
    private var shoulder = PIDFcontroller(params)
    private var Ttotal = 0.0

    /**
     * This function calculates the sum of two integers.
     * @return Arms Angle, Error, and motor power
     */
    fun updateSim(): Triple<AngleRange, Double, Double> {
        val calculate = shoulder.calculate(angleRange, badAngleRange)
        val controlEffort = calculate.motorPower

        /**
         * @see Models.calculateTmotor
         * @see Models.gravityTorque
         */

        //todo no way - possibly gravity test to serious? NOPE
        Ttotal = if(angleRange.start >0 ) Models.calculateTmotor(controlEffort) - Models.gravityTorque(abs(angleRange.start))
        else Models.calculateTmotor(controlEffort) + Models.gravityTorque(abs(angleRange.start))


        angularAcceleration = Ttotal / Constants.Inertia
        angularVelocity += angularAcceleration * Dt
        angleRange = AngleRange(
            AngleRange.wrap(angleRange.start + angularVelocity * Dt),
            angleRange.target
        )
//        println("d: $direction  c: $controlEffort  v: $angularVelocity a: ${angleRange.start}")
        return Triple(angleRange, controlEffort, calculate.error)
    }


}