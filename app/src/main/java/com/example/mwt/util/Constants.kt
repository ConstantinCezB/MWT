package com.example.mwt.util

//constant for the shared pref file
const val SHARED_PREFERENCE_FILE = "com.constantin.pref"

//unique worker name
const val UNIQUE_WORKER_NAME_TRACKER = "workerTrackerPeriodic"

//date variables for the daily water that the user drank
const val SHARED_PREFERENCE_NUMERATOR_DAILY = "numerator"
const val SHARED_PREFERENCE_DENOMINATOR_DAILY = "denominator"
const val TIME_INTERVAL_PREVIOUS_WORKER_DATE = "previousWorkerTimeInterval"

const val DEFAULT_NUMERATOR = 0
const val DEFAULT_DENOMINATOR = 2000
const val DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE = "NULL"


//variables for the input tab in the bmi
//Variables related to the date of birth
const val SHARED_PREFERENCE_DATE_OF_BIRTH = "DateOfBirth"
const val DEFAULT_DATE_OF_BIRTH = "NULL"

//variables for the selected gender.
const val SHARED_PREFERENCE_GENDER = "gender"
const val DEFAULT_GENDER = "Male"

//variables for the height
const val SHARED_PREFERENCE_HEIGHT = "height"
const val DEFAULT_HEIGHT = "1"

//variables for the weight
const val SHARED_PREFERENCE_WEIGHT = "weight"
const val DEFAULT_WEIGHT = "1"

//variables for the activity level
const val SHARED_PREFERENCE_ACTIVITY_LEVEL = "activityLevel"
const val DEFAULT_ACTIVITY_LEVEL = "Low"

//variables for the season
const val SHARED_PREFERENCE_SEASON = "season"
const val DEFAULT_SEASON = "Winter"

//Variable that contains the recommended amount of water to drink
const val SHARED_PREFERENCE_RECOMMENDED_AMOUNT = "recommendedAmount"

//Variable that contains the user decided amount to drink
const val SHARED_PREFERENCE_USER_AMOUNT = "userAmount"

//Default variable for user and recommended amount.
const val DEFAULT_INTAKE_AMOUNT = 3000f