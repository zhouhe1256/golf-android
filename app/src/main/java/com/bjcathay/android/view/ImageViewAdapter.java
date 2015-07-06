package com.bjcathay.android.view;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.HttpClient;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ApiUrl;

public class ImageViewAdapter {
    public static interface UpdatePredicate {
        boolean accept(ImageViewAdapter adapter, Bitmap bitmap);
    }

    public static class NullUpdatePredicate implements UpdatePredicate {

        @Override
        public boolean accept(ImageViewAdapter adapter, Bitmap bitmap) {
            return true;
        }
    }

    private static volatile long tagSequence;

    private final ImageView imageView;
    private final Http http;
    private final String url;

    private int defaultImageId;
    private int errorImageId;

    private IContentDecoder<Bitmap> contentDecoder;
    private IPromise promise;

    private UpdatePredicate predicate = new NullUpdatePredicate();

    private long tag;


    public ImageViewAdapter(ImageView imageView, String url, int defaultImageId) {
        this(imageView, url, defaultImageId, 0);
    }

    public ImageViewAdapter(ImageView imageView, String url, int defaultImageId, int errorImageId) {
        this(imageView, Http.imageInstance(), url, defaultImageId, errorImageId, null);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String url, int defaultImageId) {
        this(imageView, http, url, defaultImageId, 0, null);
    }

    public ImageViewAdapter(ImageView imageView, String url, IContentDecoder<Bitmap> contentDecoder) {
        this(imageView, Http.imageInstance(), url, 0, 0, contentDecoder);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String url, IContentDecoder<Bitmap> contentDecoder) {
        this(imageView, http, url, 0, 0, contentDecoder);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder) {
        this(imageView, http, url, defaultImageId, errorImageId, contentDecoder, new NullUpdatePredicate());
    }

    public ImageViewAdapter(ImageView imageView, Http http, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder, UpdatePredicate predicate) {
        tag = getNextTag();
        imageView.setTag(R.string.image_view_tag, tag);

        this.imageView = imageView;
        this.http = http;
        //todo
        this.url = GApplication.getInstance().getResources().getString(R.string.img_host) + url;

        this.defaultImageId = defaultImageId;
        this.errorImageId = errorImageId;

        this.contentDecoder = contentDecoder;
        if (predicate == null) {
            predicate = new NullUpdatePredicate();
        }
        this.predicate = predicate;

        if (TextUtils.isEmpty(url)) {
            if (defaultImageId == 0) {
                throw new IllegalArgumentException("url can't be empty");
            }
            imageView.setImageResource(defaultImageId);
            return;
        }

        loadImage();
    }

    private synchronized long getNextTag() {
        return tagSequence++;
    }


    private void loadImage() {
        if (defaultImageId != 0) {
            imageView.setImageResource(defaultImageId);
        }

        HttpClient httpClient = http.get(url);
        if (contentDecoder != null) {
            httpClient.contentDecoder(contentDecoder);
        }
        promise = httpClient.run().done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (isImageViewChanged()) {
                    return;
                }

                Bitmap bitmap = arguments.get(0);
                if (predicate.accept(ImageViewAdapter.this, bitmap)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                if (isImageViewChanged()) {
                    return;
                }

                if (errorImageId != 0) {
                    if (predicate.accept(ImageViewAdapter.this, null)) {
                        imageView.setImageResource(errorImageId);
                    }
                    return;
                }

                if (defaultImageId != 0) {
                    if (predicate.accept(ImageViewAdapter.this, null)) {
                        imageView.setImageResource(defaultImageId);
                    }
                }
            }
        });
    }

    private boolean isImageViewChanged() {
        Long currentTag = (Long) imageView.getTag(R.string.image_view_tag);
        if (currentTag == null) {
            return true;
        }
        return !currentTag.equals(tag);
    }


    public void setPredicate(UpdatePredicate predicate) {
        this.predicate = predicate;
    }

    public IPromise getPromise() {
        return promise;
    }

    public static IPromise adapt(ImageView imageView, String url) {
        return adapt(imageView, url, 0);
    }

    public static IPromise adapt(ImageView imageView, String url, int defaultImageId) {
        return adapt(imageView, url, defaultImageId, 0);
    }

    public static IPromise adapt(ImageView imageView, String url, int defaultImageId, int errorImageId) {
        return adapt(imageView, url, defaultImageId, errorImageId, null, null);
    }

    public static IPromise adapt(ImageView imageView, String url, int defaultImageId, int errorImageId, UpdatePredicate predicate) {
        return adapt(imageView, url, defaultImageId, errorImageId, null, predicate);
    }

    public static IPromise adapt(ImageView imageView, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder, UpdatePredicate predicate) {
        ImageViewAdapter adapter = new ImageViewAdapter(imageView, Http.imageInstance(), url, defaultImageId, errorImageId, contentDecoder, predicate);
        return adapter.getPromise();
    }

    public static IPromise adapt(ImageView imageView, String url, IContentDecoder<Bitmap> contentDecoder) {
        return adapt(imageView, url, 0, 0, contentDecoder, null);
    }
}