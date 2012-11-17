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

(ns clj-ebay.product-metadata
  "This is an small Clojure binding for the Ebay Product API."
  (:use (clj-ebay core)))

(def ^{:private true} +product-metadata-api+ "http://svcs.ebay.com/services/marketplacecatalog/ProductMetadataService/v1")

(defmacro ^:private make-prodmetas [& specifics]
  (let [standard-fields '(global-id category-id)]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +product-metadata-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      url# (_append-arg url# "categoryId" ~'category-id)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-prodmetas
  ; getCompatibilitySearchDataVersion
  get-compatibility-search-data-version "getCompatibilitySearchDataVersion" "1.1.0" []
  ; getCompatibilitySearchNames
  get-compatibility-search-names "getCompatibilitySearchNames" "1.1.0" ["dataset" data-set]
  ; getCompatibilitySearchValues
  get-compatibility-search-values "getCompatibilitySearchValues" "1.2.0"
  ["listFormatOnly" (_bool->int list-format-only),
   "propertyName" property-name,
   "propertyFilter.value.number.unitOfMeasurement" property-number-unit-of-measurement,
   "propertyFilter.value.number.value" property-number-value,
   "propertyFilter.value.text.value" property-text-value,
   "propertyFilter.value.URL.value" (_encode-url property-url-value),
   "sortOrder.order" sort-order,
   "sortOrder.propertyName" sort-order-property-name]
  ; getCompatibilitySearchValuesBulk
  get-compatibility-search-values-bulk "getCompatibilitySearchValuesBulk" "1.2.0"
  ["listFormatOnly" (_bool->int list-format-only),
   "propertyName" property-name,
   "propertyFilter.value.number.unitOfMeasurement" property-number-unit-of-measurement,
   "propertyFilter.value.number.value" property-number-value,
   "propertyFilter.value.text.value" property-text-value,
   "propertyFilter.value.URL.value" (_encode-url property-url-value),
   "sortOrder.order" sort-order,
   "sortOrder.propertyName" sort-order-property-name]
  ; getProductSearchDataVersion
  get-product-search-data-version "getProductSearchDataVersion" "1.1.0" []
  ; getProductSearchNames
  get-product-search-name "getProductSearchNames" "1.1.0" ["dataset" data-set]
  ; getProductSearchValues
  get-product-search-values "getProductSearchValues" "1.2.0"
  ["listFormatOnly" (_bool->int list-format-only),
   "propertyName" property-name,
   "propertyFilter.value.number.unitOfMeasurement" property-number-unit-of-measurement,
   "propertyFilter.value.number.value" property-number-value,
   "propertyFilter.value.text.value" property-text-value,
   "propertyFilter.value.URL.value" (_encode-url property-url-value),
   "sortOrder.order" sort-order,
   "sortOrder.propertyName" sort-order-property-name]
  ; getProductSearchValuesBulk
  get-product-search-values-bulk "getProductSearchValuesBulk" "1.2.0"
  ["listFormatOnly" (_bool->int list-format-only),
   "propertyName" property-name,
   "propertyFilter.value.number.unitOfMeasurement" property-number-unit-of-measurement,
   "propertyFilter.value.number.value" property-number-value,
   "propertyFilter.value.text.value" property-text-value,
   "propertyFilter.value.URL.value" (_encode-url property-url-value),
   "sortOrder.order" sort-order,
   "sortOrder.propertyName" sort-order-property-name]
  )
