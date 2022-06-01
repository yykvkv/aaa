package com.grgbanking.core.utils;

import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class FsnFileHelper {

    public static String getRandString(){
        Random random = new Random();
        String randString = String.valueOf(Math.abs(random.nextInt()) %1000 + 1000);


        try{
            String dateString = DateUtil.getDateTime(DateUtil.TIME_MILLISECOND_FORMAT_WITHOUTSPLITER);
            randString  = dateString + randString;
        } catch (Exception e){

        }

        if (randString.length() > 16){
            randString = randString.substring(0, 16);
        }

        return randString;
    }
    public static String createGZHMFileName(String orgId, String devId){

        String fileTime = "20000101000000";
        try{
            fileTime = DateUtil.getDateTime(DateUtil.DATETIME_FORMAT_WITHOUTSPLITER);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //大数据平台原因，没有的字段必须按文档填0
        //操作员号#来源机构#去向机构#预留项。
        //return String.format("11_CNY_%s_%s_%s_GZHM_0#0$%s.FSN", fileTime, orgId, devId, getRandString());
        return String.format("11_CNY_%s_%s_%s_GZHM_0#0#0#0$%s.FSN", fileTime, orgId, devId, getRandString());
    }

    public static String createGMJYFileName(Map<String, Object> mapData){

        String fileTime = "20000101000000";
        try{
            fileTime = DateUtil.getDateTime(DateUtil.DATETIME_FORMAT_WITHOUTSPLITER);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //修改取fsn数据中的机具号
        String devId = (String)mapData.get("fsn_deviceid");
        if (devId.isEmpty()){
            devId = (String)mapData.get("device_id");
        }
        String dataCurrcecy = (String)mapData.get("fsn_currency");
        if (dataCurrcecy.isEmpty()){
            dataCurrcecy = "CNY";
        }
        String orgId =  (String)mapData.get("org_id");

        String transTime = String.format("%04d%02d%02d%02d%02d%02d",
                2000+ Integer.valueOf((String)mapData.get("datetime_year")), Integer.valueOf((String)mapData.get("datetime_month")),
                Integer.valueOf((String)mapData.get("datetime_day")), Integer.valueOf((String)mapData.get("datetime_hour")),
                Integer.valueOf((String)mapData.get("datetime_minute")), Integer.valueOf((String)mapData.get("datetime_second")));

        String transData1 = (String)mapData.get("transaction_code");
        String transData2 = (String)mapData.get("transaction_amount");
        String transData3 = (String)mapData.get("credential_type");
        String transData4 = (String)mapData.get("credential_num");
        String transData5 = (String)mapData.get("account_num");
        String transData6 = (String)mapData.get("teller_num");
        String transData7 = (String)mapData.get("transaction_num");

        if (transData1.isEmpty()){
            transData1 = "0";
        }

        String tailData = transData1;
        if (!transData2.isEmpty()){
            tailData += "#" + transData2;
        }else {
            tailData += "#" + "0";
        }

        if (!transData3.isEmpty()){
            tailData += "#" + transData3;
        }else {
            tailData += "#" + "0";
        }

        if (!transData4.isEmpty()){
            tailData += "#" + transData4;
        }else {
            tailData += "#" + "0";
        }

        if (!transData5.isEmpty()){
            tailData += "#" + transData5;
        }else {
            tailData += "#" + "0";
        }

        if (!transData6.isEmpty()){
            tailData += "#" + transData6;
        }else {
            tailData += "#" + "0";
        }

        if (!transData7.isEmpty()){
            tailData += "#" + transData7;
        }else {
            tailData += "#" + "0";
        }

        return String.format("11_%s_%s_%s_%s_GMJY_%s$%s.FSN",dataCurrcecy, transTime,orgId,devId,
                tailData, getRandString());
    }

    public static String createLargeMoneryFileName(String virtulaBarcode, Map<String, Object> mapData){

        //11_CNY_20140813151820_00001_00000005_XJLZ_00552994296184863070#admin#DEQK6222003677.FSN
        //(注：11版协议命名：协议版本号_币种标识_日期时间_机构编号_设备编号_交易类型码_交易数据.FSN，
        //XJLZ的交易数据为流转条码号、操作员号、预留项)
        String fileTime = "20000101000000";
        try{
            fileTime = DateUtil.getDateTime(DateUtil.DATETIME_FORMAT_WITHOUTSPLITER);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        //修改取fsn数据中的机具号
        String devId = (String)mapData.get("fsn_deviceid");//(String)mapData.get("device_id");
        if (devId.isEmpty()){
            devId = (String)mapData.get("device_id");
        }
        String dataCurrcecy = (String)mapData.get("fsn_currency");
        if (dataCurrcecy.isEmpty()){
            dataCurrcecy = "CNY";
        }
        String orgId =  (String)mapData.get("org_id");

        String transTime = String.format("%04d%02d%02d%02d%02d%02d",
                2000+ Integer.valueOf((String)mapData.get("datetime_year")), Integer.valueOf((String)mapData.get("datetime_month")),
                Integer.valueOf((String)mapData.get("datetime_day")), Integer.valueOf((String)mapData.get("datetime_hour")),
                Integer.valueOf((String)mapData.get("datetime_minute")), Integer.valueOf((String)mapData.get("datetime_second")));

        String transData1 = virtulaBarcode;
        String transData2 = (String)mapData.get("teller_num");
        String transData3 = "DEQK" + (String)mapData.get("account_num");

        if (transData1.isEmpty()){
            transData1 = "0";
        }

        String tailData = transData1;
        if (!transData2.isEmpty()){
            tailData += "#" + transData2;
        }else {
            tailData += "#" + "0";
        }

        if (!transData3.isEmpty()){
            tailData += "#" + transData3;
        }else {
            tailData += "#" + "0";
        }

        return String.format("11_%s_%s_%s_%s_XJLZ_%s$%s.FSN",dataCurrcecy,transTime,orgId,devId,
                tailData, getRandString());
    }

    public static String createATMJCFileName(Map<String, Object> mapData){

        String fileTime = "20000101000000";
        try{
            fileTime = DateUtil.getDateTime(DateUtil.DATETIME_FORMAT_WITHOUTSPLITER);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        String devId = (String)mapData.get("device_id");
        String orgId =  (String)mapData.get("org_id");

        String transData1 = (String)mapData.get("atm_num");
        String transData2 = (String)mapData.get("box_num");

        if (transData1.isEmpty()){
            transData1 = "0";
        }

        String tailData = transData1;
        if (!transData2.isEmpty()){
            tailData += "#" + transData2;
        }else {
            tailData += "#" + "0";
        }
        return String.format("11_CNY_%s_%s_%s_ATMJC_%s#0#0$%s.FSN",fileTime,orgId,devId,
                tailData, getRandString());
    }

    public static String updateFileNameDeviceId(String fsnFileName, String deviceId, String noteCurrency){

        /*StringTokenizer token = new StringTokenizer(fsnFileName,"_");
        if (token.countTokens() != 7){
            return fsnFileName;
        }*/
        String retFileName = fsnFileName.replace('\\', '/');
        int posTmp = retFileName.lastIndexOf('/');
        if (posTmp >= 0){
            retFileName = retFileName.substring(posTmp+1);
        }

        if (deviceId.isEmpty() && noteCurrency.isEmpty()){
            return retFileName;
        }

        String[] arrayFileName = retFileName.split("_");
        if (arrayFileName.length != 7){
            return fsnFileName;
        }

        if (!noteCurrency.isEmpty()){
            arrayFileName[1] = noteCurrency;
        }

        if (!deviceId.isEmpty()){
            arrayFileName[4] = deviceId;
        }

        retFileName = arrayFileName[0];
        for (int i = 1; i < arrayFileName.length; i++){
            retFileName += "_" + arrayFileName[i];
        }

        return retFileName;

    }
}
