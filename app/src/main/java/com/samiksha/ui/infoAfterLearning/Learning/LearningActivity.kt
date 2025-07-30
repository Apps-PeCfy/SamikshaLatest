package com.samiksha.ui.infoAfterLearning.Learning

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.util.EventLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.samiksha.databinding.ActivityLearningBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.infoAfterLearning.questionstart.QuestionStartACtivity
import com.samiksha.utils.SessionManager

class LearningActivity : AppCompatActivity() {
    lateinit var binding: ActivityLearningBinding
    private var context: Context = this
    private var learningVideoModel: SkillDetailsResponsePOJO.DataItem? = null
    private var benefitsAdapter: BenefitsAdapter? = null
    private var list: List<String> = ArrayList()
    private var isPlayedOnce : Boolean = false

    private var player: ExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectionParameters: DefaultTrackSelector.Parameters? = null
    private var eventListener: Player.Listener? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val display_mode = resources.configuration.orientation
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {

            binding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            binding.toolbar.visibility = View.GONE
            binding.rlVideoListToolbar.visibility = View.GONE
            binding.imgMinimizeScreen.visibility = View.VISIBLE
            binding.imgFullScreen.visibility = View.GONE

        }
        init()
    }

    private fun init() {
        supportActionBar?.hide()
        learningVideoModel =
            SessionManager.getInstance(context).getSkillDetailModel()?.learningVideoModel

        binding.txtTitle.text = learningVideoModel?.group
        binding.txtName.text = learningVideoModel?.name
        binding.txtDescription.text = learningVideoModel?.description
        list = learningVideoModel?.benefits!!
        setListeners()

        setRecyclerView()

        if (learningVideoModel?.file.isNullOrEmpty()){
            Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show()
        }else{
            initializePlayer()
        }

    }

    private fun setRecyclerView() {
        benefitsAdapter =
            BenefitsAdapter(context, list, object : BenefitsAdapter.BenefitsAdapterInterface {
                override fun onItemSelected(position: Int, name: String) {

                }

            })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = benefitsAdapter
    }

    private fun initializePlayer() {
        trackSelectionParameters = DefaultTrackSelector.ParametersBuilder( this).build()
        // clearStartPosition()

        val preferExtensionDecoders =
            intent.getBooleanExtra("prefer_extension_decoders", false)
        val renderersFactory: RenderersFactory = buildRenderersFactory( context, preferExtensionDecoders)

        trackSelector = DefaultTrackSelector( context)

        //Initialize the player
        player = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            //.setMediaSourceFactory(createMediaSourceFactory())
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .setTrackSelector(trackSelector!!)
            .build()

        eventListener = object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    if (player?.playWhenReady == true){
                        isPlayedOnce = true
                    }
                    binding.progressBar.visibility = View.GONE
                } else if (playbackState == Player.STATE_ENDED && isPlayedOnce) {
                    fireEvent()
                    releasePlayer()
                    val intent = Intent(applicationContext, QuestionStartACtivity::class.java)
                    startActivity(intent)
                    finish()
                }else if (playbackState == Player.STATE_ENDED && !isPlayedOnce) {
                    Toast.makeText(context, "Something went wrong might be your network is slow please try after some time!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                    player?.seekToDefaultPosition()
                    player?.prepare()
                }
            }




        }

        player?.addListener(eventListener!!)
        player?.trackSelectionParameters = trackSelectionParameters!!
        player?.addAnalyticsListener(EventLogger(trackSelector))
        player?.setAudioAttributes(AudioAttributes.DEFAULT,   true)
        player?.playWhenReady = true
        binding.videoView.player = player
        startPlayer()
    }

    private fun buildRenderersFactory(
        context: Context, preferExtensionRenderer: Boolean
    ): RenderersFactory {
        val extensionRendererMode =
            if (false) if (preferExtensionRenderer) DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON else DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        return DefaultRenderersFactory(context.applicationContext)
            .setExtensionRendererMode(extensionRendererMode)
    }

    private fun startPlayer() {
        try {
            if (learningVideoModel != null) {
                val extension: String =
                    learningVideoModel?.file?.substring(learningVideoModel?.file?.lastIndexOf(".")!!)!!
                if (extension.equals(".mp4", ignoreCase = true)) {
                    if (learningVideoModel?.file != null) {
                        val videoUri =
                            Uri.parse(learningVideoModel?.file)

                        val defaultDataSourceFactory = DefaultHttpDataSource.Factory()
                        val videoMediaItem = MediaItem.Builder().setUri(videoUri)

                        val videoSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory).setLoadErrorHandlingPolicy(
                            DefaultLoadErrorHandlingPolicy(16)
                        ).createMediaSource(videoMediaItem.build())

                        player?.run {
                            setMediaSource(videoSource)
                            setSeekParameters(SeekParameters.EXACT)
                            play()
                            prepare()
                        }

                    }
                }
            }
        } catch (e: Exception) {  // exception management is not implemented in this demo code
            // Auto-generated catch block
            e.printStackTrace()
        }
    }

    private fun setListeners() {
        binding.apply {

            imgBack.setOnClickListener {
                releasePlayer()
                finish()
            }

            imgFullScreen.setOnClickListener {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (player != null) {
                        SessionManager.getInstance(context).setPlayerPosition(player!!.currentPosition)
                    }
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    SessionManager.getInstance(context).setPlayerPosition(player!!.currentPosition)
                  }

            }

            imgMinimizeScreen.setOnClickListener {
                binding.imgMinimizeScreen.visibility = View.GONE
                binding.imgFullScreen.visibility = View.VISIBLE
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (player != null) {
                        SessionManager.getInstance(context).setPlayerPosition(player!!.currentPosition)
                    }
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    SessionManager.getInstance(context).setPlayerPosition(player!!.currentPosition)
                    finish()
                }

            }

            imgPlayButton.setOnClickListener {
                startPlayer()
                imgPlayButton.visibility = View.GONE
            }

            binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    override fun onResume() {
        super.onResume()
        if (player != null) {
            playPlayer()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            pausePlayer()
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }


    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.playWhenReady = false
            player!!.playbackState
        }
    }

    private fun playPlayer() {
        if (player != null) {
            player!!.playWhenReady = true
            player!!.playbackState
        }
    }

    private fun isPlaying(): Boolean {
        return if (player != null) {
            player!!.playbackState == Player.STATE_READY && player!!.playWhenReady
        } else false
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (player != null) {
                SessionManager.getInstance(context).setPlayerPosition(player!!.currentPosition)
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            SessionManager.getInstance(context).setPlayerPosition(player!!.currentPosition)
            releasePlayer()
            finish()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCode && data != null) {
            val lastPlayPosition = data.getLongExtra("last_play_position", 0)
            if (player != null) {
                player!!.seekTo(lastPlayPosition)
            }
        }
    }


    /* override fun onBackPressed() {
         ProjectUtils.showAlertDialog(this)
     }*/

    private fun fireEvent() {
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)


        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, learningVideoModel?.id.toString())
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, learningVideoModel?.name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text")

        mFirebaseAnalytics.logEvent("video_watched", bundle)
    }

}
