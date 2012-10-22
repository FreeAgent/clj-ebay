;; Copyright (C) 2011, Eduardo Julián. All rights reserved.
;;
;; The use and distribution terms for this software are covered by the 
;; Eclipse Public License 1.0
;; (http://opensource.org/licenses/eclipse-1.0.php) which can be found
;; in the file epl-v10.html at the root of this distribution.
;;
;; By using this software in any fashion, you are agreeing to be bound
;; by the terms of this license.
;;
;; You must not remove this notice, or any other, from this software.

(ns clj-ebay.client-alerts
  "This is an small Clojure binding for the Ebay API for client alerts."
  (:use (clj-ebay core)
    (clojure.contrib def)))

(defvar- +client-alerts-api+ "http://clientalerts.ebay.com/ws/ecasvc/ClientAlerts")

(defmacro- make-logins [& specifics]
  (let [standard-fields '(global-id message-id)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +client-alerts-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "MessageID.Value" ~'message-id)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-logins
  ; Login
  login "Login" "569" ["ClientAlertsAuthToken.Value" client-alerts-auth-token]
  ; Logout
  logout "Logout" "569" ["SessionData.Value" session-data, "SessionID.Value" session-id]
  )

(defmacro- make-notifs [& specifics]
  (let [standard-fields '(global-id error-language message-id version warning-level)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +client-alerts-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "ErrorLanguage" ~'error-language, "MessageID" ~'message-id, "Version" ~'version, "WarningLevel" ~'warning-level)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-notifs
  ; SetNotificationPreferences
  set-notification-preferences "SetNotificationPreferences" "687"
  ["ApplicationDeliveryPreferences.AlertEmail" alert-email, "ApplicationDeliveryPreferences.AlertEnable" alert-enable,
   "ApplicationDeliveryPreferences.ApplicationEnable" application-enable,
   "ApplicationDeliveryPreferences.ApplicationURL" (_encode-url application-url),
   "ApplicationDeliveryPreferences.DeliveryURLDetails.DeliveryURL" (_encode-url delivery-url),
   "ApplicationDeliveryPreferences.DeliveryURLDetails.DeliveryURLName" delivery-url-name,
   "ApplicationDeliveryPreferences.DeliveryURLDetails.Status" delivery-url-status,
   "ApplicationDeliveryPreferences.DeviceType" device-type,
   "ApplicationDeliveryPreferences.NotificationPayloadType" notification-payload-type,
   "ApplicationDeliveryPreferences.PayloadVersion" payload-version,
   "EventProperty.EventType" event-type, "EventProperty.Name" event-name, "EventProperty.Value" event-value,
   "UserData.ExternalUserData" external-user-data, "UserData.SMSSubscription" sms-subscription,
   "UserData.SMSSubscription.CarrierID" carrier-id, "UserData.SMSSubscription.ErrorCode" error-code,
   "UserData.SMSSubscription.ItemToUnsubscribe" item-to-unsubscribe, "UserData.SMSSubscription.SMSPhone" sms-phone,
   "UserData.SMSSubscription.UserStatus" user-status,
   "UserData.SummarySchedule" summary-schedule, "UserData.SummarySchedule.EventType" summary-schedule-event-type,
   "UserData.SummarySchedule.Frequency" summary-schedule-frequency, "UserData.SummarySchedule.SummaryPeriod" summary-period,
   "UserDeliveryPreferenceArray.NotificationEnable" notification-enable,
   "UserDeliveryPreferenceArray.NotificationEnable.EventEnable" notification-enable-event-enable,
   "UserDeliveryPreferenceArray.NotificationEnable.EventType" notification-enable-event-type]
  ; GetNotificationPreferences
  get-notification-preferences "GetNotificationPreferences" "687"
  ["OutputSelector" output-selector, "PreferenceLevel" preferences-level]
  )
