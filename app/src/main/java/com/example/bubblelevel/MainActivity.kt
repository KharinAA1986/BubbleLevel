package com.example.bubblelevel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val radToDegree = 57.2958f
    lateinit var sManager: SensorManager
    private var magnetic = FloatArray(9)
    private var gravity = FloatArray(9)

    private var accelerometer = FloatArray(3)
    private var magneticField = FloatArray(3)
    private var values = FloatArray(3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSensor = findViewById<TextView>(R.id.tv_degree)
        val lLevel = findViewById<LinearLayout>(R.id.lvl)
        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorAccelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorMagnetic = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val slistener = object: SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                when(event?.sensor?.type) {
                    Sensor.TYPE_ACCELEROMETER ->accelerometer = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD ->magneticField = event.values.clone()
                }
                SensorManager.getRotationMatrix(gravity,magnetic,accelerometer,magneticField)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(
                    gravity,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    outGravity
                )
                SensorManager.getOrientation(outGravity,values)
                val degree = values[2] * radToDegree +90
                lLevel.rotation = degree
                tvSensor.text = degree.toString()
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }
        sManager.registerListener(slistener,sensorAccelerometer,SensorManager.SENSOR_DELAY_NORMAL)
        sManager.registerListener(slistener,sensorMagnetic,SensorManager.SENSOR_DELAY_NORMAL)
    }
}