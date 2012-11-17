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

(ns clj-ebay.price-research
  "This is an small Clojure binding for the Ebay API for finding products."
  (:use (clj-ebay core)))

(def ^{:private true} +price-research-api+ "http://api.dataunison.com/rest/")

(defn get-price-research "" [{:keys [token user-token developer-name keywords end-time-to research-period currency
                                     include-daily-statistics site-id child-category-id listing-limit min-price max-price]}]
  (let [url (str +price-research-api+ "?call_name=" "GetPriceResearch")
        url (_append-arg url "version" "2", "appid" *app-id*)
        url (_append-arg url "token" token, "usr_tok" user-token, "dev_name" developer-name, "QueryKeywords" keywords,
              "EndTimeTo" end-time-to, "ResearchPeriod" research-period, "Currency" currency,
              "IncludeDailyStatistics" (_bool->int include-daily-statistics), "siteid" site-id,
              "ChildCategoryID" child-category-id, "ListingLimit" listing-limit, "MinPrice" min-price, "MaxPrice" max-price)]
    (_fetch-url url)))
