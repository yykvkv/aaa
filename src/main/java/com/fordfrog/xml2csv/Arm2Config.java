package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class Arm2Config implements ConversionConfig {

    @Override
    public Map<String, Integer> getColumns() {
        return ColumnsConverter.toMap(Arrays.asList(
                "Brand",
                "PayerID",
                "CustomerID",
                "InvoiceNumber",
                "TransactionDate",
                "Contact[@type='Customer']/Name",
                "Contact[@type='Customer']/TelephoneNumber",
                "CostCentre",
                "TransactionAmount",
                "TransactionType",
                "CustomerName",
                "Address[@type='Customer']/AddressLine[@sequence='1']",
                "Address[@type='Customer']/AddressLine[@sequence='2']",
                "Address[@type='Customer']/AddressLine[@sequence='3']",
                "Address[@type='Customer']/AddressLine[@sequence='4']",
                "Address[@type='Customer']/PostalCode",
                "CreditStatus",
                "DueDate",
                "TransactionNetAmount",
                "POAReference",
                "TransactionID",
                "Region",
                "Contact[@type='Customer']/Email",
                "Contact[@type='Company']/Name",
                "Contact[@type='Company']/Email",
                "LegalStatus")
        );
    }

    @Override
    public String getSeparator() {
        return TILDE;
    }

    @Override
    public boolean shouldTrim() {
        return true;
    }

    @Override
    public boolean shouldJoin() {
        return false;
    }

    @Override
    public String getRowItemName() {
        return "/SyncTPAROpenItems[@schemaLocation='http://schema.infor.com/InforOAGIS/2 SyncTPAROpenItems.xsd'][@releaseID='9.2'][@versionID='2.0.1'][@systemEnvironmentCode='Production'][@languageCode='GB']/DataArea/TPAROpenItems[@type='Reconcilliation']/OpenItem";
    }

}
