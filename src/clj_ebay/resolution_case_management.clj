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

(ns clj-ebay.resolution-case-management
  "This is an small Clojure binding for the Ebay API for resolution case management."
  (:use (clj-ebay core)))

(def ^{:private true} +resolution-case-management-api+ "https://svcs.ebay.com/services/resolution/v1/ResolutionCaseManagementService")

(defmacro ^:private make-rcms [& specifics]
  (let [standard-fields '(global-id)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +resolution-case-management-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      ;url# (_append-arg url# "ErrorLanguage" ~'error-language, "MessageID" ~'message-id, "Version" ~'version, "WarningLevel" ~'warning-level)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-rcms
  ; getUserCases
  get-user-cases "getUserCases" "1.0.0"
  ["caseStatusFilter.caseStatus" case-status, "caseTypeFilter.caseType" case-type,
   "creationDateRangeFilter.fromDate" from-date, "creationDateRangeFilter.toDate" to-date,
   "itemFilter.itemId" item-id, "itemFilter.transactionId" transaction-id,
   "paginationInput.entriesPerPage" entries-per-page, "paginationInput.pageNumber" page-number, "sortOrder" sort-order]
  ; getEBPCaseDetail
  get-ebp-case-detail "getEBPCaseDetail" "1.2.0" ["caseId.id" case-id, "caseId.type" case-type]
  ; provideTrackingInfo
  provide-tracking-info "provideTrackingInfo" "1.2.0"
  ["carrierUsed" carrier-used, "caseId.id" case-id, "caseId.type" case-type, "comments" comments, "trackingNumber" tracking-number]
  ; issueFullRefund
  issue-full-refund "issueFullRefund" "1.2.0"
  ["caseId.id" case-id, "caseId.type" case-type, "comments" comments]
  ; offerOtherSolution
  offer-other-solution "offerOtherSolution" "1.2.0"
  ["caseId.id" case-id, "caseId.type" case-type, "messageToBuyer" message-to-buyer]
  ; escalateToCustomerSupport
  escalate-to-customer-support "escalateToCustomerSupport" "1.2.0"
  ["caseId.id" case-id, "caseId.type" case-type, "comments" comments, "escalationReason.buyerINRReason" buyer-inr-reason,
   "escalationReason.buyerSNADReason" buyer-snad-reason, "escalationReason.sellerINRReason" seller-inr-reason,
   "escalationReason.sellerSNADReason" seller-snad-reason]
  ; appealToCustomerSupport
  appeal-to-customer-support "appealToCustomerSupport" "1.2.0"
  ["appealReason" appeal-reason, "caseId.id" case-id, "caseId.type" case-type, "comments" comments]
  ; getActivityOptions
  get-activity-options "getActivityOptions" "1.2.0" ["caseId.id" case-id, "caseId.type" case-type]
  ; getVersion
  get-version "getVersion" "1.0.0" []
  )
