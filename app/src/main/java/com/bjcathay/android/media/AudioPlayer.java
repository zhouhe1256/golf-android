package com.bjcathay.android.media;

import android.media.MediaPlayer;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.Deferred;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.cache.DiskCache;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.android.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer {
    private static DiskCache<String, byte[]> cache;

    static {
        cache = new DiskCache<String, byte[]>("voice", new DiskCache.ByteArraySerialization<String>());
    }

    private Deferred deferred;
    private IPromise httpPromise;
    private MediaPlayer player;

    private void ensurePlayer() {
        if (player == null) {
            player = new MediaPlayer();
        }
    }

    private void play(File audioFile) {
        ensurePlayer();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                deferred.resolved(new Arguments(AudioPlayer.this));
                reset();
            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                deferred.reject(new Arguments(AudioPlayer.this));
                reset();
                return true;
            }
        });

        FileInputStream in = null;
        try {
            if (player.isPlaying()) {
                player.stop();
            }

            player.reset();
            in = new FileInputStream(audioFile);
            player.setDataSource(in.getFD());
            player.prepare();
            player.start();
        } catch (IOException e) {
            deferred.reject(new Arguments(e));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public IPromise play(final String url) {
        if (deferred != null) {
            throw new IllegalStateException();
        }

        deferred = new Deferred();

        httpPromise = Http.instance().get(url).ignoreGlobalCallbacks().
                cache(cache).contentDecoder(new IContentDecoder.BinaryDecoder()).
                run().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                File file = cache.fileForKey(url);
                play(file);
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                deferred.reject(new Arguments(arguments.get(0)));
                reset();
            }
        }).cancel(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                deferred.cancel(new Arguments(AudioPlayer.this));
                reset();
            }
        });

        return deferred;
    }

    public boolean isPlaying() {
        return deferred != null;
    }

    public void reset() {
        deferred = null;
        httpPromise = null;
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void cancel() {
        httpPromise.cancel(new Arguments());
        reset();
    }
}
