package com.samiksha.ui.downloads

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.util.EventLogger
import com.google.gson.Gson
import com.samiksha.databinding.ActivityDownloadedTrainingBinding
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.utils.Constants
import com.samiksha.utils.DownloadUtils
import fr.maxcom.http.LocalSingleHttpServer
import fr.maxcom.libmedia.Licensing
import java.io.File
import java.io.IOException
import java.security.GeneralSecurityException
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DownloadedTrainingActivity : AppCompatActivity() {

    private var context : Context = this
    lateinit var binding: ActivityDownloadedTrainingBinding

    private var player: ExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectionParameters: DefaultTrackSelector.Parameters? = null
    private var eventListener: Player.Listener? = null

    private var mServer: LocalSingleHttpServer? = null

    private var skillModel : SkillModel ?= null
    private var skillDetail : SkillDetailsResponsePOJO.Data ?= null


    private var mediaPlayer: MediaPlayer? = null
    private var startTime = 0.0
    private var finalTime = 0.0
    private var oneTimeOnly = 0
    private var isPause = false
    private val myHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadedTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        Licensing.allow(applicationContext)
        skillModel = Gson().fromJson(intent.getStringExtra("skill_model"), SkillModel::class.java)
        skillDetail = Gson().fromJson(skillModel?.jsonData, SkillDetailsResponsePOJO.Data::class.java)
        setListeners()
      //  initializePlayer()
        getSongAndPlay()
    }

    private fun setListeners() {
        binding.apply {
            imgBackArrow.setOnClickListener {
               onBackPressed()
            }
        }
    }

    private fun initializePlayer() {

        trackSelectionParameters = DefaultTrackSelector.ParametersBuilder( this).build()

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
                if (playbackState == Player.STATE_BUFFERING){
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY){
                    binding.progressBar.visibility = View.GONE
                } else if (playbackState == Player.STATE_ENDED) {
                    startTrainingFeedback()
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

    private fun startTrainingFeedback() {
        startActivity(Intent(context, DownloadedTrainingFeedbackActivity::class.java).putExtra("skill_model", Gson().toJson(skillModel)))
        finish()
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
        /*    mServer = LocalSingleHttpServer()
            val c: Cipher = getCipher()!!
            if (c != null) {  // null means a clear video ; no need to set a decryption processing
                mServer?.setCipher(c)
            }
            mServer?.start()*/

            if (skillModel!= null) {
                var filePath: String = DownloadUtils.fetchStoragePath(context).toString() + "${Constants.FOLDER_NAME}/${skillModel?.user_id}/${skillDetail?.name}/${skillModel?.fileName}"

                val topicFile = File(filePath)
                if (topicFile.exists()) {
                  //
                      val path: String? = mServer?.getURL(topicFile.path)
                    //val path: String? = mServer?.getURL(skillDetail?.subSkiils!![1].data?.get(0)?.file)

                    // This is the MediaSource representing the media to be played.
                    val videoUri = Uri.parse(topicFile.path)

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
        } catch (e: Exception) {  // exception management is not implemented in this demo code
            // Auto-generated catch block
            e.printStackTrace()
        }
    }



    /**
     * DECRYPT
     */
    @Throws(GeneralSecurityException::class)
    fun getCipher(): Cipher? {
        val initialIV = ByteArray(16)
        return rebaseCipher(initialIV)
    }


    @Throws(GeneralSecurityException::class)
    fun rebaseCipher(iv: ByteArray?): Cipher? {
        var c: Cipher? = null
        val password: String = Constants.CIPHER_PASSWORD
        c = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC")
        c.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(password.toByteArray(), "AES")
        )
        return c
    }



    /**
     * Media Player Functionality
     */

    private fun getSongAndPlay(): Unit {
        //Check For Current Song Text Color to changed
        binding.songProgress.visibility = View.VISIBLE
        binding.imgPause.visibility = View.GONE
        binding.imgPlay.visibility = View.GONE

        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }

        mediaPlayer?.reset()
        oneTimeOnly = 0

        if (skillModel!= null) {
            var filePath: String = DownloadUtils.fetchStoragePath(context).toString() + "${Constants.FOLDER_NAME}/${skillModel?.user_id}/${skillDetail?.name}/${skillModel?.fileName}"

            val topicFile = File(filePath)
            if (topicFile.exists()) {

                val url: String = topicFile.path // your URL here

                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                try {
                    mediaPlayer?.setDataSource(url)
                    mediaPlayer?.prepareAsync() // might take long! (for buffering, etc)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


        setMediaListeners()

    }

    private fun setMediaListeners() {
        binding.apply {
            mediaPlayer?.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
                mp.start()
                binding.songProgress.visibility = View.GONE
                binding.imgPlay.visibility = View.GONE
                binding.imgPause.visibility = View.VISIBLE
                seekBarFunctionality()
            })
            mediaPlayer?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                binding.imgPlay.visibility = View.VISIBLE
                binding.imgPause.visibility = View.GONE

                startTrainingFeedback()


            })
            mediaPlayer?.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
                Toast.makeText(context, "Song not available or currupted", Toast.LENGTH_SHORT)
                    .show()
                false
            })


            imgNext.setOnClickListener {

                mediaPlayer!!.seekTo(mediaPlayer?.currentPosition?.plus(10000)!!)

            }

            imgPrevious.setOnClickListener {
                mediaPlayer!!.seekTo(mediaPlayer?.currentPosition?.minus(10000)!!)
            }



            imgPause.setOnClickListener {
                if (mediaPlayer!!.isPlaying) {
                    isPause = true
                    mediaPlayer?.pause()
                    imgPlay.visibility = View.VISIBLE
                    imgPause.visibility = View.GONE
                    songProgress.visibility = View.GONE
                }
            }

            imgPlay.setOnClickListener {
                if (isPause) {
                    isPause = false
                    mediaPlayer?.start()
                    imgPlay.visibility = View.GONE
                    imgPause.visibility = View.VISIBLE
                    songProgress.visibility = View.GONE
                } else if (skillModel!= null) {
                    getSongAndPlay()
                } else {
                    Toast.makeText(context, "Please select song to play!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.seekTo(seekBar.progress)
                    }
                }
            })
        }

    }

    private fun seekBarFunctionality() {
        finalTime = mediaPlayer?.duration?.toDouble()!!
        startTime = mediaPlayer?.currentPosition?.toDouble()!!
        if (oneTimeOnly == 0) {
            binding.seekbar.max = finalTime.toInt()
            oneTimeOnly = 1
        }
        binding.seekbar.progress = startTime.toInt()
        myHandler.postDelayed(updateSongTime, 100)
        binding.txtEndTime.text = String.format(
            "%d:%02d ",
            TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            finalTime.toLong()
                        )
                    )
        )
        binding.txtStartTime.text = String.format(
            "%d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            startTime.toLong()
                        )
                    )
        )
    }

    private val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null && mediaPlayer?.currentPosition != null) {
                startTime = mediaPlayer?.currentPosition?.toDouble()!!
                binding.txtStartTime.text = String.format(
                    "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    startTime.toLong()
                                )
                            )
                )
                binding.seekbar.progress = startTime.toInt()
                myHandler.postDelayed(this, 100)

            }
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
            player?.release()
            player = null
        }

        if (mediaPlayer != null) {
            myHandler.removeCallbacks(updateSongTime)
            mediaPlayer?.release()
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
}