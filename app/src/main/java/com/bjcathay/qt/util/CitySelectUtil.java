
package com.bjcathay.qt.util;

import com.bjcathay.qt.model.CModel;
import com.bjcathay.qt.model.PModel;
import com.bjcathay.qt.model.GetCitysModel;
import com.bjcathay.qt.model.ProvinceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-6-25.
 */
public class CitySelectUtil {
    public static List<PModel> getCities(List<ProvinceModel> province, List<GetCitysModel> city) {
        List<PModel> pModels = new ArrayList<PModel>();
        for (int i = 0; i < province.size(); i++) {
            PModel p = new PModel();
            p.setProvince(province.get(i).getName());
            p.setId(province.get(i).getId());
            List<CModel> c = new ArrayList<CModel>();
            CModel mp = new CModel();
            mp.setId(p.getId());
            mp.setName(p.getProvince());
            c.add(mp);
            if (p.getProvince().startsWith("北京") || p.getProvince().startsWith("天津")
                    || p.getProvince().startsWith("上海") || p.getProvince().startsWith("重庆")) {
            } else {
                for (int j = 0; j < city.size(); j++) {
                    // todo 以后优化计算速度
                    if (city.get(j).getProvinceId() == province.get(i).getId()) {
                        CModel m = new CModel();
                        m.setId(city.get(j).getId());
                        m.setName(city.get(j).getName());
                        c.add(m);
                    }
                }
            }
            p.setCity(c);
            if (c != null && !c.isEmpty())
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
