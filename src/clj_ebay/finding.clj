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

(ns clj-ebay.finding
  "This is an small Clojure binding for the Ebay API for finding products."
  (:use (clj-ebay core)))

(def ^{:private true} +finding-api+ "http://svcs.ebay.com/services/search/FindingService/v1")

(defn get-search-keywords-recommendation "" [keywords]
  (let [url (str +finding-api+ "?OPERATION-NAME=" "getSearchKeywordsRecommendation")
        url (_append-arg url "SERVICE-VERSION" "1.0.0", "SECURITY-APPNAME" *app-id*, "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")
        url (_append-arg url "keywords" (_encode-url keywords))]
    (_fetch-url url)))

(defn get-histograms "" [category-id]
  (let [url (str +finding-api+ "?OPERATION-NAME=" "getHistograms")
        url (_append-arg url "SERVICE-VERSION" "1.0.0", "SECURITY-APPNAME" *app-id*, "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")
        url (_append-arg url "categoryId" category-id)]
    (_fetch-url url)))

;Imagine that this macro is a specialized do-template
(defmacro make-finders [& specifics]
  (let [standard-fields '(global-id custom-id network-id tracking-id buyer-postal-code entries-per-page page-number sort-order)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +finding-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "affiliate.customId" ~'custom-id, "affiliate.networkId" ~'network-id,
                             "affiliate.trackingId" ~'tracking-id, "buyerPostalCode" ~'buyer-postal-code,
                             "paginationInput.entriesPerPage" ~'entries-per-page, "paginationInput.pageNumber" ~'page-number,
                             "sortOrder" ~'sort-order)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-finders
  ; findItemsByKeywords
  find-items-by-keywords "findItemsByKeywords" "1.9.0"
  ["aspectFilter.aspectName" aspect-name, "aspectFilter.aspectValueName" aspect-value-name,
   "domainFilter.domainName" domain-name, "itemFilter.name" item-filter-name,
   "itemFilter.paramName" item-filter-param-name, "itemFilter.paramValue" item-filter-param-value,
   "itemFilter.value" item-filter-value, "keywords" (_encode-url keywords), "outputSelector" output-selector]
  ; findItemsByCategory
  find-items-by-category "findItemsByCategory" "1.9.0"
  ["aspectFilter.aspectName" aspect-name, "aspectFilter.aspectValueName" aspect-value-name,
   "categoryId" category-id, "domainFilter.domainName" domain-name, "outputSelector" output-selector,
   "itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
   "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value]
  ; findItemsAdvanced
  find-items-advanced "findItemsAdvanced" "1.9.0"
  ["aspectFilter.aspectName" aspect-name, "aspectFilter.aspectValueName" aspect-value-name,
   "categoryId" category-id, "domainFilter.domainName" domain-name, "outputSelector" output-selector,
   "itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
   "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value, "keywords" (_encode-url keywords)
   "descriptionSearch" (_bool->int description-search)]
  ; findItemsByProduct
  find-items-by-product "findItemsByProduct" "1.9.0"
  ["itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
   "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value, "outputSelector" output-selector,
   "productId.value" product-id, "productId.type" product-type]
  ; findItemsIneBayStores
  find-items-in-ebay-stores "findItemsIneBayStores" "1.9.0"
  ["aspectFilter.aspectName" aspect-name, "aspectFilter.aspectValueName" aspect-value-name,
   "categoryId" category-id, "domainFilter.domainName" domain-name, "outputSelector" output-selector,
   "itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
   "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value, "keywords" (_encode-url keywords)
   "storeName" store-name])
