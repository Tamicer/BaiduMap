package com.tamic.map.mapdemo.map.thirdmap;

/**
 * 地图操作类工厂，使用此类获取指定地图操作类
        */
public class MapOperatorFactory {

    public static <T extends IMapOperator> T getOperator(Class<T> clazz) {
        IMapOperator operator = null;
        try {
            operator = (IMapOperator) Class.forName(clazz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) operator;
    }
}
