package com.bjcathay.qt.util;

import com.bjcathay.qt.model.CModel;
import com.bjcathay.qt.model.PModel;
import com.bjcathay.qt.model.GetCitysModel;
import com.bjcathay.qt.model.ProvinceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-6-25.
 */
public class CitySelectUtil {
    public static List<PModel> getCities(List<ProvinceModel> province, List<GetCitysModel> city) {
        List<PModel> pModels = new ArrayList<PModel>();
       /* Collections.sort(city,new Comparator<CitysModel>() {
            @Override
            public int compare(CitysModel cityModel, CitysModel cityModel2) {
                if(cityModel.getProvinceId()>cityModel2.getProvinceId()){
                    return 1;
                }else if(cityModel.getProvinceId()==cityModel2.getProvinceId()){
                    return 0;
                }else{
                    return -1;
                }
            }
        });*/
        for (int i = 0; i < province.size(); i++) {
            PModel p = new PModel();
            p.setProvince(province.get(i).getName());
            List<CModel> c = new ArrayList<CModel>();
            for (int j = 0; j < city.size(); j++) {
                //todo 以后优化计算速度
                if (city.get(j).getProvinceId() == province.get(i).getId()) {
                    CModel m = new CModel();
                    m.setId(city.get(j).getId());
                    m.setName(city.get(j).getName());
                    c.add(m);
                }
            }
            p.setCity(c);
            if (c != null&&!c.isEmpty())
                pModels.add(p);
        }
        return pModels;
    }

    public static List<CModel> getHot(List<GetCitysModel> city) {
        List<CModel> c = new ArrayList<CModel>();
        for (int i = 0; i < city.size(); i++) {
            if (city.get(i).isHot()) {
                CModel m = new CModel();
                m.setId(city.get(i).getId());
                m.setName(city.get(i).getName());
                c.add(m);
            }

        }
        return c;
    }
}
