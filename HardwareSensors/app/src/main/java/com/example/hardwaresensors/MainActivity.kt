package com.example.hardwaresensors

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorEventListener: SensorEventListener
    lateinit var sensorManager: SensorManager
    lateinit var proxSensor: Sensor
    lateinit var accelSensor: Sensor

    val colors = arrayOf(Color.BLACK, Color.BLUE, Color.GRAY, Color.GREEN, Color.MAGENTA, Color.RED, Color.YELLOW, Color.CYAN, Color.LTGRAY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE)!! as SensorManager
//        if(sensorManager == null){
//            Toast.makeText(this,"Could not get sensors",Toast.LENGTH_SHORT).show()
//            finish()// to finish the activity
//        }else{
//            val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
//            sensors.forEach {
//                Log.d(
//                        "HWSENS",
//                        """
//                            ${it.name} | ${it.stringType} | ${it.vendor}
//                        """.trimIndent()
//                )
//            }
//        }
        proxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)// LINEAR_ACCELEROMETER gives the value of
        // ACCELEROMETER minus value of g(9.8 m/s^2). Linear accelerometer is used when motion is going to be in only one direction.
        // Eg. when a person is running
        sensorEventListener = this
    }

    // Override Functions of SensorEventListener
    override fun onSensorChanged(event: SensorEvent?) {
        // Proximity Sensors
//        if (event!!.values[0] > 0){
//            flProxIndicator.setBackgroundColor(colors[Random.nextInt(9)]) // To change color randomly. Will give us random
//        // values from 0 to 6, we can also write size of colors array. So, take any random color from array and set it as the
//        // background color whenever the sensor value changes
//        }

        // Accelerometer Sensors
        val bgColor = Color.rgb(
            accel2color(event!!.values[0]),
            accel2color(event!!.values[1]),
            accel2color(event!!.values[2])
        )
        flAccelIndicator.setBackgroundColor(bgColor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // nothing
    }

    private fun accel2color(accel: Float) : Int = (((accel + 12) / (24)) * 255).toInt()
    //(((accel + accelSensor.maximumRange) / (2*accelSensor.maximumRange)) * 255).toInt()

    // When our app is on screen, only then sensor listening work is going to happen
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            sensorEventListener,
            accelSensor,//Sensor on which we want to listen from
            1000 * 1000 //Sampling period is how many times you want to observe the value of the sensors. The rate
            // at which sensor events are delivered. This is just a hint to the os. Events may be received faster or slower than
            // the specified rate. Usually events are received faster.
        )
    }

    override fun onPause() {
        sensorManager.unregisterListener(sensorEventListener)// When the app is paused, we unregister listener
        super.onPause()
    }

}