/*
 * ONE EYE CORE v1.0
 * RooModelDownloader.kt
 *
 * DEMO IMPLEMENTATION - Android DownloadManager wrapper.
 * Supply the official model download URL from your approved model source.
 * No model URL or model licence is assumed by this demo.
 */

package com.oneeyecore.demo

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class RooModelDownloader(context: Context) {

    private val appContext = context.applicationContext
    private val downloadManager =
        appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    /**
     * Starts a model download and returns the DownloadManager download ID.
     *
     * The URL must be an HTTPS URL that the user is authorized to download.
     * This method queues a download; it does not verify MediaPipe compatibility
     * or initialize the downloaded model.
     */
    fun downloadModel(modelUrl: String, fileName: String = "one_eye_model.task"): Long {
        require(modelUrl.startsWith("https://", ignoreCase = true)) {
            "Model URL must use HTTPS."
        }
        require(fileName.isNotBlank() && !fileName.contains("/")) {
            "Provide a simple file name."
        }

        val request = DownloadManager.Request(Uri.parse(modelUrl))
            .setTitle("ONE EYE CORE - Neural Link")
            .setDescription("Downloading the on-device AI model")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setDestinationInExternalFilesDir(
                appContext,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)

        return downloadManager.enqueue(request)
    }

    fun queryDownload(downloadId: Long): DownloadManager.Query {
        return DownloadManager.Query().setFilterById(downloadId)
    }

    fun getDownloadedFileUri(downloadId: Long): Uri? {
        return downloadManager.getUriForDownloadedFile(downloadId)
    }
}
