package com.example.weiboxx.ui.video

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import com.example.weiboxx.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.Url


class PlayerFragment (private val url: String ): Fragment() {
    private val mediaPlayer= MediaPlayer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progressBar : ProgressBar= view.findViewById(R.id.progressBar)
        val progressBarH : ProgressBar = view.findViewById(R.id.progressBarH)
        val surfaceView : SurfaceView = view.findViewById(R.id.surfaceView)
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer.apply {
            setOnPreparedListener{
                progressBarH.max=mediaPlayer.duration
//                it.start()
                seekTo(1)
                progressBar.visibility= View.INVISIBLE
            }
            setDataSource(url)
            prepareAsync()
            progressBar.visibility= View.VISIBLE
        }

        lifecycleScope.launch{
            while (true){
                progressBarH.progress = mediaPlayer.currentPosition
                delay(500)
            }

        }

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                mediaPlayer.setDisplay(p0)
                mediaPlayer.setScreenOnWhilePlaying(true)

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}

            override fun surfaceCreated(holder: SurfaceHolder)  {}

        })
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
        lifecycleScope.launch{//守护进程，确保执行
            while (!mediaPlayer.isPlaying){
                mediaPlayer.start()
                delay(500)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }
}