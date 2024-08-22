package com.example.engageissuerepro

import android.content.Context
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.android.engage.audio.datamodel.MusicArtistEntity
import com.google.android.engage.common.datamodel.ContinuationCluster
import com.google.android.engage.common.datamodel.Image
import com.google.android.engage.service.AppEngagePublishClient
import com.google.android.engage.service.PublishContinuationClusterRequest
import kotlinx.coroutines.tasks.await

class EngageWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val artists = List(5) { index ->
            MusicArtistEntity
                .Builder()
                .setEntityId("id$index")
                .setName("Artist $index")
                .setPlayBackUri("http://www.example.com/artist/$index/play".toUri())
                .setInfoPageUri("http://www.example.com/artist/$index".toUri())
                .addPosterImage(
                    Image
                        .Builder()
                        .setImageUri("https://picsum.photos/1200".toUri())
                        .setImageWidthInPixel(1200)
                        .setImageHeightInPixel(1200)
                        .build()
                )
                .build()
        }
        val clusterBuilder = ContinuationCluster.Builder()
        for (artist in artists) {
            clusterBuilder.addEntity(artist)
        }
        val client = AppEngagePublishClient(applicationContext)
        if (client.isServiceAvailable.await()) {
            try {
                client
                    .publishContinuationCluster(
                        PublishContinuationClusterRequest
                            .Builder()
                            .setContinuationCluster(clusterBuilder.build())
                            .build()
                    )
                    .await()
            } catch (e: Exception) {
                return Result.failure()
            }
        }
        return Result.success()
    }

    companion object {
        fun startWorker(context: Context) {
            WorkManager
                .getInstance(context)
                .enqueue(OneTimeWorkRequestBuilder<EngageWorker>().build())
        }
    }
}