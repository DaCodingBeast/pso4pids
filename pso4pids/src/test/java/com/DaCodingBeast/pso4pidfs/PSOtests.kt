package com.example.pso4pid

import com.DaCodingBeast.pso4pidfs.PSO_Algorithm.Particle
import android.util.Log
import org.junit.Test
import kotlin.random.Random

class PSOtests {


    /**
     * Make sure that particles do not have negative pidf paramters, and that they always have velocities
     */
    @Test
    fun particleParamRanges(){
        for(i in 0..100){
            val ranges = arrayListOf(Pair(0.0, Random.nextDouble(0.0,Double.MAX_VALUE)))
            val particle = Particle(ranges, false)
            particle.velocity.particleParams.forEach { require(it==0.0){ Log.d(com.DaCodingBeast.pso4pidfs.ArmSpecific.error,"Dm Creator that this error occurred")} }
            particle.position.particleParams.forEach { require(it>0.0) {Log.d(com.DaCodingBeast.pso4pidfs.ArmSpecific.error,"Dm Arya that this error occurred")} }
        }
    }


}