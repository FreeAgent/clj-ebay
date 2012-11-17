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

(ns clj-ebay.best-match
  "This is an small Clojure binding for the Ebay API for merchandising products."
  (:use (clj-ebay core)))

(def ^{:private true} +best-match-api+ "https://svcs.ebay.com/services/search/BestMatchItemDetailsService/v1")

; findBestMatchItemDetailsBySeller
(defn find-best-match-item-details-by-seller ""
  [{:keys [global-id category-id ignore-featured entries-per-page page-number seller-username
           item-filter-name item-filter-param-name item-filter-param-value item-filter-value]}]
  (let [url (str +best-match-api+ "?OPERATION-NAME=" "findBestMatchItemDetailsBySeller")
        url (_append-arg url "SERVICE-VERSION" "1.5.0", "SECURITY-APPNAME" *app-id*,
              "GLOBAL-ID" (name global-id), "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")
        url (_append-arg url "categoryId" category-id, "ignoreFeatured" (_bool->int ignore-featured),
              "itemFilter.name" item-filter-name, "itemFilter.paramName" item-filter-param-name,
              "itemFilter.paramValue" item-filter-param-value, "itemFilter.value" item-filter-value,
              "paginationInput.entriesPerPage" entries-per-page, "paginationInput.pageNumber" page-number,
              "sellerUserName" seller-username)]
    (_fetch-url url)))

; getBestMatchItemDetails
(defn get-best-match-item-details "" [item-id]
  (let [url (str +best-match-api+ "?OPERATION-NAME=" "getBestMatchItemDetails")
        url (_append-arg url "SERVICE-VERSION" "1.2.0", "SECURITY-APPNAME" *app-id*, "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")
        url (_append-arg url "itemId" item-id)]
    (_fetch-url url)))

; getVersion
(defn get-version "" []
  (let [url (str +best-match-api+ "?OPERATION-NAME=" "getVersion")
        url (_append-arg url "SERVICE-VERSION" "1.1.0", "SECURITY-APPNAME" *app-id*, "RESPONSE-DATA-FORMAT" "JSON")
        url (str url "&REST-PAYLOAD")]
    (_fetch-url url)))

;Imagine that this macro is a specialized do-template
(defmacro ^:private make-best-matchers [& specifics]
  (let [standard-fields '(global-id entries-per-page ignore-featured output-selector
                           post-search-filter-item-id post-search-filter-seller-username site-results-per-page)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +best-match-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "entriesPerPage" ~'entries-per-page, "ignoreFeatured" (_bool->int ~'ignore-featured),
                             "outputSelector" ~'output-selector, "postSearchItemFilter.itemId" ~'post-search-filter-item-id,
                             "postSearchSellerFilter.sellerUserName" ~'post-search-filter-seller-username,
                             "siteResultsPerPage" ~'site-results-per-page)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))
(make-best-matchers
  ; findBestMatchItemDetailsAcrossStores
  find-best-match-item-details-across-stores "findBestMatchItemDetailsAcrossStores" "1.5.0"
  ["categoryId" category-id, "keywords" keywords]
  ; findBestMatchItemDetailsAdvanced
  find-best-match-item-details-advanced "findBestMatchItemDetailsAdvanced" "1.5.0"
  ["categoryId" category-id, "keywords" keywords]
  ; findBestMatchItemDetailsByCategory
  find-best-match-item-details-by-category "findBestMatchItemDetailsByCategory" "1.5.0" ["categoryId" category-id]
  ; findBestMatchItemDetailsByKeywords
  find-best-match-item-details-by-keywords "findBestMatchItemDetailsByKeywords" "1.5.0" ["keywords" keywords]
  ; findBestMatchItemDetailsByProduct
  find-best-match-item-details-by-product "findBestMatchItemDetailsByProduct" "1.5.0"
  ["productId.value" product-id, "productId.type" product-type]
  )
