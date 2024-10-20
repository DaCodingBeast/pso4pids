package com.DaCodingBeast.pso4pidfs.PSO_Algorithm

import com.DaCodingBeast.pso4pidfs.ArmSpecific.FitnessFunction
import CommonUtilities.AngleRange
import kotlin.random.Random.Default.nextDouble


/**
 * PSO Simulator, that updates the swarms best position, and provides it to the particles.
 * This effectively moves the swarm towards the better performing PIDF Coefficients
 * @param swarmSize The number of Particles
 * @param randomInRanges The Ranges of the PIDF Coefficients
 * @param angleRange Start and Target Angles
 * @param obstacleAngleRange Obstacle
 * @param multiplePidfs Allows the use of Multiple PIDF Controllers at once
 * @param pBestWeight The Impact of the Particles prev position on particle's new velocity
 * @param prevVeloWeight The Impact of the Particles previous velocity on particle's new velocity
 * @param gBestWeight The Impact of the Swarms best position on the particle's new velocity
 */
//decrease previous when running a ton of swarm

//todo have a power paramter, that saves previous power or is null
class PSO(
    swarmSize: Int,
    private val randomInRanges: ArrayList<Pair<Double, Double>>,
    time: Double,
    angleRange: AngleRange,
    obstacleAngleRange: AngleRange? = null,
    private val multiplePidfs: Boolean = false,
    private val prevVeloWeight: Double = 0.01,
    private val pBestWeight: Double = .02,
    private val gBestWeight: Double = .9
) {
    private val particles = Array(swarmSize) { Particle(randomInRanges, multiplePidfs) }

    //initialize
    private var gBestResult = Double.MAX_VALUE
    var gBestParams = Vector(randomInRanges.map { nextDouble(it.first, it.second) }.toDoubleArray())

    /**
     * The fitness function you are using
     */
    private var fitnessFunction = FitnessFunction(time, angleRange, obstacleAngleRange)

    var lastPower = 0.0

    fun update(a: Int): Double {
        (0 until a).forEach { b ->
            for (particle in particles) {
                val fitness = fitnessFunction.computeParticle(particle)
                lastPower = fitness.second[2].second
                //PBEST
                if (particle.bestResult > fitness.first) {
                    particle.bestResult = fitness.first
                    particle.pBestParam = particle.position
//                    if(b>0) println(" fdfdf${particle.bestResult} ${fitness.first}")
                }
                //GBEST
                if (gBestResult > particle.bestResult) {
                    gBestResult = particle.bestResult
                    gBestParams = particle.pBestParam
                    (0 until fitness.second.size).forEach { c ->
                        println(" motor power: ${fitness.second[c].second}")
                        println(" angle: ${Math.toDegrees(fitness.second[c].first)}")
//                            println(" error: ${Math.toDegrees(fitness.second[c].third)}")
                    }
                }

                var newVelocity = ((particle.velocity * prevVeloWeight)+
                    ((particle.pBestParam - particle.position) * pBestWeight * nextDouble())+
                    ((gBestParams - particle.pBestParam) * gBestWeight * nextDouble()))

//                println("o ${particle.position}")

                /**
                 * Make sure all params are positive
                 */

                var counter = 0
                while((particle.position+newVelocity).isNegative()){
                    counter+=1
                    newVelocity = ((particle.velocity * prevVeloWeight)+
                            ((particle.pBestParam - particle.position) * pBestWeight * nextDouble())+
                            ((gBestParams - particle.pBestParam) * gBestWeight * nextDouble()))

                    //todo weird error I have not found the answer to, so just in case, force a positive value

                    if(counter>100) newVelocity = Vector(doubleArrayOf(0.0,0.0,0.0,0.0))
//                    if((particle.position+ newVelocity).isNegative()) println("H${particle.position} j$newVelocity")
                }

                particle.velocity = newVelocity
                particle.position += particle.velocity
//                println("i ${particle.position}")

            }
            println("ran ${b + 1} times")


        }
        return lastPower
    }
}
//only have prints be the global best at the end