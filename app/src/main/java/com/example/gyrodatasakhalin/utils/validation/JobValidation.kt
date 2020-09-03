package com.example.gyrodatasakhalin.utils.validation

class JobValidation {

    val jobNumberPattern = Regex("([A-Z]{2}[0-9]{4}[A-Z]{3}[0-9]{3})")
    val modemVersionPattern = Regex("([1-9].[0-9]{2})")
    val maxTempPattern = Regex("([1-9]*.[0-9]{2})")

    fun checkJobNumber(jobNumber: String): Boolean{
        return jobNumber.matches(jobNumberPattern)
    }

    fun checkModemVersion(modemVer: String): Boolean{
        return modemVer.matches(modemVersionPattern)
    }

    fun checkMaxTemp(maxTemp: String): Boolean{
        return maxTemp.matches(maxTempPattern)
    }
}