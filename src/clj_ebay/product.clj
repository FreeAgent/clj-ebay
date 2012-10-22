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

(ns clj-ebay.product
  "This is an small Clojure binding for the Ebay Product API."
  (:use (clj-ebay core)
    (clojure.contrib def)))

(defvar- +product-api+ "http://svcs.ebay.com/services/marketplacecatalog/ProductService/v1")

;Imagine that this macro is a specialized do-template
(defmacro- make-prods [& specifics]
  (let [standard-fields '(global-id )]
    `(do ~@(for [[finder-sym finder-meth finder-version specific-appends] (partition 4 specifics)]
             `(defn ~finder-sym "" [{:keys ~(vec (concat standard-fields (_extract-vars specific-appends)))}]
                (let [url# (str +product-api+ "?OPERATION-NAME=" ~finder-meth)
                      url# (_append-arg url# "SERVICE-VERSION" ~finder-version, "SECURITY-APPNAME" *app-id*,
                             "GLOBAL-ID" (name ~'global-id), "RESPONSE-DATA-FORMAT" "JSON")
                      url# (str url# "&REST-PAYLOAD")
                      ;url# (_append-arg url# "affiliate.customId" ~'custom-id, "affiliate.networkId" ~'network-id,
                      ;       "affiliate.trackingId" ~'tracking-id, "buyerPostalCode" ~'buyer-postal-code,
                      ;       "paginationInput.entriesPerPage" ~'entries-per-page, "paginationInput.pageNumber" ~'page-number,
                      ;       "sortOrder" ~'sort-order)
                      url# (_append-arg url# ~@specific-appends)]
                  (_fetch-url url#)))
             ))))

(make-prods
  ; findCompatibilitiesBySpecification
  find-compatibilities-by-specification "findCompatibilitiesBySpecification" "1.2.0"
  ["categoryId" category-id,
   "compatibilityPropertyFilter.propertyName" compatibility-property-name,
   "compatibilityPropertyFilter.value.number.value" compatibility-property-number-value,
   "compatibilityPropertyFilter.value.text.value" compatibility-property-text-value,
   "compatibilityPropertyFilter.value.URL.value" (_encode-url compatibility-property-value-url),
   "dataSet" data-set, "datasetPropertyName" data-set-property-name,
   "exactMatch" (_bool->int exact-match),
   "paginationInput.entriesPerPage" entries-per-page, "paginationInput.pageNumber" page-number,
   "sortOrder.sortOrder.order" sort-order,
   "sortOrder.sortOrder.propertyName" sort-order-property-name,
   "sortOrder.sortPriority" sort-priority,
   "specification.propertyName" specification-property-name,
   "specification.value.number.value" specification-number-value,
   "specification.value.text.value" specification-text-value,
   "specification.value.URL.value" (_encode-url specification-url-value)]
  ; findProducts
  find-products "findProducts" "1.1.0"
  ["productSearch.categoryId" category-id, "productSearch.invocationId" invocation-id, "productSearch.keywords" keywords,
   "productSearch.dataset" data-set, "productSearch.datasetPropertyName" data-set-property-name,
   "productSearch.paginationInput.entriesPerPage" entries-per-page, "productSearch.paginationInput.pageNumber" page-number,
   "productSearch.productStatusFilter.excludeForeBayReviews" (_bool->int exclude-for-ebay-reviews),
   "productSearch.productStatusFilter.excludeForeBaySelling" (_bool->int exclude-for-ebay-selling),
   "productSearch.productStatusFilter.excludeForHalfSelling" (_bool->int exclude-for-half-selling),
   "productSearch.propertyFilter.propertyName" property-name,
   "productSearch.propertyFilter.value.number.unitOfMeasurement" property-number-unit-of-measurement,
   "productSearch.propertyFilter.value.number.value" property-number-value,
   "productSearch.propertyFilter.value.text.value" property-text-value,
   "productSearch.propertyFilter.value.URL.value" (_encode-url property-url-value),
   "productSearch.sortOrder.order" sort-order,
   "productSearch.sortOrder.propertyName" sort-order-property-name]
  ; findProductsByCompatibility
  find-products-by-compatibility "findProductsByCompatibility" "1.1.0"
  ["productByCompatibilitySearch.applicationPropertyFilter.propertyFilter.propertyName" app-property-name,
   "productByCompatibilitySearch.applicationPropertyFilter.propertyFilter.value.number.unitOfMeasurement" app-property-number-unit-of-measurement,
   "productByCompatibilitySearch.applicationPropertyFilter.propertyFilter.value.number.value" app-property-number-value,
   "productByCompatibilitySearch.applicationPropertyFilter.propertyFilter.value.text.value" app-property-text-value,
   "productByCompatibilitySearch.applicationPropertyFilter.propertyFilter.value.URL.value" (_encode-url app-property-url-value),
   "productByCompatibilitySearch.productSearch.categoryId" category-id,
   "productByCompatibilitySearch.productSearch.dataset" data-set,
   "productByCompatibilitySearch.productSearch.datasetPropertyName" data-set-property-name,
   "productByCompatibilitySearch.productSearch.invocationId" invocation-id,
   "productByCompatibilitySearch.productSearch.keywords" keywords,
   "productByCompatibilitySearch.productSearch.paginationInput.entriesPerPage" entries-per-page,
   "productByCompatibilitySearch.productSearch.paginationInput.pageNumber" page-number,
   "productByCompatibilitySearch.productSearch.productStatusFilter.excludeForeBayReviews" (_bool->int exclude-for-ebay-reviews),
   "productByCompatibilitySearch.productSearch.productStatusFilter.excludeForeBaySelling" (_bool->int exclude-for-ebay-selling),
   "productByCompatibilitySearch.productSearch.productStatusFilter.excludeForHalfSelling" (_bool->int exclude-for-half-selling),
   "productByCompatibilitySearch.productSearch.propertyFilter.propertyName" property-name,
   "productByCompatibilitySearch.productSearch.propertyFilter.value.number.unitOfMeasurement" property-number-unit-of-measurement,
   "productByCompatibilitySearch.productSearch.propertyFilter.value.number.value" property-number-value,
   "productByCompatibilitySearch.productSearch.propertyFilter.value.text.value" property-text-value,
   "productByCompatibilitySearch.productSearch.propertyFilter.value.URL.value" (_encode-url property-url-value),
   "productByCompatibilitySearch.productSearch.sortOrder.order" sort-order,
   "productByCompatibilitySearch.productSearch.sortOrder.propertyName" sort-order-property-name]
  ; getProductCompatibilities
  get-product-compatibilities "getProductCompatibilities" "1.1.0"
  ["applicationPropertyFilter.propertyFilter.propertyName" app-property-name,
   "applicationPropertyFilter.propertyFilter.value.number.unitOfMeasurement" app-property-number-unit-of-measurement,
   "applicationPropertyFilter.propertyFilter.value.number.value" app-property-number-value,
   "applicationPropertyFilter.propertyFilter.value.text.value" app-property-text-value,
   "applicationPropertyFilter.propertyFilter.value.URL.value" (_encode-url app-property-url-value),
   "dataset" data-set, "datasetPropertyName" data-set-property-name,
   "disabledProductFilter.excludeForeBayReviews" (_bool->int exclude-for-ebay-reviews),
   "disabledProductFilter.excludeForeBaySelling" (_bool->int exclude-for-ebay-selling),
   "disabledProductFilter.excludeForHalfSelling" (_bool->int exclude-for-half-selling),
   "paginationInput.entriesPerPage" entries-per-page, "paginationInput.pageNumber" page-number,
   "productIdentifier.EAN" ean-id, "productIdentifier.ePID" epid-id, "productIdentifier.ISBN" isbn-id,
   "productIdentifier.productId" product-id, "productIdentifier.UPC" upc-id,
   "sortOrder.sortOrder.order" sort-order, "sortOrder.sortOrder.propertyName" sort-order-property-name,
   "sortOrder.sortPriority" sort-priority]
  ; getProductDetails
  get-product-details "getProductDetails" "1.1.0"
  ["productDetailsRequest.dataset" data-set,
   "productDetailsRequest.datasetPropertyName" data-set-property-name,
   "productDetailsRequest.productIdentifier.EAN" ean-id,
   "productDetailsRequest.productIdentifier.ePID" epid-id,
   "productDetailsRequest.productIdentifier.ISBN" isbn-id,
   "productDetailsRequest.productIdentifier.productId" product-id,
   "productDetailsRequest.productIdentifier.UPC" upc-id,
   "productDetailsRequest.productStatusFilter.excludeForeBayReviews" (_bool->int exclude-for-ebay-reviews),
   "productDetailsRequest.productStatusFilter.excludeForeBaySelling" (_bool->int exclude-for-ebay-selling),
   "productDetailsRequest.productStatusFilter.excludeForHalfSelling" (_bool->int exclude-for-half-selling)]
  )
