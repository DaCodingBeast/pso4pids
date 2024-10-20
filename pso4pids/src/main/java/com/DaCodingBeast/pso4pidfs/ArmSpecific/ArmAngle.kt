package ArmSpecific

import CommonUtilities.AngleRange

class ArmAngle (val motor: Hardware.Motor, private val angleOffset: Double) {
    fun findAngle(encoder: Int): Double {
        val angle = AngleRange.wrap(encoder * (2 * Math.PI / motor.encoderTicksPerRotation))
        return AngleRange.wrap(angle + angleOffset)
    }
}