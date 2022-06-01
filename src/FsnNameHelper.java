package com.grgbanking.core.utils;

public class FsnNameHelper {
    private String fileVersion = "11";
    private String transType = "GZHM";
    private String currency = "CNY";
    private String orgId = "00001";
    private String deviceId = "00000001";
    private String fileTime ="0";
    private String transData="0";

    private String transData1;
    private String transData2;
    private String transData3;
    private String transData4;
    private String transData5;
    private String transData6;
    private String transData7;
    private String transData8;
    private String transData9;
    private String transData10;


    private void verifyData() {

        //例如20140101020204标准格式的
        try {

            if (fileTime.isEmpty() || fileTime.length() < 14) {
                fileTime = DateUtil.getDateTime(DateUtil.DATETIME_FORMAT_WITHOUTSPLITER);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        if (transType.isEmpty()){
            transType = "GZHM";
        }
        if (transData1.isEmpty()){
            transData1 = "0";
        }
        if (transData2.isEmpty()){
            transData2 = "0";
        }
        if (orgId.isEmpty()){
            orgId = "0";
        }
        if (deviceId.isEmpty()){
            deviceId = "0";
        }
        if (currency.isEmpty()){
            currency = "CNY";
        }
    }

    public String Parse() {
        verifyData();

        //11 协议版本号_币种标识_日期时间_机构编号_设备编号_交易类型码_交易数据$时间戳四位的随机数.FSN;
        transData = new String();
        if (transData1.isEmpty()) {
            transData = transData1 + "#";
        }

        if (!transData2.isEmpty()){
            transData += transData2 + "#";
        }

        if (!transData3.isEmpty()){
            transData += transData3 + "#";
        }

        if (!transData4.isEmpty()){
            transData += transData4 + "#";
        }

        if (!transData5.isEmpty()){
            transData += transData5 + "#";
        }

        if (!transData6.isEmpty()){
            transData += transData6 + "#";
        }

        if (!transData7.isEmpty()){
            transData += transData2 + "#";
        }

        if (!transData8.isEmpty()){
            transData += transData8 + "#";
        }

        if (!transData9.isEmpty()){
            transData += transData9 + "#";
        }

        if (!transData10.isEmpty()){
            transData += transData10 + "#";
        }

        if (transData.isEmpty()){
            transData = "0";
        }

        String fileName = new String();
        try{

            fileName = String.format("%s_%s_%s_%s_%s_%s_%s$%s%03d.FSN",fileVersion, currency, fileTime, orgId, deviceId,
                    transType, transData, DateUtil.getDateTime(DateUtil.DATETIME_FORMAT_WITHOUTSPLITER), System.currentTimeMillis());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return fileName;
    }
}
