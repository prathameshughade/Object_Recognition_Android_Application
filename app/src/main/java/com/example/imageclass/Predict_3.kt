package com.example.imageclass

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.imageclass.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.*

class Predict_3 : AppCompatActivity() {

    lateinit var bitmap: Bitmap
    lateinit var imgview:ImageView
    lateinit var mTTS:TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)
        var speakBtn: Button = findViewById(R.id.speakBtn)
        var next2: Button = findViewById(R.id.next2)
        imgview = findViewById(R.id.imageView)
        var tv:TextView = findViewById(R.id.textView)
        var select: Button = findViewById(R.id.btSelect)
        val fileName ="label.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }

        var townList = inputString.split("\n")
        mTTS = TextToSpeech(applicationContext,TextToSpeech.OnInitListener { status ->
            if(status != TextToSpeech.ERROR){
                mTTS.language = Locale.UK
            }
        })

        next2.setOnClickListener {
            val editText = findViewById<TextView>(R.id.textView)
            val message = editText.text.toString()
            val intent = Intent(this,Trans_4::class.java).apply {
                putExtra(EXTRA_MESSAGE,message)
            }

            startActivity(intent)
        }

        speakBtn.setOnClickListener {
            val toSpeak = tv.text.toString()
            if(toSpeak==""){
                Toast.makeText(this,"Please select image first",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,toSpeak,Toast.LENGTH_LONG).show()
                mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }



        select.setOnClickListener(View.OnClickListener {


            var intent:Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"

            startActivityForResult(intent,100)
        })

        var predict:Button = findViewById(R.id.idPredict)
        predict.setOnClickListener(View.OnClickListener {

            var resized: Bitmap = Bitmap.createScaledBitmap(bitmap,224,224,true)
            val model = MobilenetV110224Quant.newInstance(this  )
            next2.isEnabled=true


// Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer
            inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            var max = getMax(outputFeature0.floatArray)

            tv.setText(townList[max])


// Releases model resources if no longer used.
            model.close()

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        imgview.setImageURI(data?.data)

        var uri: Uri?= data?.data

        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }

    fun getMax(arr:FloatArray) :Int{
        var ind = 0
        var min = 0.0f
        for (i in 0..1000)
        {
            if (arr[i]>min)
            {
                ind = i
                min = arr[i]
            }
        }
        return ind
    }
    override fun onDestroy() {
        if(mTTS != null){
            mTTS.stop()
            mTTS.shutdown()
        }
        super.onDestroy()
    }




}



