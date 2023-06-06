package com.rubber.base.components.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * @author luffyu
 * Created on 2023/1/2
 */
public class LbsUtils {

    /**
     * 计算两者直接的距离
     */
    public static double getDistance(String latitude,String longitude,String targetLatitude,String targetLongitude){
        //给定两个坐标系,计算两点相差距离
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(latitude), Double.parseDouble(longitude));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(targetLatitude), Double.parseDouble(targetLongitude));
        //Sphere坐标的计算结果
        return getDistanceMeter(source,target, Ellipsoid.Sphere);
        //WGS84坐标系计算结果
        //double meter2 = getDistanceMeter(source,target,Ellipsoid.WGS84);
    }
    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid){
        //创建GeodeticCalculator,调用计算方法,传入坐标系,经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }
}
