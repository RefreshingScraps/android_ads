package com.example.ads

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.ads.ads.GDTAd

class PlayerActivity : AppCompatActivity() {
    // 声明 ExoPlayer
    private var player: ExoPlayer? = null
    var isVideoLoaded: Boolean = false
    var isAdSkipped: Boolean = false

    @OptIn(markerClass = [UnstableApi::class])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // 1. 绑定布局中的 PlayerView
        val playerView = findViewById<PlayerView?>(R.id.playerView)

        // 2. 初始化 ExoPlayer (Java 中 Builder 调用方式与 Kotlin 略有不同)
        player = ExoPlayer.Builder(this)
            .build()
        playerView!!.setUseController(false)
        // 3. 将 Player 绑定到 PlayerView
        playerView.setPlayer(player)

        val mediaItem: MediaItem?
        // 4. 创建媒体项 (支持网络URL、本地资源、本地文件)
        // 示例1：网络视频 URL (替换为有效地址)
        val fileURL = this.intent.getStringExtra("videofilepath")
        mediaItem = if (fileURL == null) {
            MediaItem.fromUri("android.resource://" + packageName + "/" + R.raw.crotalus_atrox)
        } else if (fileURL.isEmpty()) {
            MediaItem.fromUri("http://freshingair.dpdns.org/%E6%81%AD%E5%96%9C%E5%8F%91%E8%B4%A2%20%E5%88%98%E5%BE%B7%E5%8D%8E.mp3")
        } else {
            MediaItem.fromUri(fileURL)
        }
        // 示例2：本地 res/raw 资源 (如 test.mp4)
        // MediaItem mediaItem = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.test);
        // 示例3：本地文件路径
        // MediaItem mediaItem = MediaItem.fromUri("file:///sdcard/Download/test.mp4");
        playerView.hideController()
        playerView.setUseController(false)
        // 6. 设置自动播放 (prepare 后自动播放)
        player!!.playWhenReady = false
        // 5. 添加媒体项并准备播放
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        // 7. 添加播放状态监听 (Java 中使用匿名内部类实现 Listener)
        player!!.addListener(object : Player.Listener {
            // 播放状态变化回调
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    ExoPlayer.STATE_IDLE -> {}
                    ExoPlayer.STATE_BUFFERING -> {}
                    ExoPlayer.STATE_READY ->                         // 准备完成，可播放
//                        LoadAdByPlatform(AdPlatform.values()[new Random().nextInt(AdPlatform.values().length)]);
                        if (!isVideoLoaded) {
                            LoadAdByPlatform(AdPlatform.GDT)
                            isVideoLoaded = true
                        }

                    ExoPlayer.STATE_ENDED -> {
                        // 播放结束，重置并重新播放
                        player!!.seekTo(0)
                        player!!.stop()
                    }
                }
            }

            // 播放/暂停状态变化回调
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying && !isAdSkipped) {
                    playerView.setUseController(true)
                    playerView.showController()
                    isAdSkipped = true
                }
                // isPlaying = true 表示正在播放，false 表示暂停
            }

        })
    }

    private fun LoadAdByPlatform(adPlatform: AdPlatform) {
        when (adPlatform) {
            AdPlatform.GDT -> GDTAd.GDTPreRollAd(
                this,
                AdId.GDTId.NATIVE_VIDEO_ID,
                findViewById(R.id.instream_ad_container),
                player
            )

            else -> player!!.play()
        }
    }

    // 生命周期：暂停播放
    override fun onPause() {
        super.onPause()
        if (player != null) {
            player!!.pause()
        }
    }

    // 生命周期：恢复播放
    override fun onResume() {
        super.onResume()
        if (player != null) {
            player!!.play()
        }
    }

    // 生命周期：释放播放器（关键，避免内存泄漏）
    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            player!!.release() // 释放所有资源
            player = null // 置空，避免空指针
        }
    } // 常用播放控制方法（可按需调用）
    //    private void controlPlayer() {
    //        if (player == null) return;
    // 继续播放
    //        player.play();
    // 停止
    //        player.stop();
    // 快进10秒
    //        player.seekTo(player.getCurrentPosition() + 10000);
    // 快退10秒
    //        player.seekTo(player.getCurrentPosition() - 10000);
    // 设置音量（0.0 ~ 1.0）
    //        player.setVolume(1.0f);
    // 设置循环模式：REPEAT_MODE_OFF(默认)/REPEAT_MODE_ONE/REPEAT_MODE_ALL
    //        player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
    //    }
}