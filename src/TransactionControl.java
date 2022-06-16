package com.grgbanking.core.common;

import com.grgbanking.core.parameter.NodeDefinition;
import com.grgbanking.core.parameter.NodeErrorCode;
import com.grgbanking.core.server.init.NodeTellerConfig;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TransactionControl {
    static Map<String, Object> mapDevTransaction = new ConcurrentHashMap<>();

    static Map<String, Object> mapYellowTransaction = new ConcurrentHashMap<>();

    private static ScheduledExecutorService mScheduledExecutorService;//= Executors.newSingleThreadScheduledExecutor();//Executors.newScheduledThreadPool(4);

    static Runnable runnable = new Runnable() {
        public void run() {

            Iterator<Map.Entry<String, Object>> it = mapYellowTransaction.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, Object> entry=it.next();
                NodeTransaction nodeTrans = (NodeTransaction)entry.getValue();
                if (nodeTrans.getYellowTransTimeout()){
                    nodeTrans.closeLastTransaction();
                    it.remove();
                }
            }
        }
    };
    public static boolean Init(){
        if (mScheduledExecutorService == null
                ||mScheduledExecutorService.isShutdown()){
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        mScheduledExecutorService.scheduleAtFixedRate(runnable, 1000, 1000, TimeUnit.MILLISECONDS);
        return true;
    }

    public static boolean Cleanup(){
        if (mScheduledExecutorService != null){
            mScheduledExecutorService.shutdown();
        }

        Iterator<Map.Entry<String, Object>> it = mapYellowTransaction.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry=it.next();
            NodeTransaction nodeTrans = (NodeTransaction)entry.getValue();
            nodeTrans.closeLastTransaction();
            it.remove();
        }
        return true;
    }

    private static NodeTransaction getNodeTransaction(String deviceKey){
        if (NodeTellerConfig.getInstance().multiDeviceMode){
            if (mapDevTransaction.containsKey(deviceKey)){
                NodeTransaction nodeTrans = (NodeTransaction)mapDevTransaction.get(deviceKey);
                return nodeTrans;
            }
        }else {
            if (mapDevTransaction.size() > 0){
                for (Map.Entry<String, Object> entry : mapDevTransaction.entrySet()) {
                    return (NodeTransaction)entry.getValue();
                }
            }
        }

        return  null;
    }

    public static  int startTransaction(String deviceKey, Map<String, Object> mapTransData){

       /* if (mapDevTransaction.containsKey(deviceKey)){
            NodeTransaction nodeTrans = (NodeTransaction)mapDevTransaction.get(deviceKey);
            if (nodeTrans.getTransactionStatus() == NodeDefinition.TRANSACTION_TELLER){
                return NodeErrorCode.RETCODE_TMC_DEVC_TRANBIND_EXIST;
            }else if (nodeTrans.getTransactionStatus() == NodeDefinition.TRANSACTION_ADDCASH){
                return NodeErrorCode.RETCODE_TMC_OTHER_TRAN_EXIST;
            }else {
                nodeTrans.closeLastTransaction();
                mapDevTransaction.remove(deviceKey);
            }
        }*/

        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans != null){
            if (nodeTrans.getTransactionStatus() == NodeDefinition.TRANSACTION_TELLER){
                return NodeErrorCode.RETCODE_TMC_DEVC_TRANBIND_EXIST;
            }else if (nodeTrans.getTransactionStatus() == NodeDefinition.TRANSACTION_ADDCASH){
                return NodeErrorCode.RETCODE_TMC_OTHER_TRAN_EXIST;
            }else {
                nodeTrans.closeLastTransaction();
                mapDevTransaction.remove(nodeTrans.getDeviceKey());
            }
        }

        nodeTrans = new NodeTransaction(deviceKey, deviceKey);

        int transType = Integer.valueOf((String)mapTransData.get("transaction_type"));
        if (nodeTrans.startTransaction(transType, mapTransData)){
            mapDevTransaction.put(deviceKey, nodeTrans);
        }else {
            return NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
        }

        return NodeErrorCode.RETCODE_SUCCESS;
    }

    public static int stopTransaction(String deviceKey, Map<String, Object> mapTransData){
        /*if (!mapDevTransaction.containsKey(deviceKey)){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        NodeTransaction nodeTrans = (NodeTransaction)mapDevTransaction.get(deviceKey);
        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }*/

        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }

        int retCode = NodeErrorCode.RETCODE_SUCCESS;
        if (!nodeTrans.stopTransaction(mapTransData)){
            retCode = NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
        }

        mapDevTransaction.remove(nodeTrans.getDeviceKey());
        return retCode;
    }

    public static int stopATMJCTransaction(String deviceKey, Map<String, Object> mapTransData){
        /*if (!mapDevTransaction.containsKey(deviceKey)){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        NodeTransaction nodeTrans = (NodeTransaction)mapDevTransaction.get(deviceKey);
        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }*/

        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_ADDCASH){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }

        int retCode = NodeErrorCode.RETCODE_SUCCESS;
        if (!nodeTrans.stopATMJCTransaction(mapTransData)){
            retCode = NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
        }

        mapDevTransaction.remove(nodeTrans.getDeviceKey());
        return retCode;
    }

    public static int writeFsnRecord(boolean bMulti, String deviceKey, byte[] bytesFsn){

        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null ){
            if (NodeTellerConfig.getInstance().multiDeviceMode){
                return NodeErrorCode.RETCODE_TMA_SEQUENCE_INVALID;
            }
            else {
                nodeTrans = new NodeTransaction(deviceKey, deviceKey);
                Map<String, Object> mapTransData = new HashMap<>();
                mapTransData.put("org_id", NodeTellerConfig.getInstance().organizationID);

                if (nodeTrans.startTransaction(NodeDefinition.TRANSACTION_IDEL, mapTransData)){
                    mapDevTransaction.put(deviceKey, nodeTrans);
                    mapYellowTransaction.put(deviceKey, nodeTrans);
                }else {
                    return NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
                }
            }
        }

        if (nodeTrans != null){
            if (nodeTrans.getFsnFileCloseStatus() && nodeTrans.getTransactionStatus() == NodeDefinition.TRANSACTION_IDEL){
                mapYellowTransaction.put(nodeTrans.getDeviceKey(), nodeTrans);
            }
            if (!nodeTrans.writeFsnRecord(bytesFsn)){
                return NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
            }
            else {
                return NodeErrorCode.RETCODE_SUCCESS;
            }
        }
        return NodeErrorCode.RETCODE_TMA_PROCESS_ERR;
    }

    public static boolean writeFsnBlock(String deviceKey, byte[] bytesFsn){
        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            return false;
        }

        return true;
    }

    public  static Map<String, Object> getTransactionData(String deviceKey, Map<String, Object> mapTransData){
        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            mapTransData.put("ret_code", String.valueOf(NodeErrorCode.RETCODE_TMA_DEVID_INVALID));
            return mapTransData;
        }

        Map<String, Object> mapResult = nodeTrans.getTransactionData(mapTransData);
        mapResult.put("ret_code", String.valueOf(NodeErrorCode.RETCODE_SUCCESS));
        return mapResult;
    }

    public static Map<String, Object> getNoteData(String deviceKey, Map<String, Object> mapTransData){

        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            mapTransData.put("ret_code", String.valueOf(NodeErrorCode.RETCODE_TMA_DEVID_INVALID));
            return mapTransData;
        }

        Map<String, Object> mapResult = nodeTrans.getNoteData(mapTransData);
        mapResult.put("ret_code", String.valueOf(NodeErrorCode.RETCODE_SUCCESS));
        return mapResult;
    }

    public static int recountMoney(String deviceKey, Map<String, Object> mapTransData){
        /*if (!mapDevTransaction.containsKey(deviceKey)){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        NodeTransaction nodeTrans = (NodeTransaction)mapDevTransaction.get(deviceKey);
        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }*/

        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }

        int retCode = NodeErrorCode.RETCODE_SUCCESS;

        if (!nodeTrans.recountMoney(mapTransData)){
            retCode = NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
        }

        return retCode;
    }

    public static int setBarcode(String deviceKey, Map<String, Object> mapTransData) {
        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }

        int retCode = NodeErrorCode.RETCODE_SUCCESS;

        if (!nodeTrans.setBarcode(mapTransData)){
            retCode = NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
        }

        return retCode;
    }

    public static int switchTranType(String deviceKey, Map<String, Object> mapTransData) {
        NodeTransaction nodeTrans = getNodeTransaction(deviceKey);
        if (nodeTrans == null){
            return NodeErrorCode.RETCODE_TMC_DEVC_INVALID;
        }

        if (nodeTrans.getTransactionStatus() != NodeDefinition.TRANSACTION_TELLER){
            return  NodeErrorCode.RETCODE_TMC_TLV_SEQUENCE_INVALID;
        }

        int retCode = NodeErrorCode.RETCODE_SUCCESS;

        if (!nodeTrans.switchTranType(mapTransData)){
            retCode = NodeErrorCode.RETCODE_TMC_PROCESS_ERR;
        }

        return retCode;
    }

}
