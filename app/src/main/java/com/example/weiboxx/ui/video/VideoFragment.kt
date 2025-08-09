package com.example.weiboxx.ui.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weiboxx.R

private val videoUrls=listOf<String>(
    "https://youtube.com/shorts/FEJU2ir5EZw?si=tbTpvVeUSOnMgxda",
    "https://youtube.com/shorts/4L2KpxiL2ZI?si=kJz7BLpmqZcAM8Cg",
    "https://youtube.com/shorts/4L2KpxiL2ZI?si=CuMjZnFMB5YFIv8i",
    "https://youtube.com/shorts/8coxeTEs8wk?si=gM5aTgkiEaXUwdm9",
    "https://youtube.com/shorts/gmciNQEW1Xk?si=u8wGhaimFVTC5nAB",
    "https://youtube.com/shorts/vPMsx93c59w?si=jcf5BhQV0LKc-Bfp",
    "https://youtube.com/shorts/DxvaQ0Rlxqc?si=TybiVcans_FW9-GD",
    "https://youtube.com/shorts/Wo4DiD1Kmhk?si=y5QEQQWch-EBt3Do",
    "https://youtube.com/shorts/giFTV37-X7o?si=I3jg317TFhR3gqxn",
    "https://youtube.com/shorts/lz1gAs4UFlA?si=6wOxQO-lmiBpYYoX"
)

class VideoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoViewPager: ViewPager2 = view.findViewById(R.id.videoViewPager)
        videoViewPager.apply {
            adapter = object : FragmentStateAdapter (this@VideoFragment){
                override fun getItemCount()=videoUrls.size

                override fun createFragment(position: Int)= PlayerFragment(videoUrls[position])

            }
            offscreenPageLimit = 5
        }
    }
}