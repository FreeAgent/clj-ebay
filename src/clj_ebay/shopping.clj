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

(ns clj-ebay.shopping
  "This is an small Clojure binding for the Ebay API for shopping for products."
  (:use (clj-ebay core)))

(def ^{:private true} +shopping-api+ "http://open.api.ebay.com/shopping")

;Imagine that this macro is a specialized do-template
(defmacro ^:private make-shoppers [& specifics]
  (let [standard-fields '(global-id message-id)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +shopping-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "MessageID" ~'message-id)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-shoppers
  ; FindProducts
  find-products "FindProducts" "637"
  ["AvailableItemsOnly" (_bool->int available-items-only), "CategoryID" category-id, "DomainName" domain-name,
   "HideDuplicateItems" (_bool->int hide-duplicate-items), "IncludeSelector" include-selector, "MaxEntries" max-entries,
   "PageNumber" page-number, "ProductID.value" product-id, "ProductID.type" product-type, "ProductSort" product-sort,
   "QueryKeywords" keywords, "SortOrder" sort-order]
  ; FindHalfProducts
  find-half-products "FindHalfProducts" "637"
  ["AvailableItemsOnly" (_bool->int available-items-only), "DomainName" domain-name, "IncludeSelector" include-selector,
   "MaxEntries" max-entries, "PageNumber" page-number, "ProductID.value" product-id, "ProductID.type" product-type,
   "ProductSort" product-sort, "QueryKeywords" keywords, "SortOrder" sort-order, "SellerID" seller-id]
  ; GetSingleItem
  get-single-item "GetSingleItem" "689"
  ["IncludeSelector" include-selector, "ItemID" item-id, "VariationSKU" variation-sku,
   "VariationSpecifics.NameValueList.Name" variation-specifics-name,
   "VariationSpecifics.NameValueList.Value" variation-specifics-value]
  ; GetItemStatus
  get-item-status "GetItemStatus" "699" ["ItemID" item-id]
  ; GetShippingCosts
  get-shipping-costs "GetShippingCosts" "665"
  ["DestinationCountryCode" destination-country-code, "DestinationPostalCode" destination-postal-code,
   "IncludeDetails" (_bool->int include-details), "ItemID" item-id, "QuantitySold" quantity-sold]
  ; GetMultipleItems
  get-multiple-items "GetMultipleItems" "689" ["IncludeSelector" include-selector, "ItemID" item-id]
  ; GetUserProfile
  get-user-profile "GetUserProfile" "629" ["IncludeSelector" include-selector, "UserID" user-id]
  ; FindPopularSearches
  find-popular-searches "FindPopularSearches" "577"
  ["CategoryID" category-id, "IncludeChildCategories" (_bool->int include-child-categories), "MaxKeywords" max-keywords,
   "MaxResultsPerPage" max-results-per-page, "PageNumber" page-number, "QueryKeywords" keywords]
  ; FindPopularItems
  find-popular-items "FindPopularItems" "637"
  ["CategoryID" category-id, "CategoryIDExclude" category-id-exclude, "MaxEntries" max-entries, "QueryKeywords" keywords]
  ; FindReviewsAndGuides
  find-reviews-and-guides "FindReviewsAndGuides" "577"
  ["CategoryID" category-id, "MaxResultsPerPage" max-results-per-page, "PageNumber" page-number,
   "ProductID.value" product-id, "ProductID.type" product-type, "ReviewSort" review-sort, "SortOrder" sort-order, "UserID" user-id]
  )
