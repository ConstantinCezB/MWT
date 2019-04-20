package com.example.mwt.util

// Constant for the shared pref file
const val SHARED_PREFERENCE_FILE = "com.constantin.pref"

// Unique worker name
const val UNIQUE_WORKER_NAME_TRACKER = "workerTrackerPeriodic"

// This keeps track of the previous day.
const val TIME_INTERVAL_PREVIOUS_WORKER_DATE = "previousWorkerTimeInterval"
const val DEFAULT_INTERVAL_PREVIOUS_WORKER_DATE = "NULL"

// =================================================================================================
/*
 * Amount of water tracked
 */
// =================================================================================================
// This is the amount a user should drink water.
const val SHARED_PREFERENCE_AMOUNT_DAILY = "numeratorDaily"
const val SHARED_PREFERENCE_AMOUNT_WEEKLY = "numeratorWeekly"
const val SHARED_PREFERENCE_AMOUNT_MONTHLY = "numeratorMonthly"
const val DEFAULT_AMOUNT_DAILY_WEEKLY_MONTHLY = 0f

// This is the default for the amount of water that a user should drink.
const val SHARED_PREFERENCE_GOAL_DAILY = "denominatorDaily"
const val DEFAULT_GOAL_DAILY = 2000f
const val SHARED_PREFERENCE_GOAL_WEEKLY = "denominatorWeekly"
const val DEFAULT_GOAL_WEEKLY = 14000f
const val SHARED_PREFERENCE_GOAL_MONTHLY = "denominatorMonthly"
const val DEFAULT_GOAL_MONTHLY = 56000f

//Variable that contains the recommended amount of water to drink
const val SHARED_PREFERENCE_RECOMMENDED_AMOUNT = "recommendedAmount"
// =================================================================================================
/*
 * User info.
 */
// =================================================================================================
// Variables related to the date of birth
const val SHARED_PREFERENCE_DATE_OF_BIRTH = "DateOfBirth"
const val DEFAULT_DATE_OF_BIRTH = "NULL"

// variables for the selected gender.
const val SHARED_PREFERENCE_GENDER = "gender"
const val DEFAULT_GENDER = "Male"

// variables for the height
const val SHARED_PREFERENCE_HEIGHT = "height"
const val DEFAULT_HEIGHT = "1"

// variables for the weight
const val SHARED_PREFERENCE_WEIGHT = "weight"
const val DEFAULT_WEIGHT = "1"

// variables for the activity level
const val SHARED_PREFERENCE_ACTIVITY_LEVEL = "activityLevel"
const val DEFAULT_ACTIVITY_LEVEL = "Low"

// variables for the season
const val SHARED_PREFERENCE_SEASON = "season"
const val DEFAULT_SEASON = "Winter"
// =================================================================================================