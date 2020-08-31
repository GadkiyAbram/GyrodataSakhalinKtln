package com.example.gyrodatasakhalin.utils.validation

class BatteryValidation {

    var box = Regex("([1-9]*)")
    var serialsAllPattern = Regex("([A-Z][0-9]-[0-9]{4}-[0-9]{4})")
    var invoicePattern: Regex = Regex("([A-Z]\\/[0-9]{2}\\/[0-9]{2})")
    var ccdPattern: Regex = Regex("([0-9]{8}\\/[0-9]{6}\\/[0-9]{7})")

    fun checkSerial(serial: String): Boolean{
        return serial.matches(serialsAllPattern)
    }

    fun checkInvoice(invoice: String): Boolean{
        return invoice.matches(invoicePattern)
    }

    fun checkCcd(ccd: String): Boolean{
        return ccd.matches(ccdPattern)
    }
}