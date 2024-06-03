package com.cavalcantibruno.flashlight

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.cavalcantibruno.flashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var cameraState:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding)
        {
            btnOnOff.setOnClickListener {
                turnFlashLightOnOff()
            }

            /*Listener to work with the seekbar that controls the flash intensity, on SDK versions
            superior or equal than the TIRAMISU*/
            lightIntensity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    /* 0 -> Back Camera ; 1 -> Front Camera */
                    val cameraId = cameraManager.cameraIdList[0]
                    if (seekBar != null) {
                        cameraManager.turnOnTorchWithStrengthLevel(cameraId,(seekBar.progress+1))
                        cameraState = true
                    }
                }
                //Function that get the result when the user touch the seekbar
                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                //Function that get the result when the user releases the seekbar
                override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

            })

        }
    }

    private fun turnFlashLightOnOff()
    {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if(!cameraState)
            try {
                cameraManager.setTorchMode(cameraId,true)
                cameraState = true

            }catch (e:Exception)
            {
                e.printStackTrace()
                Log.i("FlashLight", "turnFlashLightOnOff: ${e.message}")
            }
        else {
            cameraManager.setTorchMode(cameraId,false)
            cameraState = false
        }
    }

}