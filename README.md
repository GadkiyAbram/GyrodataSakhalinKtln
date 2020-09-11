# GyrodataSakhalinKtln

This app is the further development of GyrodataSakhalinAndroid
https://github.com/GadkiyAbram/GyrodataSakhalinAndroid
written on Java.

Now this app redone on Kotlin with Coroutines and Retrofit2 library to fetch the data from backend resource 
# !!! ATTACH LINK TO REPO Gyrodata API !!!

The purpose of this app is to connect to Gyrodata API and receive Items / Batteries / Jobs data from DB, running on MSSQL Server on Azure.

Usage:
1.1. To use the app you will need to ask Admin to grant the access to service.
This should be done using the link below:
http://demolaravel.azurewebsites.net - go to "Send Request" and fill First / Last Names & Email. Admin will check information and grant the access, give you a password and set your Role (Admin / User)
If you're Admin - you will be able to manage User's data, e.g. grant users's access.
If you're User - you will only be able to manage Items / Batteries / Jobs data, e.g. Insert / Update.

Enter your credentials and API service will send you a token (valid 24 hrs) to use the API Service.

2. What can be done with App once you receive the credentials.
Dashboard screen will show 
  - BATTERIES data in DB (Batteries table last updated and Total Batteries in Table)
  - JOB LOGS data in DB (Jobs table last updated and Total Jobs in Table)
  - TOOLS data in DB (Items table last updated and Total Items in Table)
  
TOOLS screen diplays the Equipment Items in DB. The record in RecyclerView can be expanded to see full details and Picture attached.
The Item data consists of: 
  - Item Name (e.g. GDP Sections, Modem etc.)
  - Item arrived in Country (Russia in current case)
  - Item Asset number
  - Item Circulation hours, the important value for some Items, that should be calculated carefully. The PM (Periodical Maintanence) for Item is after 500 Hrs.
    Thus, if the Tool reaches 400 hrs, the text will be switched to RED (kind of warning)
  - Item CCD (Custom Declaration) & Invoice (the Item was sent from Base Country, currently from the UK)
  - Item Position in CCD
  - Comments
  - etc.
  
 BATTERIES screen displays the Battery Items in DB. The record in RecyclerView can be expanded to see full details.
 The Battery data consists of:
   - Battery Serial One / Two / Three. Serial One is main S/N, it should be indicated anyway. The rest ones could be N/A
   - CCD / Invoice (the same as for Tools)
   - Condition (New / Used). Indicated in the circle and marked with the color. Additionally, N or U put in circle
   - Manufactured Date
   - Location (If one is in Storage Base or outsidde, e.g. on the Rig, assigned for the Job)
   - Comment if needed
   
 JOBS screen displays the Job Items in DB. The record in RecyclerView can be expanded to see full details.
 The Job data consists of:
  - Job Number. This parameter is neccesary, should be unique
  - Client. Indicated in Circle and shortened name attached.
  - Tool (GDP, Gyro / Power / Data) - asset number
  - Modem - asset number
  - Battery Bullplug - asset number
  - Battery - serial number
  - Circulation Hours - this could be 0 if the Job is not done yet. Otherwise, it should be entered, as this value is used for calculating the Tools circulation hours (for GDP / Modem / BBP) - max 500 hrs prior to Maintanence
  - Engineers names, assigned to the Job
  - Dates of Engineers arrivals / leaving the Rig
  - Container Asset
  - Dates of Container arrival / leaving the Rig
  - Max Temp in the Well being drilled
  - Issues (yes / no). If "Yes" - put in Comment short description
  - Comment - if neccesary
  
The right bottom Floating Button will show three buttons: Add Item / Battery / Job
Prior the Job creating (final step) you will need to insert Items and Batteries.

INSERTING ITEMS.
In mobile App there is no need to instert ALL the information, as it will be easier to correct one via desktop / web versions of the Service.
The TWO parameters you will need to insert: Type of Item and Asset number. The app will check then if there is no duplicate values.
For creating the Job you will need to have the following Items: GDP Sections / GWD Modem / GWD Bullplug
Below you can attach picture if applicable

INSERTING BATTERIES.
As well as Items, there is no need to insert ALL the information.
You just need to insert Serial One number of the NEW battery, as for Job creating there should be only NEW batteries.

INSERTING JOB.
If you have you Items and Batteries inserted, it will be populated in the spinners of Job creating form:
The App will call the API with "Initial Job Data", that consists of:
  - GDP Sections
  - GWD Modem
  - GWD Bullplug
  - Battery
  - Engineer1 & 2 - this values comes from API and hardrecoded in DB
  - Client - the same as for Engineers

