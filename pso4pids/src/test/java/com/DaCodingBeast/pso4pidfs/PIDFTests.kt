package com.example.pso4pid

import com.DaCodingBeast.pso4pidfs.ArmSpecific.Direction
import CommonUtilities.AngleRange
import com.DaCodingBeast.pso4pidfs.CommonUtilities.PIDFParams
import com.DaCodingBeast.pso4pidfs.CommonUtilities.PIDFcontroller
import org.junit.Test
import kotlin.math.PI
import kotlin.random.Random

class PIDFTests {

    /**
     * Testing PIDF error for Clockwise Rotation
     * Expected Outcome: Error is relative to Motor Direction
     */
    @Test
    fun clockWisePIDFError() {
        for (i in 0..100) {
            val angle = AngleRange(Random.nextDouble(-PI, PI), Random.nextDouble(-PI,PI))
            val actualError = AngleRange.findPIDFAngleError(Direction.Clockwise,angle)

            val wrappedAngle = AngleRange.wrap(angle.target - angle.start)

            if(wrappedAngle >0 ){
                assert(actualError in -2* PI .. 0.0){ println("$angle $wrappedAngle  $actualError") }
            }
            else assert(actualError in -PI ..0.0){ println("$angle $wrappedAngle  $actualError") }
        }
    }

    /**
     * Testing PIDF error for CounterClockwise Rotation
     * Expected Outcome: Error is relative to Motor Direction
     */
    @Test
    fun counterClockwisePIDFError() {
        for (i in 0..100) {
            val angle = AngleRange(Random.nextDouble(-PI, PI), Random.nextDouble(-PI,PI))
            val actualError = AngleRange.findPIDFAngleError(Direction.CounterClockWise,angle)

            val wrappedAngle = AngleRange.wrap(angle.target - angle.start)

            if(wrappedAngle <0 ){
                assert(actualError in 0.0 .. 2 * PI){ println("$angle $wrappedAngle  $actualError") }
            }
            else assert(actualError in 0.0..PI){ println("$angle $wrappedAngle  $actualError") }
        }
    }

    /**
     * Testing power returned by PIDF Controller
     * Expected Outcome: An error that is in between -1 to 1 (for motor)
     */
    @Test
    fun powerReturnedByController() {
        for (i in 0..100) {
            val randomNumbers = List(5) {Random.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)}
            val pidfController = PIDFcontroller(PIDFParams(randomNumbers[1],randomNumbers[2],randomNumbers[3],randomNumbers[4]))

            val angle = AngleRange(Random.nextDouble(-PI,PI),Random.nextDouble(-PI,PI))
            val motorPower = pidfController.calculate(angle, null).first

            assert(motorPower in -1.0 .. 1.0)
        }
    }

}