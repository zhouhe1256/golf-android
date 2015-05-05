package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.golf.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-5.
 */
public class StadiumModel implements Serializable {
    private Long id;// 1,
    private String name;// "名称",
    private String description;// "描述",
    private String comboContent;// "内容",
    private String price;//88.8,
    private String address;//"北京市朝阳区",
    private double lon;// 40.544566,
    private double lat;// 116.565456,
    private String priceData;// "{"2015-05-05":200,...}",
    private String holeNumber;//"18洞",
    private String fullLength;// "全场总长",
    private String floorSpace;// "占地面积",
    private String startAt;// "09:00",
    private String endAt;// "18:00",
    private int intervalTime;//15,
    private List<String> imageUrls;// [ "/upload/image/xxx.png",
    private static IContentDecoder<StadiumModel> decoder = new IContentDecoder.BeanDecoder<StadiumModel>(StadiumModel.class, "stadium");

    //预约与否
    public static IPromise stadiumDetail(Long id) {
        return Http.instance().put(ApiUrl.stadiumDetail(id)).
                run();
    }

}
