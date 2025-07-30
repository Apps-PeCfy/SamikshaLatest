package com.samiksha.ui.infoAfterLearning.startTraining

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultLoadErrorHandlingPolicy
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.samiksha.R
import com.samiksha.database.LocalCrudRepository
import com.samiksha.databinding.ActivityTrainingStartBinding
import com.samiksha.fcm.DownloadBroadcastReceiverService
import com.samiksha.receiver.MyDownloadBroadCastReceiver
import com.samiksha.ui.downloads.SkillModel
import com.samiksha.ui.home.dealingWithDistraction.pojo.SkillDetailsResponsePOJO
import com.samiksha.ui.home.pojo.AllCategoriesResponsePOJO
import com.samiksha.ui.infoAfterLearning.Learning.BenefitsAdapter
import com.samiksha.ui.infoAfterLearning.trainingFeedback.TrainingFeedback
import com.samiksha.utils.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.TimeUnit

class TrainingStartActivity : AppCompatActivity() {

    lateinit var binding: ActivityTrainingStartBinding
    private var context: Context = this
    private var trainingAudioModel: SkillDetailsResponsePOJO.DataItem? = null
    private var skillDetail: SkillDetailsResponsePOJO.Data? = null
    private var trainingAudioModelImg: SkillDetailsResponsePOJO? = null
    private var allCategory: AllCategoriesResponsePOJO? = null

    private var player: ExoPlayer? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var trackSelectionParameters: DefaultTrackSelector.Parameters? = null
    private var eventListener: Player.Listener? = null
    var imgUrl: String? = null

    private var mediaPlayer: MediaPlayer? = null
    private var startTime = 0.0
    private var finalTime = 0.0
    private var oneTimeOnly = 0
    private val myHandler = Handler()
    private var isPause = false
    private var benefitsAdapter: BenefitsAdapter? = null
    private var list: List<String> = ArrayList()
    private var localCrudRepository: LocalCrudRepository? = null
    private var sessionManager: SessionManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingStartBinding.inflate(layoutInflater)
        val display_mode = resources.configuration.orientation
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {

            binding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            binding.toolbar.visibility = View.GONE
            binding.rlVideoListToolbar.visibility = View.GONE
            binding.imgMinimizeScreen.visibility = View.VISIBLE
            binding.imgFullScreen.visibility = View.GONE

        }
        setContentView(binding.root)
        initView()

    }


    private fun initView() {

        localCrudRepository = LocalCrudRepository.getInstance(context)
        sessionManager = SessionManager.getInstance(context)
        skillDetail = sessionManager?.getSkillDetailModel()?.skillDetail!!.data
        allCategory = sessionManager?.getAllCategories()
        if (skillDetail!!.moduleType.equals("Yoga")) {
            setData()
            binding.txtDetailLabel.visibility = View.GONE
            binding.txtDescription.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.txtBenefits.visibility = View.GONE
            binding.llDailyMotivation.visibility = View.VISIBLE
            binding.llYogaDescription.visibility = View.VISIBLE
            binding.txtDaily.visibility = View.VISIBLE
            binding.tvDailyMotivation.text = allCategory!!.dailyMotivation!!.motivation
            binding.tvMotivatorName.text = allCategory!!.dailyMotivation!!.by
        } else {
            binding.llDailyMotivation.visibility = View.GONE
            binding.txtDaily.visibility = View.GONE
            binding.llYogaDescription.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtDetailLabel.visibility = View.VISIBLE
            binding.txtDescription.visibility = View.VISIBLE
            binding.txtBenefits.visibility = View.VISIBLE


        }

        trainingAudioModel =
            sessionManager?.getSkillDetailModel()?.trainingAudioModel

        trainingAudioModelImg =
            sessionManager?.getSkillDetailModel()?.skillDetail

        if (trainingAudioModel != null && !trainingAudioModel?.file.isNullOrEmpty()) {
            if (trainingAudioModel?.type.equals("Video", ignoreCase = true)) {
                supportActionBar?.hide()

                val display_mode = resources.configuration.orientation
                if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.toolbar.visibility = View.GONE

                } else {
                    binding.toolbar.visibility = View.VISIBLE

                }


                //    binding.rlVideo.visibility = View.VISIBLE
                binding.rlAudio.visibility = View.GONE
                binding.txtDownloadMessage.visibility = View.GONE
                binding.imgDownload.visibility = View.GONE
                binding.txtDownload.visibility = View.GONE
                initializePlayer()
            } else {
                mediaPlayer = MediaPlayer()

                binding.toolbar.setTitleTextColor(Color.WHITE)
                setSupportActionBar(binding.toolbar)
                binding.rlVideo.visibility = View.GONE
                binding.rlAudio.visibility = View.VISIBLE
                checkForDownloadingStatus()



                Glide.with(this)
                    .load(trainingAudioModelImg!!.data!!.image)
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform()
                    ).into(binding.imgTop!!)


                val display_mode = resources.configuration.orientation
                if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.toolbar.visibility = View.GONE

                } else {
                    binding.toolbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayShowTitleEnabled(false);
                    binding.toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance)

                }

                getSongAndPlay()
            }
        } else {
            Toast.makeText(context, "Data not available", Toast.LENGTH_SHORT).show()
        }

        setListeners()
        setRecyclerView()
    }

    private fun checkForDownloadingStatus() {

        if (DownloadUtils.isFileDownloaded(context, skillDetail!!, trainingAudioModel?.file!!)) {
            binding.txtDownloadMessage.visibility = View.VISIBLE
            binding.txtDownloadMessage.text =
                "Please go to My Downloads in the Profile tab to follow training in offline mode."
            binding.imgDownload.visibility = View.GONE
            binding.txtDownload.visibility = View.VISIBLE
            binding.txtDownload.text = "Download Complete"
        } else {
            binding.txtDownloadMessage.visibility = View.VISIBLE
            binding.imgDownload.visibility = View.VISIBLE
            binding.txtDownload.visibility = View.GONE
            binding.txtDownloadMessage.text =
                "To access your training in offline mode please click on the download button."

        }


    }

    private fun setData() {
        binding.apply {
            txtDetailLabel.text = skillDetail?.name
            txtDescription.text = skillDetail?.description
            list = skillDetail!!.benefits
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

    private fun setListeners() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }

            imgFullScreen.setOnClickListener {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            imgMinimizeScreen.setOnClickListener {
                binding.imgMinimizeScreen.visibility = View.GONE
                binding.imgFullScreen.visibility = View.VISIBLE
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            }



            imgPlayButton.setOnClickListener {
                startPlayer()
                imgPlayButton.visibility = View.GONE
            }

            binding.toolbar.setNavigationOnClickListener { onBackPressed() }

            btnDone.setOnClickListener {
                releasePlayer()
                val intent = Intent(applicationContext, TrainingFeedback::class.java)
                startActivity(intent)
                finish()
            }

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
                } else if (trainingAudioModel != null) {
                    getSongAndPlay()
                } else {
                    Toast.makeText(context, "Please select song to play!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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

            imgDownload.setOnClickListener {
                if (ProjectUtils.checkInternetAvailable(this@TrainingStartActivity)!!) {
                    if (Build.VERSION.SDK_INT >= 34) {
                        if (CommonAppPermission.requestPermissionGrantedAbove14(context)) {
                            downLoadFile()
                        }
                    }else{
                        if (CommonAppPermission.requestPermissionGranted(context)) {
                            downLoadFile()
                        }
                    }

                } else {
                    ProjectUtils.showToast(
                        this@TrainingStartActivity,
                        getString(R.string.no_internet_connection)
                    )

                }
            }
        }

    }

    private fun downloadFunctionality(fileName: String) {
        var skillModel = localCrudRepository?.getSkillModelBySkillName(
            sessionManager?.getUserModel()?.id,
            skillDetail?.name
        )
        if (skillModel == null) {
            skillModel = SkillModel()
            skillModel.user_id = sessionManager?.getUserModel()?.id
            skillModel.jsonData = Gson().toJson(skillDetail)
            skillModel.type = skillDetail?.name
            skillModel.fileName = fileName

            localCrudRepository?.insertSkillModel(skillModel)
        } else {
            skillModel.jsonData = Gson().toJson(skillDetail)
            skillModel.fileName = fileName
            localCrudRepository?.updateSkillModel(skillModel)
        }

    }

    private fun initializePlayer() {

        trackSelectionParameters = DefaultTrackSelector.ParametersBuilder(this).build()
        // clearStartPosition()

        val preferExtensionDecoders =
            intent.getBooleanExtra("prefer_extension_decoders", false)
        val renderersFactory: RenderersFactory =
            buildRenderersFactory(context, preferExtensionDecoders)

        trackSelector = DefaultTrackSelector(context)

        //Initialize the player
        player = ExoPlayer.Builder(context)
            .setRenderersFactory(renderersFactory)
            //.setMediaSourceFactory(createMediaSourceFactory())
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .setTrackSelector(trackSelector!!)
            .build()

        eventListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.progressBar.visibility = View.GONE
                } else if (playbackState == Player.STATE_ENDED) {
                    releasePlayer()
                    val intent = Intent(applicationContext, TrainingFeedback::class.java)
                    startActivity(intent)
                    finish()
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
        player?.setAudioAttributes(AudioAttributes.DEFAULT, true)
        player?.playWhenReady = true
        binding.videoView.player = player

        //  startPlayer()
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
            if (trainingAudioModel != null) {
                val extension: String =
                    trainingAudioModel?.file?.substring(trainingAudioModel?.file?.lastIndexOf(".")!!)!!
                if (extension.equals(".mp4", ignoreCase = true)) {
                    if (trainingAudioModel?.file != null) {

                        val videoUri = Uri.parse(trainingAudioModel?.file)

                        val defaultDataSourceFactory = DefaultHttpDataSource.Factory()
                        val videoMediaItem = MediaItem.Builder().setUri(videoUri)

                        val videoSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                            .setLoadErrorHandlingPolicy(
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


    /**
     * Media Player Functionality
     */

    private fun getSongAndPlay(): Unit {
        //Check For Current Song Text Color to changed
        binding.songProgress.visibility = View.VISIBLE
        binding.imgPause.visibility = View.GONE
        binding.imgPlay.visibility = View.GONE

        mediaPlayer?.reset()
        oneTimeOnly = 0

        val url: String = trainingAudioModel?.file!! // your URL here

        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer?.setDataSource(url.replace(" ".toRegex(), "%20"))
            mediaPlayer?.prepareAsync() // might take long! (for buffering, etc)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mediaPlayer?.setOnPreparedListener(OnPreparedListener { mp ->
            mp.start()
            binding.songProgress.visibility = View.GONE
            binding.imgPlay.visibility = View.GONE
            binding.imgPause.visibility = View.VISIBLE
            seekBarFunctionality()
        })
        mediaPlayer?.setOnCompletionListener(OnCompletionListener {
            binding.imgPlay.visibility = View.VISIBLE
            binding.imgPause.visibility = View.GONE

            releasePlayer()
            val intent = Intent(applicationContext, TrainingFeedback::class.java)
            startActivity(intent)
            finish()


        })
        mediaPlayer?.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
            Toast.makeText(context, "Song not available or currupted", Toast.LENGTH_SHORT)
                .show()
            false
        })
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                Context.RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        }



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
        unregisterReceiver(onComplete)
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

    private fun isPlaying(): Boolean {
        return if (player != null) {
            player!!.playbackState == Player.STATE_READY && player!!.playWhenReady
        } else false
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (player != null) {
                sessionManager?.setPlayerPosition(player!!.currentPosition)
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            releasePlayer()
            sessionManager?.setPlayerPosition(0)
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

    /**
     * Download
     */

    private fun downLoadFile() {

        binding.imgDownload.visibility = View.GONE
        binding.txtDownload.visibility = View.VISIBLE
        binding.txtDownload.text = "Downloading..."
        binding.txtDownloadMessage.text =
            "To access your training in offline mode please click on the download button."


        var fileName = DownloadUtils.getFileNameFromUrl(trainingAudioModel?.file)
        downloadFunctionality(fileName)
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(trainingAudioModel?.file))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            request.setDestinationInExternalPublicDir(
                "${Constants.FOLDER_NAME}/${sessionManager?.getUserModel()?.id}/${skillDetail?.name}",
                fileName
            )
        } else {
            request.setDestinationInExternalFilesDir(
                context,
                "${Constants.FOLDER_NAME}/${sessionManager?.getUserModel()?.id}/${skillDetail?.name}",
                fileName
            )
        }
        request.setTitle(fileName)
        request.setDescription(
            DownloadUtils.fetchStoragePath(context)
                .toString() + "${Constants.FOLDER_NAME}/${sessionManager?.getUserModel()?.id}/${skillDetail?.name}"
        )

        val topicFile: File = File(
            DownloadUtils.fetchStoragePath(context)
                .toString() + "${Constants.FOLDER_NAME}/${sessionManager?.getUserModel()?.id}/${skillDetail?.name}/" + fileName
        )
        if (topicFile.exists()) {
            topicFile.delete()
        }
        val reference = downloadManager.enqueue(request)
        try {
            downloadManager.openDownloadedFile(reference)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun registerDownloadReceiver() {
        // to start a service
        val service = Intent(context, DownloadBroadcastReceiverService::class.java)
        context.startService(service)
        val br: BroadcastReceiver = MyDownloadBroadCastReceiver()
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        this.registerReceiver(br, filter)
    }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(ctxt: Context, intent: Intent) {
            val action = intent.action!!
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val query = DownloadManager.Query()
                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
                val manager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor = manager.query(query)
                val download_id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
                if (cursor.moveToFirst()) {
                    if (cursor.count > 0) {
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        val downloadTitle =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                        val downloadDescription =
                            cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            //   DownloadUtils.encryptFile(downloadDescription, downloadTitle)
                        } else {

                        }
                    }
                } else {

                }
                cursor.close()
                checkForDownloadingStatus()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= 34) {
            if (CommonAppPermission.requestPermissionGrantedAbove14(context)) {
                downLoadFile()
            }
        }else{
            if (CommonAppPermission.requestPermissionGranted(context)) {
                downLoadFile()
            }
        }
    }


}
