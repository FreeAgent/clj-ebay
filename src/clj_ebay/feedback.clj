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

(ns clj-ebay.feedback
  "This is an small Clojure binding for the Ebay API for sending feedback."
  (:use (clj-ebay core)))

;Need to find out which is the Feedback API's URL
(def ^{:private true} +feedback-match-api+ "https://svcs.ebay.com/FeedbackService")

;Imagine that this macro is a specialized do-template
(defmacro ^:private make-feedbacks [& specifics]
  (let [standard-fields '(global-id date-from date-to date-range-event-type)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +feedback-match-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "dateRange.dateFrom" ~'date-from, "dateRange.dateTo" ~'date-to,
                             "dateRangeEventType" ~'date-range-event-type)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-feedbacks
  ; createDSRSummaryByCategory
  create-dsr-summary-by-category "createDSRSummaryByCategory" "1.0.1" ["categoryId" category-id]
  ; createDSRSummaryByPeriod
  create-dsr-summary-by-period "createDSRSummaryByPeriod" "1.0.1" []
  ; createDSRSummaryByShippingDetail
  create-dsr-summary-by-shipping-detail "createDSRSummaryByShippingDetail" "1.2.2"
  ["shippingCostType" shipping-cost-type, "shippingDestinationType" shipping-destination-type,
   "shippingService" shipping-service, "shipToCountry" ship-to-country]
  )

(defn create-dsr-summary-by-transaction "" [{:keys [item-id transaction-id]}]
  (let [url (str +feedback-match-api+ "?OPERATION-NAME=" "createDSRSummaryByTransaction")
        url (_append-arg url "SERVICE-VERSION" "1.0.1", "SECURITY-APPNAME" *app-id*, "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")
        url (_append-arg url "transactionKey.itemId" item-id, "transactionKey.transactionId" transaction-id)]
    (_fetch-url url)))

(defn get-dsr-summary "" [job-id]
  (let [url (str +feedback-match-api+ "?OPERATION-NAME=" "createDSRSummaryByTransaction")
        url (_append-arg url "SERVICE-VERSION" "1.2.2", "SECURITY-APPNAME" *app-id*, "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")
        url (_append-arg url "jobId" job-id)]
    (_fetch-url url)))
