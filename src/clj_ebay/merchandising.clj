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

(ns clj-ebay.merchandising
  "This is an small Clojure binding for the Ebay API for merchandising products."
  (:use (clj-ebay core)))

(def ^{:private true} +merchandising-api+ "http://svcs.ebay.com/MerchandisingService")

;Imagine that this macro is a specialized do-template
(defmacro ^:private make-merchandisers [& specifics]
  (let [standard-fields '(global-id custom-id network-id tracking-id max-results)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +merchandising-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "affiliate.customId" ~'custom-id, "affiliate.networkId" ~'network-id,
                             "affiliate.trackingId" ~'tracking-id, "maxResults" ~'max-results)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-merchandisers
  ; getMostWatchedItems
  get-most-watched-items "getMostWatchedItems" "1.4.0" ["categoryId" category-id]
  ; getRelatedCategoryItems
  get-related-category-items "getRelatedCategoryItems" "1.4.0"
  ["categoryId" category-id, "itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
   "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value, "itemId" item-id]
  ; getSimilarItems
  get-similar-items "getSimilarItems" "1.4.0"
  ["categoryId" category-id, "categoryIdExclude" category-id-exclude, "endTimeFrom" end-time-form, "endTimeTo" end-time-to,
   "itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
   "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value,
   "itemId" item-id, "listingType" listing-type, "maxPrice" max-price]
  ; getTopSellingProducts
  get-top-selling-products "getTopSellingProducts" "1.1.0" []
  )
