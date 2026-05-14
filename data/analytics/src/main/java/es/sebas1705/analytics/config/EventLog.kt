package es.sebas1705.analytics.config

/**
 * Sealed class to represent the event logs
 *
 * @param tag [String]: Tag of the event log
 *
 * @property Error [EventLog]: Error event log
 * @property FirstTime [EventLog]: First time event log
 * @property SignUp [EventLog]: Sign up event log
 * @property SignIn [EventLog]: Sign in event log
 * @property SignOut [EventLog]: Sign out event log
 * @property SignInGoogle [EventLog]: Sign in with google event log
 * @property ChargeTime [EventLog]: Charge time event log
 *
 * @since 0.1.0
 * @author Sebas1705 01/03/2025
 */
sealed class EventLog(val tag: String) {
    data object Error : EventLog("error")
    data object FirstTime : EventLog("first_time")
    data object SignUp : EventLog("sign_up")
    data object SignIn : EventLog("sign_in")
    data object SignOut : EventLog("sign_out")
    data object SignInGoogle : EventLog("sign_in_google")
    data object ChargeTime : EventLog("charge_time")
}