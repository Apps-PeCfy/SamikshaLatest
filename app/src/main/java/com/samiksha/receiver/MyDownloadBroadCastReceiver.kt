package com.samiksha.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyDownloadBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // get the refid from the download manager
        val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      /*  val downloadModel: DownloadModel = LocalCrudRepository.getInstance(context)
            .getDownloadModelByReferenceNumber(
                referenceId,
                SessionManager.getInstance(context).getUserModel().getV_AccountId()
            )
        if (downloadModel != null) {
            downloadModel.setDownloaded(true)
            LocalCrudRepository.getInstance(context).updateDownloadModel(downloadModel)
        }*/
    }
}