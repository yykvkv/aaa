package com.fordfrog.xml2csv;

import java.util.Arrays;
import java.util.Map;

public class RealWorldConfig implements ConversionConfig {

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
                "Address[@type='Customer']/PostalCode",
                "CreditStatus",
                "DueDate",
                "TransactionNetAmount",
                "POAReference",
                "TransactionID",
                "Region",
                //email
                //credit control clerk
                "LegalStatus")
        );
    }

    @Override
    public char getSeparator() {
        return '~';
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
    public String getItemName() {
        return "/SyncTPAROpenItems[@schemaLocation='http://schema.infor.com/InforOAGIS/2 SyncTPAROpenItems.xsd'][@releaseID='9.2'][@versionID='2.0.1'][@systemEnvironmentCode='Production'][@languageCode='GB']/DataArea/TPAROpenItems[@type='Reconcilliation']/OpenItem";
    }

}
